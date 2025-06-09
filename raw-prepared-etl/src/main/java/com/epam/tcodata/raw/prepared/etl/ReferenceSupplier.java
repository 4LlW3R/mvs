package com.epam.tcodata.raw.prepared.etl;

import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawLibraryEvent;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationSubGroup;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is for cashing data that is used by different ETLs.
 * Cached data should be quite small because instance of this class will be transferred via serialization.
 */
public class ReferenceSupplier implements Serializable {

    private static final long serialVersionUID = 8351848993232678388L;

    private Map<Long, RawOrganisationSubGroup> organizationSubGroupMap;
    private Map<Long, RawOrganisationGroup> organizationGroupMap;
    private Map<Long, RawLibraryEvent> libraryEventMap;
    private Map<Long, RawAsset> assetMap;

    /**
     * Public main constructor, that collects all needed data from hive via supplied hive instance.
     *
     * @param rawHive instance of Hive raw database.
     */
    public ReferenceSupplier(IHive rawHive) {
        this.organizationSubGroupMap = readOrganizationSubGroupMap(rawHive);
        this.organizationGroupMap = readOrganizationMap(rawHive);
        this.libraryEventMap = readLibraryEventMap(rawHive);
        this.assetMap = readAssetMap(rawHive);
    }

    /**
     * Looks up for subgroup entity by its group Id. It there isn't any returns null.
     *
     * @param subGroupId subgroup Id
     * @return
     */
    public RawOrganisationSubGroup getSubGroup(Long subGroupId) {
        return this.organizationSubGroupMap.get(subGroupId);
    }

    /**
     * Looks up for group entity by its group Id. It there isn't any returns null.
     *
     * @param groupId group Id
     * @return
     */
    public RawOrganisationGroup getGroup(Long groupId) {
        return this.organizationGroupMap.get(groupId);
    }

    /**
     * Looks up for library event entity by its event Id. It there isn't any returns null.
     *
     * @param eventId event Id
     * @return
     */
    public RawLibraryEvent getLibraryEvent(Long eventId) {
        return this.libraryEventMap.get(eventId);
    }

    /**
     * Looks up for subgroup durable id  by its group Id. It there isn't any returns null.
     *
     * @param subGroupId subgroup Id
     * @return
     */
    public String getSubGroupDurableId(Long subGroupId) {
        RawOrganisationSubGroup rawOrganisationSubGroup = this.organizationSubGroupMap.get(subGroupId);
        return rawOrganisationSubGroup == null ? null : rawOrganisationSubGroup.getDurableId();
    }

    /**
     * Looks up for group durable id  by its group Id. It there isn't any returns null.
     *
     * @param groupId group Id
     * @return
     */
    public String getGroupDurableId(Long groupId) {
        RawOrganisationGroup rawOrganisationGroup = this.organizationGroupMap.get(groupId);
        return rawOrganisationGroup == null ? null : rawOrganisationGroup.getDurableId();
    }

    /**
     * Looks up for library event durable id by its event Id. It there isn't any returns null.
     *
     * @param eventId event Id
     * @return
     */
    public String getLibraryEventDurableId(Long eventId) {
        RawLibraryEvent rawLibraryEvent = this.libraryEventMap.get(eventId);
        return rawLibraryEvent == null ? null : rawLibraryEvent.getDurableId();
    }

    /**
     * Looks up for asset durable id by its asset Id. It there isn't any returns null.
     *
     * @param assetId asset Id
     * @return
     */
    public String getAssetDurableId(Long assetId) {
        RawAsset rawAsset = this.assetMap.get(assetId);
        return rawAsset == null ? null : rawAsset.getDurableId();
    }

    private Map<Long, RawOrganisationSubGroup> readOrganizationSubGroupMap(IHive rawHive) {
        IHiveRepository<RawOrganisationSubGroup> organisationSubGroupRepository = rawHive.repository(RawAreaEntityType.ORGANISATION_SUBGROUP_NORM);
        Dataset<Row> organisationSubGroupDataset = organisationSubGroupRepository.read();
        List<RawOrganisationSubGroup> organisationGroupList = HiveCommon.rowRddToEntityList(organisationSubGroupDataset.javaRDD(), RawOrganisationSubGroup.class);
        return organisationGroupList.stream()
                .collect(Collectors.toMap(RawOrganisationSubGroup::getGroupId, Function.identity(), (existing, replacement) -> existing));
    }

    private Map<Long, RawOrganisationGroup> readOrganizationMap(IHive rawHive) {
        IHiveRepository<RawOrganisationGroup> organisationGroupRepository = rawHive.repository(RawAreaEntityType.ORGANISATION_GROUP_NORM);
        Dataset<Row> organisationGroupDataset = organisationGroupRepository.read();
        List<RawOrganisationGroup> organisationGroupList = HiveCommon.rowRddToEntityList(organisationGroupDataset.javaRDD(), RawOrganisationGroup.class);
        return organisationGroupList.stream()
                .collect(Collectors.toMap(RawOrganisationGroup::getGroupId, Function.identity(), (existing, replacement) -> existing));
    }

    private Map<Long, RawLibraryEvent> readLibraryEventMap(IHive rawHive) {
        IHiveRepository<RawLibraryEvent> libraryEventRepository = rawHive.repository(RawAreaEntityType.LIBRARY_EVENT_NORM);
        Dataset<Row> libraryEventDataset = libraryEventRepository.read();
        List<RawLibraryEvent> libraryEventList = HiveCommon.rowRddToEntityList(libraryEventDataset.javaRDD(), RawLibraryEvent.class);
        return libraryEventList.stream()
                .collect(Collectors.toMap(RawLibraryEvent::getEventTypeId, Function.identity(), (existing, replacement) -> existing));
    }

    private Map<Long, RawAsset> readAssetMap(IHive rawHive) {
        IHiveRepository<RawLibraryEvent> assetRepository = rawHive.repository(RawAreaEntityType.ASSET_NORM);
        Dataset<Row> assetDataset = assetRepository.read();
        List<RawAsset> assetList = HiveCommon.rowRddToEntityList(assetDataset.javaRDD(), RawAsset.class);
        return assetList.stream()
                .collect(Collectors.toMap(RawAsset::getAssetId, Function.identity(), (existing, replacement) -> existing));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ subgroup size=" + this.organizationSubGroupMap.size()
                + ", group size=" + this.organizationGroupMap.size()
                + ", events size=" + this.libraryEventMap.size()
                + ", assets size=" + this.assetMap.size()
                + "}";
    }
}
