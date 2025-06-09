package com.epam.tcodata.mdm.base;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.mdm.rules.IRuleType;
import com.epam.tcodata.mdm.rules.RuleType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mdm.AssigningReason;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.*;
import com.epam.tcodata.sql.dal.service.IReadWriteService;
import com.epam.tcodata.sql.dal.service.mdm.IRelationService;
import com.epam.tcodata.sql.dal.service.mdm.IRuleService;
import com.epam.tcodata.sql.dal.service.mdm.IStepService;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

/**
 * Base class for custom implementation of any Key Managers.
 */
public abstract class AbstractKeyManager implements IKeyManager {

    private KeyManagerVersion currentVersion;
    private Set<String> subscriptions;
    private Map<Key, Value> allSteps;
    private IDaoFactory daoFactory;

    /**
     * Public constructor.
     *
     * @param version    version of KeyManager that should be started.
     * @param daoFactory DAO factory.
     */
    protected AbstractKeyManager(KeyManagerVersion version, IDaoFactory daoFactory) {
        this.currentVersion = version;
        this.daoFactory = daoFactory;

        initAll(daoFactory);
    }

    @Override
    public Set<String> subscriptions() {
        return new HashSet<>(this.subscriptions);
    }

    @Override
    public UUID factDurableKey(EntityType entityType, String naturalKeyValue) {
        return newDurableKey(entityType, naturalKeyValue);
    }

    @Override
    public Decision findOrCreate(Object entity, ApiVersion version, String subscription, EntityType entityType) {
        IMapper mapper = giveMapper(entityType);
        Map<String, Object> map = mapper.map(entity);

        List<Step> steps = steps(version, subscription, entityType);

        if (steps != null) {
            for (Step step : steps) {
                String naturalKeyName = step.getNaturalKeyName();
                IRuleType ruleType = RuleType.valueOf(step.getRuleType());
                Decision decision = ruleType.performDecision(this, this.daoFactory, entityType, map, step);

                if (decision.isOk()) {
                    AssigningReason assigningReason = decision.getAssigningReason();
                    switch (assigningReason) {
                        case NEW_KEY:
                        case RENEW_KEY:
                            registerDecision(decision, entityType, naturalKeyName, new Date());
                            break;

                        case SPECULATIVE_KEY:
                            Decision newDecision = Decision.renewKey(decision.getKey(), decision.getSurrogateKey(), decision.getKeyMappingId());
                            registerDecision(newDecision, entityType, naturalKeyName, new Date());
                            break;

                        default: // nothing to do
                    }
                    return decision;
                }
            }
        }
        return null;
    }

    @Override
    public Map<EntityType, List<SearchingResult>> keysSubstitution(Object entity, ApiVersion version, String subscription, EntityType entityType) {
        return this.keysSubstitutions(Arrays.asList(entity), version, subscription, entityType);
    }

    @Override
    public Map<EntityType, List<SearchingResult>> keysSubstitutions(List<Object> entities, ApiVersion version, String subscription, EntityType entityType) {
        Map<EntityType, List<SearchingResult>> result = new HashMap<>();
        if (entities == null || entities.isEmpty()) {
            return result;
        }

        IMapper mapper = giveMapper(entityType);
        List<Relation> relations = relations(version, subscription, entityType);
        if (relations != null) {
            Map<EntityType, List<DalRequestData>> dalRequestsData = collectNaturalKeys(
                    relations, mapper, entities);
            Map<EntityType, List<DalRequestData>> dalRequestsDataWithIDs = fillUUIDs(dalRequestsData);
            result = constructAllUUIDsInResult(dalRequestsDataWithIDs);
        }
        return result;
    }

    private Map<EntityType, List<SearchingResult>> constructAllUUIDsInResult(
            Map<EntityType, List<DalRequestData>> dalRequestsDataWithIDs) {

        Map<EntityType, List<SearchingResult>> result = new HashMap<>();
        dalRequestsDataWithIDs.entrySet().forEach(entry -> {
            EntityType entityType = entry.getKey();
            List<DalRequestData> dalRequestData = entry.getValue();

            List<SearchingResult> searchingResults = dalRequestData.stream()
                    .map(requestData -> new SearchingResult(requestData.getSurrogateKeyName(), requestData.getUuid()))
                    .collect(Collectors.toList());

            result.put(entityType, searchingResults);
        });

        return result;
    }

    private Map<EntityType, List<DalRequestData>> fillUUIDs(Map<EntityType, List<DalRequestData>> dalRequestsData) {
        dalRequestsData.entrySet().forEach(entry -> fillUUIDs(entry.getValue()));
        return dalRequestsData;
    }

