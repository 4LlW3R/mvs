package com.epam.tcodata.internal.pump.converter.dimension;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class OrganisationGroupConverter extends AbstractEntityConverter<AvroOrganisationGroup, EnrichedOrganisationGroup, RawOrganisationGroup> {

    private static final long serialVersionUID = -8639983198205404069L;

    /**
     * Main public constructor.
     */
    public OrganisationGroupConverter() {
        /***  Default implementation ***/
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedOrganisationGroup
     */
    @Override
    public EnrichedOrganisationGroup convertToEnriched(AvroOrganisationGroup avro) {
        OrganisationGroup.OrganisationGroupBuilder organisationGroupBuilder = new OrganisationGroup.OrganisationGroupBuilder()
                .setGroupId(avro.getGroupId())
                .setType(checkedToString(avro.getType()))
                .setDisplayTimeZone(checkedToString(avro.getDisplayTimeZone()))
                .setName(checkedToString(avro.getName()));

        OrganisationGroup organisationGroup = organisationGroupBuilder.build();

        return new EnrichedOrganisationGroup(organisationGroup)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeOrganisationGroup
     */
    @Override
    public RawOrganisationGroup convertToRaw(EnrichedOrganisationGroup enriched, Timestamp persistedDate) {
        RawOrganisationGroup dataLakeOrganisationGroup = new RawOrganisationGroup();
        // DataLakeEntity fields
        dataLakeOrganisationGroup.setDurableId(enriched.getDurableId());
        dataLakeOrganisationGroup.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeOrganisationGroup.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeOrganisationGroup.setLineageCode(enriched.getLineageCode());
        dataLakeOrganisationGroup.setPersistedDateUtc(persistedDate);
        // DataLakeOrganisationGroup fields
        dataLakeOrganisationGroup.setGroupId(enriched.getGroupId());
        dataLakeOrganisationGroup.setType(enriched.getType());
        dataLakeOrganisationGroup.setDisplayTimeZone(enriched.getDisplayTimeZone());
        dataLakeOrganisationGroup.setName(enriched.getName());
        return dataLakeOrganisationGroup;
    }

    @Override
    public StructType getSchemaForWrite() {
        return new StructType(
                new StructField[]{
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("persisted_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("group_id", DataTypes.LongType, true),
                        DataTypes.createStructField("type", DataTypes.StringType, true),
                        DataTypes.createStructField("display_time_zone", DataTypes.StringType, true),
                        DataTypes.createStructField("name", DataTypes.StringType, true)
                }
        );
    }

}
