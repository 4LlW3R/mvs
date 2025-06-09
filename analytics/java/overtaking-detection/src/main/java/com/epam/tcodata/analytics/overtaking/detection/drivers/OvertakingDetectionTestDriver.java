package com.epam.tcodata.analytics.overtaking.detection.drivers;

import com.epam.tcodata.analytics.overtaking.detection.factory.IOvertakingDetectionFactory;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.PureGPSProcessing;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSPoint;
import org.apache.commons.cli.CommandLine;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.epam.tcodata.analytics.overtaking.detection.Config.getSparkSession;

public class OvertakingDetectionTestDriver implements IDriver, Serializable {
    private static final long serialVersionUID = 4873115901979539939L;

    private static final String DWH_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSS";
    public static final String OPTION_INPUT_CSV_FILE_PATH = "input";
    public static final String OPTION_OUTPUT_CSV_FILE_PATH = "output";

    @Override
    public void runJob(CommandLine commandLine, IOvertakingDetectionFactory factory) {
        String appName = commandLine.getOptionValue(OPTION_APP_NAME);
        String inputPath = commandLine.getOptionValue(OPTION_INPUT_CSV_FILE_PATH);
        String outputPath = commandLine.getOptionValue(OPTION_OUTPUT_CSV_FILE_PATH);

        SparkSession session = getSparkSession(appName);

        Dataset<GPSPoint> gpsDF = getDatasetFromCSV(session, inputPath);

        Dataset<OvertakingEvent> ovt = PureGPSProcessing.processData(gpsDF);

        ovt.coalesce(1)
                .write()
                .option("header", "true")
                .orc(outputPath);
    }

    private Dataset<GPSPoint> getDatasetFromCSV(SparkSession session, String inputPath) {
        Encoder<GPSPoint> gpsEncoder = Encoders.bean(GPSPoint.class);
        StructType csvSchema = getCSVSchema();
        return session
                .read()
                .schema(csvSchema)
                .option("header", true)
                .option("timestampFormat", DWH_DATETIME_FORMAT)
                .csv(inputPath)
                .as(gpsEncoder);
    }

    /**
     * Creates CSV schema to read test csv files. Can't use "inferSchema" options because of incorrect encoding.
     */
    private StructType getCSVSchema() {
        List<StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("ID", DataTypes.StringType, false));
        fields.add(DataTypes.createStructField("SubscriptionID", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("ExternalID", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("IngestedDateUTC", DataTypes.TimestampType, false));
        fields.add(DataTypes.createStructField("VehicleID", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("DriverId", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("OriginalId", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("BlockSeq", DataTypes.LongType, false));
        fields.add(DataTypes.createStructField("Time", DataTypes.TimestampType, false));
        fields.add(DataTypes.createStructField("Latitude", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("Longitude", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("Altitude", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("Heading", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("Satellites", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("HDop", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("AgeOfReading", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("DistanceSinceReading", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("Velocity", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("IsAvl", DataTypes.BooleanType, false));
        fields.add(DataTypes.createStructField("Odometer", DataTypes.IntegerType, false));
        fields.add(DataTypes.createStructField("CoordValid", DataTypes.BooleanType, false));
        fields.add(DataTypes.createStructField("lineageCode", DataTypes.IntegerType, false));
        return DataTypes.createStructType(fields);
    }
}
