package com.epam.tcodata.eventhub.dal;

import com.epam.tcodata.eventhub.dal.exceptions.WrongMappingException;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.Secret;

public enum EventHubInfo {

    POSITION("fact-dim", Secret.EventHub.NameSpace.Raw),
    EVENT("fact-dim", Secret.EventHub.NameSpace.Raw),
    TRIP("fact-dim", Secret.EventHub.NameSpace.Raw),
    DRIVER("fact-dim", Secret.EventHub.NameSpace.Raw),
    ASSET("fact-dim", Secret.EventHub.NameSpace.Raw),
    LOCATION("fact-dim", Secret.EventHub.NameSpace.Raw),
    LIBRARY_EVENT("fact-dim", Secret.EventHub.NameSpace.Raw),
    ORGANIZATION_GROUP("fact-dim", Secret.EventHub.NameSpace.Raw),
    ORGANIZATION_SUBGROUP("fact-dim", Secret.EventHub.NameSpace.Raw),
    TACHO("fact-dim", Secret.EventHub.NameSpace.Raw),
    OVERTAKING("overtaking", Secret.EventHub.NameSpace.Overtaking),
    OVERTAKING_VIOLATION("overtaking", Secret.EventHub.NameSpace.Overtaking),
    CONFIRMED_OVERTAKING_VIOLATION("confirmed-overtaking", Secret.EventHub.NameSpace.ConfirmedOvertaking),
    ROAD_CONDITION_VIOLATION("road-condition", Secret.EventHub.NameSpace.RoadCondition),
    ROAD_CONDITION("road-condition", Secret.EventHub.NameSpace.RoadCondition);

    private String namespaceType;
    private Secret.EventHub.NameSpace nameSpace;

    EventHubInfo(String namespaceType, Secret.EventHub.NameSpace nameSpace) {
        this.namespaceType = namespaceType;
        this.nameSpace = nameSpace;
    }

    /**
     * Returns eventHub namespace type.
     * This property is used as a prefix, when
     * getting eventHub properties from resource file.
     *
     * @return name space property prefix.
     */
    public String getNamespaceType() {
        return this.namespaceType;
    }

    /**
     * Returns eventHub namespace.
     * This property is used as part of secret identity to get accessKey from SecretStorage.
     *
     * @return
     */
    public Secret.EventHub.NameSpace getNameSpace() {
        return this.nameSpace;
    }

    /**
     * Returns {@link EventHubInfo} by {@link EntityType}.
     *
     * @param entityType {@link EntityType}
     * @return {@link EventHubInfo}
     */
    public static EventHubInfo getEventHubInfoByEntityType(EntityType entityType) {
        switch (entityType) {
            case POSITION:
                return POSITION;
            case EVENT:
                return EVENT;
            case TRIP:
                return TRIP;
            case DRIVER:
                return DRIVER;
            case ASSET:
                return ASSET;
            case LIBRARY_EVENT:
                return LIBRARY_EVENT;
            case LOCATION:
                return LOCATION;
            case ORGANISATION_GROUP:
                return ORGANIZATION_GROUP;
            case ORGANISATION_SUBGROUP:
                return ORGANIZATION_SUBGROUP;
            case TACHO:
                return TACHO;
            default:
                throw new WrongMappingException("Entity type " + entityType + " does not map to any event hub info");
        }
    }
}