    private List<DalRequestData> fillUUIDs(List<DalRequestData> dalRequestsData) {
        Map<DalRequestData, UUID> idsMap = findByNaturalKey(dalRequestsData);
        dalRequestsData.forEach(dalRequestData -> {
            UUID uuid = findOrCreateUUIDs(idsMap, dalRequestData);
            dalRequestData.setUuid(uuid);
        });
        return dalRequestsData;
    }

    private UUID findOrCreateUUIDs(Map<DalRequestData, UUID> idsMap, DalRequestData dalRequestData) {
        if (idsMap.get(dalRequestData) == null) {
            String naturalKey = dalRequestData.getNaturalKey();
            EntityType dimEntityType = dalRequestData.getDimEntityType();

            UUID uuid = newDurableKey(dimEntityType, naturalKey);

            Decision decision = Decision.anyDesicion(AssigningReason.SPECULATIVE_KEY, naturalKey,  uuid, null);
            registerDecision(decision, dimEntityType, dalRequestData.getNaturalKeyName(), new Date());

            idsMap.put(dalRequestData, uuid);
        }

        return idsMap.get(dalRequestData);
    }

    private Map<EntityType, List<DalRequestData>> collectNaturalKeys(
            List<Relation> relations,
            IMapper mapper,
            List<Object> entities) {

        Map<EntityType, List<DalRequestData>> result = new HashMap<>();

        for (Relation relation : relations) {
            EntityType relatedEntityType = relation.getEntityType();

            String relatedNaturalKeyName = relation.getNaturalKeyName();
            EntityType dimEntityType = relation.getEntityType();
            String surrogateKeyName = relation.getSurrogateKeyName();

            List<DalRequestData> requestDataWithSpecifiedEntity = result.computeIfAbsent(dimEntityType, e -> new ArrayList<>());
            entities.forEach(entity -> {
                Map<String, Object> entityMap = mapper.map(entity);
                String naturalKey = String.valueOf(entityMap.get(relatedNaturalKeyName)); // TO DO possible it is not so simple mapping
                requestDataWithSpecifiedEntity.add(new DalRequestData(relatedNaturalKeyName, naturalKey, dimEntityType, surrogateKeyName, relatedEntityType));
            });
        }

        return result;
    }

    @Override
    public UUID newDurableKey(EntityType entityType, String naturalKeyValue) {
        return UUID.randomUUID();
    }

    @Override
    public IDaoFactory getDaoFactory() {
        return this.daoFactory;
    }

    @Override
    public void close() throws Exception {
        this.daoFactory.close();
    }

    /**
     * Returns list of steps corresponding to the give version of API, subscription and entity type.
     *
     * @param apiVersion   API version.
     * @param subscription subscription.
     * @param entityType   entity type.
     * @return list of steps.
     */
    public List<Step> steps(ApiVersion apiVersion, String subscription, EntityType entityType) {

        Value value = this.allSteps.get(Key.of(apiVersion, subscription, entityType));
        if (value != null) {
            return value.steps;
        }
        value = this.allSteps.get(Key.of(apiVersion, IKeyManager.ANY_SUBS, entityType));
        return value == null ? null : value.steps;
    }

    /**
     * Returns list of relations corresponding to the give version of API, subscription and entity type.
     *
     * @param apiVersion   API version.
     * @param subscription subscription.
     * @param entityType   entity type.
     * @return list of relations.
     */
    public List<Relation> relations(ApiVersion apiVersion, String subscription, EntityType entityType) {

        Value value = this.allSteps.get(Key.of(apiVersion, subscription, entityType));
        if (value != null) {
            return value.relations;
        }
        value = this.allSteps.get(Key.of(apiVersion, IKeyManager.ANY_SUBS, entityType));
        return value == null ? null : value.relations;
    }

    @Override
    public void refresh() {
        initAll(this.daoFactory);
    }

    /*
            Set of methods that must be implemented into a derived class.
         */
    protected abstract Map<DalRequestData, UUID> findByNaturalKey(List<DalRequestData> dalRequestData);

    protected abstract IMapper giveMapper(EntityType kind);

    protected abstract void registerDecision(Decision decision, EntityType kind, String naturalKeyName, Date date);


