package com.epam.tcodata.mdm;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IKeyManager extends AutoCloseable {


    /**
     * This name matches to any subscription. So, if you need to describe a rule for any subscription you can use
     * this instead using multiple rules for every subscription.
     * Rules with such subscription name have priority against particular ones.
     */
    String ANY_SUBS = "*";

    /**
     * Returns set of all subscriptions that is known for that Key manager.
     *
     * @return set of subscriptionsl
     */
    Set<String> subscriptions();

    /**
     * Assigns a durable key for a fact entity. To be alone point of UUID generation.
     *
     * @param entityType fact entity type
     * @param naturalKeyValue natural key value
     * @return
     */
    UUID factDurableKey(EntityType entityType, String naturalKeyValue);

    /**
     * Provides a surrogate key for given entity, its kind, keeping in mind API version and subscription.
     * The key might be either generated for new entities or retrieved from a special storage for known entities.
     * This method should be used for dimensions to assign or reassign their surrogate keys by natural keys.
     *
     * @param entity - the dimension entity itself
     * @param version - API version
     * @param subscription - subscription id
     * @param entityType - entity kind
     * @return Desision object, that contains surrogate key as UUID, the assigning reason etc. or null in case
     *         of impossibility to assign a key.
     */
    Decision findOrCreate(
            Object entity,
            ApiVersion version,
            String  subscription,
            EntityType entityType
    );

    /**
     * Provides the whole set of all dimensions in each fact entity. The set is returned in a map, whose keys are
     * entity kind values, and values are lists of special objects SearchingResult type. That type contains
     * the surrogate key and the name for each dimension that fact entity points to. If corresponding dimension isn't
     * found the special value of UUID "00000000-0000-0000-c000-000000000046" is set. You may refer to it as
     * SearchingResult.UNKNOWN in your code. This value indicates that right now the corresponding dimension couldn't
     * be define. Probably because of delay of downloading its data.
     * Calling side should decide how to use the keys to enrich primary entities. The simplest way is to inject their
     * values in a new object.
     *
     * @param entity - the fact entity itself
     * @param version - API version
     * @param subscription - subscription id
     * @param entityType - entity kind
     * @return map of EntityKind -> List of SearchingResult
     */
    Map<EntityType, List<SearchingResult>> keysSubstitution(
            Object entity,
            ApiVersion version,
            String subscription,
            EntityType entityType
    );

    /**
     * Provides the whole set of all dimensions in each fact entity. The set is returned in a map, whose keys are
     * entity kind values, and values are lists of special objects SearchingResult type. That type contains
     * the surrogate key and the name for each dimension that fact entity points to. If corresponding dimension isn't
     * found the special value of UUID "00000000-0000-0000-c000-000000000046" is set. You may refer to it as
     * SearchingResult.UNKNOWN in your code. This value indicates that right now the corresponding dimension couldn't
     * be define. Probably because of delay of downloading its data.
     * Calling side should decide how to use the keys to enrich primary entities. The simplest way is to inject their
     * values in a new object.
     *
     * @param entities - the list of facts entities
     * @param version - API version
     * @param subscription - subscription id
     * @param entityType - entity kind
     * @return map of EntityKind -> List of SearchingResult
     */
    Map<EntityType, List<SearchingResult>> keysSubstitutions(
            List<Object> entities,
            ApiVersion version,
            String subscription,
            EntityType entityType
    );

    /**
     * Generates a new durable key.
     *
     * @param entityType fact entity type
     * @param naturalKeyValue natural key value
     * @return
     */
    UUID newDurableKey(EntityType entityType, String naturalKeyValue);


    /**
     * Provides DaoFactory that is used for storing/restoring data to/from a storage.
     *
     * @return DaoFactory.
     */
    IDaoFactory getDaoFactory();

    /**
     * Refreshes all internal structures in case of changing config database by external agents.
     */
    void refresh();
}
