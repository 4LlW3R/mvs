package com.epam.tcodata.internal.pump.handler.fact;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class TripDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = 1022673717864107398L;

    private SubTripDataHandler subTripDataHandler;

    /**
     * Public main contructor.
     *
     * @param entityFactory      the fabric that created this handler.
     * @param sparkSession       the Spark session in which this handler is actual.
     * @param subTripDataHandler subordinate handler.
     */
    public TripDataHandler(IInternalFactory entityFactory, SparkSession sparkSession, SubTripDataHandler subTripDataHandler) {
        super(entityFactory, sparkSession);

        this.subTripDataHandler = subTripDataHandler;
    }

    @Override
    protected void handle(JavaRDD<IEnrichable> enrichedJavaRDD,
                          SparkSession sparkSession,
                          Timestamp persistedDate,
                          IHiveOffsetService hiveOffsetService,
                          EntityType entityType) {

        super.handle(enrichedJavaRDD, sparkSession, persistedDate, hiveOffsetService, entityType);

        JavaRDD<IEnrichable> enrichedEntities = enrichedJavaRDD
                .mapPartitions(enrichableIterator -> {
                    List<IEnrichable> enrichedTripList = IteratorUtils.toList(enrichableIterator);
                    return enrichedTripList.stream()
                            .flatMap(enrichedTrip -> ((EnrichedTrip) enrichedTrip).getEnrichedSubTripList().stream())
                            .collect(Collectors.toList())
                            .iterator();
                })
                .map(e -> e);

        if (!enrichedEntities.isEmpty()) {
            this.subTripDataHandler.handle(enrichedEntities, sparkSession, persistedDate, hiveOffsetService, entityType);
        }

    }
}
