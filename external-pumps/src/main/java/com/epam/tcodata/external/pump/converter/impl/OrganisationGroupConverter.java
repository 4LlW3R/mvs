package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.time.Instant;

import static com.epam.tcodata.external.pump.util.ConverterUtil.timestampToDateTime;

public class OrganisationGroupConverter implements IConverter<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup> {

    private static final long serialVersionUID = 5169309978772841682L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedOrganisationGroup convertToEnriched(OrganisationGroup organisationGroup,
                                                       AbstractDto dto,
                                                       IKeyManager keyManager) {
        EnrichedOrganisationGroup enrichedOrganisationGroup = new EnrichedOrganisationGroup(organisationGroup);

        Decision decision = keyManager.findOrCreate(organisationGroup, ApiVersion.API_2_0, String.valueOf(((DimensionDto) dto).getOrgGroupId()), EntityType.ORGANISATION_GROUP);

        String surrogateKeyStr = decision == null ? null : decision.getSurrogateKey().toString();

        enrichedOrganisationGroup
                //enriched with additional info
                .setDurableId(surrogateKeyStr)
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((DimensionDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedOrganisationGroup;
    }

    @Override
    public AvroOrganisationGroup convertToAvro(EnrichedOrganisationGroup enrichedOrganisationGroup) {
        return AvroOrganisationGroup.newBuilder()

                // enriched fields
                .setDurableId(enrichedOrganisationGroup.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedOrganisationGroup.getIngestedDateUtc()))
                .setSubscriptionId(enrichedOrganisationGroup.getSubscriptionId())
                .setLineageCode(enrichedOrganisationGroup.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setGroupId(enrichedOrganisationGroup.getGroupId())
                .setType(enrichedOrganisationGroup.getType())
                .setDisplayTimeZone(enrichedOrganisationGroup.getDisplayTimeZone())
                .setName(enrichedOrganisationGroup.getName())
                .build();
    }

    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedOrganisationGroup> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedOrganisationGroup, Row>) enrichedOrganisationGroup ->
                RowFactory.create(enrichedOrganisationGroup.getOrderedValues()));
    }

    @SuppressWarnings("CPD-START")
    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("group_id", DataTypes.LongType, true),
                        DataTypes.createStructField("type", DataTypes.StringType, true),
                        DataTypes.createStructField("display_time_zone", DataTypes.StringType, true),
                        DataTypes.createStructField("name", DataTypes.StringType, true)
                }
        );
    }
}
