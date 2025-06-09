package com.epam.tcodata.mock.main;

import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.mock.external.pump.factory.impl.*;
import com.epam.tcodata.mock.internal.pumps.factory.dimension.*;
import com.epam.tcodata.mock.internal.pumps.factory.fact.MockInternalEventFactory;
import com.epam.tcodata.mock.internal.pumps.factory.fact.MockInternalPositionFactory;
import com.epam.tcodata.mock.internal.pumps.factory.fact.MockInternalTripFactory;
import com.epam.tcodata.mock.raw.prepared.etl.factory.impl.*;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.raw.prepared.etl.factory.ISDMFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import org.apache.spark.sql.SparkSession;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Factories {

    static final List<EntityType> ENTITY_TYPES = Arrays.asList(EntityType.DRIVER, EntityType.POSITION, EntityType.ASSET,
            EntityType.LOCATION, EntityType.ORGANISATION_GROUP, EntityType.TRIP, EntityType.EVENT, EntityType.LIBRARY_EVENT);

    private SparkSession sparkSession = null;
    private ISecretStorage secretStorage = null;
    private IDaoFactory pumpFactory = null;
    private IDaoFactory mdmFactory =  null;
    private IDaoFactory speedLayerFactory = null;
    private IHive rawHive = null;
    private IHive preparedHive = null;

    private List<IInternalFactory> internalFactories = new ArrayList<>();
    private List<IExternalFactory> externalFactories = new ArrayList<>();
    private List<ISDMFactory> sdmFactories = new ArrayList<>();

    /**
     * Main  constructor.
     *
     * @param sparkSession
     * @param secretStorage
     */
    public Factories(SparkSession sparkSession, ISecretStorage secretStorage) {
        this.sparkSession = sparkSession;
        this.secretStorage = secretStorage;
    }

    /**
     * Initializes all factories with given parameters.
     *
     * @param parameters
     */
    public void setInitParameters(Map<String, String> parameters) {
        this.internalFactories.forEach(f -> f.setInitParameters(parameters));
        this.externalFactories.forEach(f -> f.setInitParameters(parameters));
        this.sdmFactories.forEach(f -> f.setInitParameters(parameters));
    }


    public List<IInternalFactory> getInternalFactories() {
        return internalFactories;
    }

    public List<IExternalFactory> getExternalFactories() {
        return externalFactories;
    }

    public List<ISDMFactory> getSdmFactories() {
        return sdmFactories;
    }

    public SparkSession getSparkSession() {
        return this.sparkSession;
    }

    public ISecretStorage getSecretStorage() {
        return this.secretStorage;
    }

    /**
     * Creates Factories object by given entity type.
     *
     * @param currentMoment
     * @param entityType    entity type
     * @return factories object
     * @throws Exception
     */
    public static Factories factoriesByEntityType(SparkSession sparkSession, ISecretStorage secretStorage, Instant currentMoment, EntityType entityType) throws Exception {
        Factories res = new Factories(sparkSession, secretStorage);
        res.addFactoriesByEntityType(currentMoment, entityType);
        return res;
    }

    /**
     * Factory method.
     *
     * @param sparkSession spark session
     * @param secretStorage secret storage
     * @param currentMoment current moment
     * @return instance of Factories
     * @throws Exception
     */
    public static Factories allFactories(SparkSession sparkSession, ISecretStorage secretStorage, Instant currentMoment) throws Exception {
        Factories res = new Factories(sparkSession, secretStorage);

        for (EntityType entityType : ENTITY_TYPES) {
            res.addFactoriesByEntityType(currentMoment, entityType);
        }

        return res;
    }

    private void addFactoriesByEntityType(Instant currentMoment, EntityType entityType) throws Exception {
        switch (entityType) {
            case DRIVER:
                addExternal(new MockExternalDriverFactory());
                addInternal(new MockInternalDriverFactory());
                addSdm(new MockDriverSDMFactory());
                break;

            case POSITION:
                addExternal(new MockExternalPositionFactory());
                addInternal(new MockInternalPositionFactory());
                addSdm(new MockPositionSDMFactory());
                break;

            case ASSET:
                addExternal(new MockExternalAssetFactory());
                addInternal(new MockInternalAssetFactory());
                addSdm(new MockAssetSDMFactory());
                break;

            case LOCATION:
                addExternal(new MockExternalLocationFactory());
                addInternal(new MockInternalLocationFactory());
                addSdm(new MockLocationSDMFactory());
                break;

            case ORGANISATION_GROUP:
                addExternal(new MockExternalOrganisationGroupFactory());
                addExternal(new MockExternalOrganisationSubGroupFactory());
                addInternal(new MockInternalOrganisationGroupFactory());
                addInternal(new MockInternalOrganisationSubGroupFactory());
                addSdm(new MockOrganisationGroupSDMFactory());
                break;

            case ORGANISATION_SUBGROUP:
                addExternal(new MockExternalOrganisationSubGroupFactory());
                addInternal(new MockInternalOrganisationSubGroupFactory());
                break;

            case TRIP:
                addExternal(new MockExternalTripFactory());
                addInternal(new MockInternalTripFactory());
                addSdm(new MockTripSDMFactory());
                addSdm(new MockSubTripSDMFactory());
                break;

            case EVENT:
                addExternal(new MockExternalEventFactory());
                addInternal(new MockInternalEventFactory());
                addSdm(new MockEventSDMFactory());
                break;

            case LIBRARY_EVENT:
                addExternal(new MockExternalLibraryEventFactory());
                addInternal(new MockInternalLibraryEventFactory());
                addSdm(new MockLibraryEventSDMFactory());
                break;

            default:
                throw new Exception("Unknown EntetyType " + entityType);
        }
        this.externalFactories.forEach(f -> f.setCurrentMoment(currentMoment));
    }

    private void addInternal(IInternalFactory factory) {
        boolean none = this.internalFactories.stream().noneMatch(f -> f.getClass() == factory.getClass());
        if (none) {
            this.internalFactories.add(factory);
        }
    }

    private void addExternal(IExternalFactory factory) {
        boolean none = this.externalFactories.stream().noneMatch(f -> f.getClass() == factory.getClass());
        if (none) {
            this.externalFactories.add(factory);
        }
    }

    private void addSdm(ISDMFactory factory) {
        boolean none = this.sdmFactories.stream().noneMatch(f -> f.getClass() == factory.getClass());
        if (none) {
            this.sdmFactories.add(factory);
        }
    }


    /**
     * Init all databases in the factories.
     *
     * @param pumpBackupDir folder with initial backup for PUMP database
     * @param mdmBackupDir folder with initial backup for MDM database
     * @param speedlayerBackupDir folder with initial backup for SPEED_LAYER database
     * @throws Exception
     */
    public void initDatabases(String pumpBackupDir, String mdmBackupDir, String speedlayerBackupDir) throws Exception {

        if (!this.externalFactories.isEmpty()) {
            IExternalFactory externalFactory = this.externalFactories.get(0);
            this.pumpFactory = externalFactory.createPumpDaoFactory(this.secretStorage);
            if (!pumpBackupDir.isEmpty()) {
                this.pumpFactory.restore(Paths.get(pumpBackupDir));
            }
        }

        if (!this.externalFactories.isEmpty()) {
            IExternalFactory externalFactory = this.externalFactories.get(0);
            this.mdmFactory = externalFactory.createKeyFactory().createKeyManager(KeyManagerVersion.VERSION_1_0, this.secretStorage).getDaoFactory();
            if (!mdmBackupDir.isEmpty()) {
                this.mdmFactory.restore(Paths.get(mdmBackupDir));
            }
        }

        if (!this.internalFactories.isEmpty()) {
            IInternalFactory iInternalFactory = this.internalFactories.get(0);
            this.speedLayerFactory = iInternalFactory.createSpeedLayerDaoFactory(this.secretStorage);
            if (!speedlayerBackupDir.isEmpty()) {
                this.speedLayerFactory.restore(Paths.get(speedlayerBackupDir));
            }
        }

        if (!this.sdmFactories.isEmpty()) {
            this.preparedHive = this.sdmFactories.get(0).createPreparedHive(this.sparkSession);
            this.rawHive = this.sdmFactories.get(0).createRawHive(this.sparkSession);
        }
    }

    /**
     * Collect all data into data holder for further comparing.
     *
     * @return
     */
    public DataHolder collectDataInto() {

        DataHolder dataHolder = new DataHolder();

        dataHolder.collectAllReadWriteTables(this.mdmFactory, DatabaseConfig.MDM);
        dataHolder.collectAllReadWriteTables(this.pumpFactory, DatabaseConfig.PUMPS);
        dataHolder.collectAllReadWriteTables(this.speedLayerFactory, DatabaseConfig.SPEEDLAYER);

        dataHolder.collectAllData(this.rawHive);
        dataHolder.collectAllData(this.preparedHive);

        this.mdmFactory.backup(Paths.get("temp/mdm"));
        dataHolder.backupHiveData(Paths.get("temp/hive"));

        return dataHolder;
    }
}