    /*
        Private methods.
     */
    private void initAll(IDaoFactory daoFactory) {
        try (
                IRuleService ruleService = service(daoFactory, Rule.class);
                IStepService stepService = service(daoFactory, Step.class);
                IRelationService relationService = service(daoFactory, Relation.class)) {

            this.subscriptions = ruleService
                    .readAll()
                    .stream()
                    .filter(r -> r.isActive())
                    .map(r -> r.getSubscription())
                    .collect(Collectors.toCollection(HashSet::new));


            this.allSteps = new HashMap<>();

            Set<String> all = new HashSet<>(this.subscriptions);
            all.add(IKeyManager.ANY_SUBS);
            for (String subscription : all) {
                for (EntityType entityType : EntityType.values()) {
                    Optional<Rule> optionalRule = loadRule(ruleService, subscription, entityType);
                    if (optionalRule.isPresent()) {
                        Rule rule = optionalRule.get();
                        List<Step> steps = stepService.readAll(rule.getId());
                        List<Step> orderedSteps = steps.stream()
                                .sorted(new Step.StepComparator())
                                .collect(Collectors.toCollection(ArrayList::new));

                        List<Relation> relations = relationService.readAll(rule.getId());
                        this.allSteps.put(Key.of(rule.getApiVersion(), subscription, entityType), Value.of(rule, orderedSteps, relations));
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Optional<Rule> loadRule(IReadWriteService<Rule> ruleService, String subscription, EntityType entityType) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(Rule.Fields.ACTIVE, true);
        filter.put(Rule.Fields.VERSION, this.currentVersion);
        filter.put(Rule.Fields.SUBSCRIPTION, subscription);
        filter.put(Rule.Fields.ENTITY, entityType);
        return ruleService.readFiltered(filter).stream().findFirst();
    }


    protected static class DalRequestData {
        private final String naturalKeyName;
        private final String naturalKey;
        private final EntityType dimEntityType;
        private final String surrogateKeyName;
        private final EntityType relatedEntityType;
        private UUID uuid; // This value is ignored for hashcode function.

        public DalRequestData(String naturalKeyName, String naturalKey, EntityType dimEntityType, String surrogateKeyName, EntityType relatedEntityType) {
            this.naturalKeyName = naturalKeyName;
            this.naturalKey = naturalKey;
            this.dimEntityType = dimEntityType;
            this.surrogateKeyName = surrogateKeyName;
            this.relatedEntityType = relatedEntityType;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getNaturalKey() {
            return naturalKey;
        }

        public String getNaturalKeyName() {
            return naturalKeyName;
        }

        public EntityType getDimEntityType() {
            return dimEntityType;
        }

        public String getSurrogateKeyName() {
            return surrogateKeyName;
        }

        public EntityType getRelatedEntityType() {
            return relatedEntityType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DalRequestData that = (DalRequestData) o;
            return Objects.equals(naturalKey, that.naturalKey)
                    && dimEntityType == that.dimEntityType
                    && Objects.equals(surrogateKeyName, that.surrogateKeyName)
                    && relatedEntityType == that.relatedEntityType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(naturalKey, dimEntityType, surrogateKeyName, relatedEntityType);
        }

        @Override
        public String toString() {
            return "DalRequestData{"
                    + "naturalKey='" + naturalKey + '\''
                    + ", dimEntityType=" + dimEntityType
                    + ", surrogateKeyName='" + surrogateKeyName + '\''
                    + ", relatedEntityType=" + relatedEntityType
                    + ", uuid=" + uuid
                    + '}';
        }
    }


    private static class Key {
        private ApiVersion apiVersion;
        private String subscription;
        private EntityType entityType;

        public static Key of(ApiVersion apiVersion, String subscription, EntityType entityType) {
            return new Key(apiVersion, subscription, entityType);
        }

        private Key(ApiVersion apiVersion, String subscription, EntityType entityType) {
            this.apiVersion = apiVersion;
            this.subscription = subscription;
            this.entityType = entityType;
        }

        @Override
        public String toString() {
            return "Key{"
                    + "apiVersion=" + apiVersion
                    + ", subscription='" + subscription + '\''
                    + ", entityType=" + entityType
                    + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key key = (Key) o;
            return Objects.equals(subscription, key.subscription)
                    && apiVersion == key.apiVersion
                    && entityType == key.entityType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(apiVersion, subscription, entityType);
        }
    }

    private static class Value {
        private List<Step> steps;
        private List<Relation> relations;
        private Rule rule;

        public static Value of(Rule rule, List<Step> steps, List<Relation> relations) {
            return new Value(rule, steps, relations);
        }

        private Value(Rule rule, List<Step> steps, List<Relation> relations) {
            this.steps = steps;
            this.relations = relations;
            this.rule = rule;
        }

        @Override
        public String toString() {
            return "Value{"
                    + "steps=" + steps
                    + ", relations=" + relations
                    + ", rule=" + rule
                    + '}';
        }
    }

}
