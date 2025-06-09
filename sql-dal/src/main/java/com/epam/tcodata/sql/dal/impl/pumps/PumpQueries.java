package com.epam.tcodata.sql.dal.impl.pumps;

import com.epam.tcodata.sql.dal.IQuerySupplier;

public enum PumpQueries implements IQuerySupplier {

    SELECT_MIX_OFFSET_BY_GROUP_ID_AND_ENTITY_TYPE_CODE(
            "SELECT * FROM cfg.MixOffset "
                    + "WHERE OrganisationGroupId = :OrganisationGroupId "
                    + "AND "
                    + "EntityType = :EntityType "
    ),

    UPDATE_EVENT_HUB_OFFSETS_BY_ENTITY_TYPE_CODE_AND_PARTITION_ID(
            "UPDATE cfg.EventHubOffset "
                    + "SET "
                    + "SeqNo = :seqNo, "
                    + "LastSyncDateUtc = :lastSyncDateUtc, "
                    + "LastSyncElementCount = :lastSyncElementCount, "
                    + "TotalElementsCount = TotalElementsCount + :lastSyncElementCount "
                    + "WHERE "
                    + "EntityType = :entityType "
                    + "AND "
                    + "PartitionId = :partitionId "),

    DELETE_EVENT_HUB_OFFSETS_BY_ENTITY_TYPE_CODE(
            "DELETE FROM cfg.EventHubOffset "
                    + "WHERE EntityType = :EntityType ");

    PumpQueries(String query) {
        this.query = query;
    }

    private String query;

    @Override
    public String query() {
        return this.query;
    }
}
