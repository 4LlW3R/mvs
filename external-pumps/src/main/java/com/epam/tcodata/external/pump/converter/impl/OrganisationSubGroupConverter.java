package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationSubGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
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

public class OrganisationSubGroupConverter implements IConverter<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup> {

    private static final long serialVersionUID = -6294458756234764702L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedOrganisationSubGroup convertToEnriched(OrganisationSubGroup organisationSubGroup,
                                                          AbstractDto dto,
                                                          IKeyManager keyManager) {
        EnrichedOrganisationSubGroup enrichedOrganisationSubGroup = new EnrichedOrganisationSubGroup(organisationSubGroup);

        Decision decision = keyManager.findOrCreate(organisationSubGroup, ApiVersion.API_2_0, String.valueOf(((DimensionDto) dto).getOrgGroupId()), EntityType.ORGANISATION_SUBGROUP);

        String surrogateKeyStr = decision == null ? null : decision.getSurrogateKey().toString();

        enrichedOrganisationSubGroup
                //enriched with additional info
                .setDurableId(surrogateKeyStr)
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((DimensionDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedOrganisationSubGroup;
    }

    @Override
    public AvroOrganisationSubGroup convertToAvro(EnrichedOrganisationSubGroup enrichedOrganisationSubGroup) {
        return AvroOrganisationSubGroup.newBuilder()

                // enriched fields
                .setDurableId(enrichedOrganisationSubGroup.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedOrganisationSubGroup.getIngestedDateUtc()))
                .setSubscriptionId(enrichedOrganisationSubGroup.getSubscriptionId())
                .setLineageCode(enrichedOrganisationSubGroup.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields

                .setGroupId(enrichedOrganisationSubGroup.getGroupId())
                .setParentOrgId(enrichedOrganisationSubGroup.getParentOrgId())
                .setParentSubGroupId(enrichedOrganisationSubGroup.getParentSubGroupId())
                .setName(enrichedOrganisationSubGroup.getName())
                .setType(enrichedOrganisationSubGroup.getType())
                .build();
    }

    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedOrganisationSubGroup> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedOrganisationSubGroup, Row>) enrichedOrganisationSubGroup ->
                RowFactory.create(enrichedOrganisationSubGroup.getOrderedValues()));
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
                        DataTypes.createStructField("parent_org_id", DataTypes.LongType, true),
                        DataTypes.createStructField("parent_sub_group_id", DataTypes.LongType, true),
                        DataTypes.createStructField("name", DataTypes.StringType, true),
                        DataTypes.createStructField("type", DataTypes.StringType, true)
                }
        );
    }
}
