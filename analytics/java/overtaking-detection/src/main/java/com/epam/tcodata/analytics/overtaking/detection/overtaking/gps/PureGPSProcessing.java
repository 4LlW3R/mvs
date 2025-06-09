package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps;


import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSCollectedTrajectory;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSPoint;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSTrajectory;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.functions;
import org.apache.spark.storage.StorageLevel;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.collect_list;

public final class PureGPSProcessing {
    private static final Logger LOGGER = Logger.getLogger(PureGPSProcessing.class);

    private static final String TRAJECTORY_B_STR_CONST = "trajectoryB";
    private static final String VEHICLE_DURABLE_ID_STR_CONST = "vehicleDurableId";
    private static final String VEHICLE_DURABLE_ID_B_STR_CONST = "vehicleDurableIdB";
    private static final String TRAJECTORY_STR_CONST = "trajectory";

    private PureGPSProcessing() {
    }

    /**
     * Entrypoint for whole data processing pipeline for overtaking finding.
     *
     * @param gpsPointDataset {@link Dataset} of {@link GPSPoint}
     * @return {@link Dataset} of {@link OvertakingEvent}
     */
    public static Dataset<OvertakingEvent> processData(Dataset<GPSPoint> gpsPointDataset) {
        Dataset<GPSPoint> filteredDataset = gpsPointDataset.filter((FilterFunction<GPSPoint>) point ->
                point.getVelocity() > 0.0
        );
        LOGGER.info("Start collecting trips trajectories...");
        Dataset<GPSCollectedTrajectory> collectedTrajectoryDataset = collectTrips(filteredDataset).coalesce(16);
        Dataset<OvertakingEvent> overtakingDataset = findOvertakings(collectedTrajectoryDataset);
        overtakingDataset = overtakingDataset.distinct();
        return overtakingDataset;
    }

    private static Dataset<GPSCollectedTrajectory> collectTrips(Dataset<GPSPoint> gpsPointDataset) {
        Dataset<Row> gpsWithTrips = gpsPointDataset
                .select(functions
                                .struct(gpsPointDataset.col("latitude"),
                                        gpsPointDataset.col("longitude"),
                                        gpsPointDataset.col("time"),
                                        gpsPointDataset.col("velocity"),
                                        gpsPointDataset.col("driverDurableId"))
                                .as(Encoders.bean(GPSTripPoint.class)).alias("tripPoint"),
                        gpsPointDataset.col(VEHICLE_DURABLE_ID_STR_CONST));

        return  gpsWithTrips
                .groupBy(VEHICLE_DURABLE_ID_STR_CONST)
                .agg(collect_list("tripPoint").alias(TRAJECTORY_STR_CONST))
                .as(Encoders.bean(GPSCollectedTrajectory.class));
    }

    private static Dataset<OvertakingEvent> findOvertakings(Dataset<GPSCollectedTrajectory> trajectoryDataset) {
        Dataset<GPSCollectedTrajectory> trajectoryA = trajectoryDataset.alias("trajectoryA");
        Dataset<Row> trajectoryB = trajectoryDataset
                .select(col(VEHICLE_DURABLE_ID_STR_CONST).alias(VEHICLE_DURABLE_ID_B_STR_CONST),
                        col(TRAJECTORY_STR_CONST).alias(TRAJECTORY_B_STR_CONST))
                .alias(TRAJECTORY_B_STR_CONST);
        Dataset<Row> joinedTrajectory = trajectoryA
                .join(trajectoryB, trajectoryA.col(VEHICLE_DURABLE_ID_STR_CONST).geq(trajectoryB.col(VEHICLE_DURABLE_ID_B_STR_CONST)))
                .persist(StorageLevel.MEMORY_AND_DISK_SER());

        Dataset<OvertakingEvent> overtakingDataset = joinedTrajectory
                .flatMap(PureGPSProcessing::runDetectionForRows, Encoders.kryo(OvertakingEvent.class))

                .filter((FilterFunction<OvertakingEvent>) Objects::nonNull)
                .persist(StorageLevel.MEMORY_AND_DISK_SER());
        joinedTrajectory.unpersist();
        trajectoryDataset.unpersist();
        return overtakingDataset;
    }


    private static Iterator<OvertakingEvent> runDetectionForRows(Row row) {
        GPSTripPoint[] pointsA = trajectoryRowToArray(row.getList(row.fieldIndex(TRAJECTORY_STR_CONST)));
        GPSTripPoint[] pointsB = trajectoryRowToArray(row.getList(row.fieldIndex(TRAJECTORY_B_STR_CONST)));

        GPSTrajectory trajectoryA = new GPSTrajectory(
                pointsA,
                row.getString(row.fieldIndex(VEHICLE_DURABLE_ID_STR_CONST)));
        GPSTrajectory trajectoryB = new GPSTrajectory(
                pointsB,
                row.getString(row.fieldIndex(VEHICLE_DURABLE_ID_B_STR_CONST)));
        return trajectoryA.findOvertake(trajectoryB).iterator();
    }


    private static GPSTripPoint[] trajectoryRowToArray(List<GenericRowWithSchema> trajectory) {
        return trajectory
                .stream()
                .filter(Objects::nonNull)
                .map(x -> new GPSTripPoint(
                        x.getTimestamp(x.fieldIndex("time")),
                        x.getDouble(x.fieldIndex("latitude")),
                        x.getDouble(x.fieldIndex("longitude")),
                        x.getDouble(x.fieldIndex("velocity")),
                        x.getString(x.fieldIndex("driverDurableId"))))
                .toArray(GPSTripPoint[]::new);
    }
}
