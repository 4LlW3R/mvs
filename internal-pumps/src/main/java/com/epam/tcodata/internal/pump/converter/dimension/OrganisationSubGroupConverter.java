package com.epam.tcodata.internal.pump.converter.dimension;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationSubGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationSubGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@SuppressWarnings("CPD-START")
public class OrganisationSubGroupConverter extends AbstractEntityConverter<AvroOrganisationSubGroup, EnrichedOrganisationSubGroup, RawOrganisationSubGroup> {

    private static final long serialVersionUID = 5203075123591227579L;

    /**
     * Main public constructor.
     */
    public OrganisationSubGroupConverter() {
        /***  Default implementation ***/
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedOrganisationSubGroup
     */
    @Override
    public EnrichedOrganisationSubGroup convertToEnriched(AvroOrganisationSubGroup avro) {
        OrganisationSubGroup.OrganisationSubGroupBuilder organisationSubGroupBuilder = new OrganisationSubGroup.OrganisationSubGroupBuilder()
                .setGroupId(avro.getGroupId())
                .setParentOrgId(avro.getParentOrgId())
                .setParentSubGroupId(avro.getParentSubGroupId())
                .setName(checkedToString(avro.getName()))
                .setType(checkedToString(avro.getType()));

        OrganisationSubGroup organisationSubGroup = organisationSubGroupBuilder.build();

        return new EnrichedOrganisationSubGroup(organisationSubGroup)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeOrganisationSubGroup
     */
    @Override
    public RawOrganisationSubGroup convertToRaw(EnrichedOrganisationSubGroup enriched, Timestamp persistedDate) {
        RawOrganisationSubGroup dataLakeOrganisationSubGroup = new RawOrganisationSubGroup();
        // DataLakeEntity fields
        dataLakeOrganisationSubGroup.setDurableId(enriched.getDurableId());
        dataLakeOrganisationSubGroup.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeOrganisationSubGroup.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeOrganisationSubGroup.setLineageCode(enriched.getLineageCode());
        dataLakeOrganisationSubGroup.setPersistedDateUtc(persistedDate);
        // DataLakeOrganisationSubGroup fields
        dataLakeOrganisationSubGroup.setGroupId(enriched.getGroupId());
        dataLakeOrganisationSubGroup.setParentOrgId(enriched.getParentOrgId());
        dataLakeOrganisationSubGroup.setParentSubGroupId(enriched.getParentSubGroupId());
        dataLakeOrganisationSubGroup.setName(enriched.getName());
        dataLakeOrganisationSubGroup.setType(enriched.getType());
        return dataLakeOrganisationSubGroup;
    }

    @Override
    public StructType getSchemaForWrite() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("persisted_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("group_id", DataTypes.LongType, true),
                        DataTypes.createStructField("parent_org_id", DataTypes.LongType, true),
                        DataTypes.createStructField("parent_subgroup_id", DataTypes.LongType, true),
                        DataTypes.createStructField("name", DataTypes.StringType, true),
                        DataTypes.createStructField("type", DataTypes.StringType, true)
                }
        );
    }

}
