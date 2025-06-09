package com.epam.tcodata.mock.main;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.domain.prepared.PreparedAreaEntityType;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.domain.pumps.EventHubOffset;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import com.epam.tcodata.sql.dal.exception.BackupException;
import com.epam.tcodata.sql.dal.service.IReadWriteService;
import com.epam.tcodata.sql.dal.service.IService;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHolder.class);

    private Map<DatabaseConfig, Map<Class<?>, List>> sqlStorage = new HashMap<>();
    private Map<IHiveEntityType, List<Row>> hiveStorage = new HashMap<>();

    private String[] excludingHiveFields = new String[]{"id", "persistedDateUtc", "year", "weekNumber"};
    private String[] excludingKeyMapFields = new String[]{"id"};

    public DataHolder() {
    }

    /**
     * Main public constructor.
     *
     * @param fileName config file name.
     * @throws Exception
     */
    public DataHolder(String fileName) throws Exception {
        PipelineConfigiration configuration = ResourceUtils.loadObject(PipelineConfigiration.class, fileName);

        loadSqlData(DatabaseConfig.MDM, KeyMapping.class, configuration.getSqlMdmKeyMapping());
        loadSqlData(DatabaseConfig.PUMPS, MixOffset.class, configuration.getSqlPumpMixOffset());
        loadSqlData(DatabaseConfig.PUMPS, EventHubOffset.class, configuration.getSqlPumpEventHubOffset());
        loadSqlData(DatabaseConfig.PUMPS, Signal.class, configuration.getSqlPumpSignal());
        loadSqlData(DatabaseConfig.SPEEDLAYER, SpeedLayerPosition.class, configuration.getSqlSpeedLayerSpeedLayerPosition());
        loadSqlData(DatabaseConfig.SPEEDLAYER, SpeedLayerEvent.class, configuration.getSqlSpeedLayerSpeedLayerEvent());

        loadHiveData(RawAreaEntityType.POSITION, configuration.getHiveRawPosition());
        loadHiveData(RawAreaEntityType.EVENT, configuration.getHiveRawEvent());
        loadHiveData(RawAreaEntityType.TRIP, configuration.getHiveRawTrip());
        loadHiveData(RawAreaEntityType.SUBTRIP, configuration.getHiveRawSubtrip());
        loadHiveData(RawAreaEntityType.DRIVER, configuration.getHiveRawDriver());
        loadHiveData(RawAreaEntityType.ASSET, configuration.getHiveRawAsset());
        loadHiveData(RawAreaEntityType.LIBRARY_EVENT, configuration.getHiveRawLibraryEvent());
        loadHiveData(RawAreaEntityType.LOCATION, configuration.getHiveRawLocation());
        loadHiveData(RawAreaEntityType.ORGANISATION_GROUP, configuration.getHiveRawOrganisationGroup());
        loadHiveData(RawAreaEntityType.ORGANISATION_SUBGROUP, configuration.getHiveRawOrganisationSubGroup());

        loadHiveData(PreparedAreaEntityType.POSITION, configuration.getHivePreparedPosition());
        loadHiveData(PreparedAreaEntityType.EVENT, configuration.getHivePreparedEvent());
        loadHiveData(PreparedAreaEntityType.TRIP, configuration.getHivePreparedTrip());
        loadHiveData(PreparedAreaEntityType.SUBTRIP, configuration.getHivePreparedSubtrip());
        loadHiveData(PreparedAreaEntityType.DRIVER, configuration.getHivePreparedDriver());
        loadHiveData(PreparedAreaEntityType.VEHICLE, configuration.getHivePrepareVehicle());
        loadHiveData(PreparedAreaEntityType.LOCATION, configuration.getHivePreparedLocation());
        loadHiveData(PreparedAreaEntityType.GROUP, configuration.getHivePreparedGroup());
    }

    public List<?> getSqlTable(DatabaseConfig databaseConfig, Class<?> clazz) {
        Map<Class<?>, List> dataBase = this.sqlStorage.computeIfAbsent(databaseConfig, config -> new HashMap<>());
        return dataBase.computeIfAbsent(clazz, type -> new ArrayList());
    }

    public List<Row> getHiveTable(IHiveEntityType entityType) {
        return this.hiveStorage.computeIfAbsent(entityType, type -> new ArrayList<>());
    }

    public void putSqlData(DatabaseConfig databaseConfig, Class<?> clazz, List list) {
        Map<Class<?>, List> dataBase = this.sqlStorage.computeIfAbsent(databaseConfig, config -> new HashMap<>());
        dataBase.put(clazz, list);
    }

    public void putHiveData(IHiveEntityType hiveEntityType, List list) {
        this.hiveStorage.put(hiveEntityType, list);
    }

    /**
     * Performs storing hive data as json files for debug purpose.
     *
     * @param path directory to store files
     */
    public void backupHiveData(Path path) {
        try {
            Files.createDirectories(path);

            for (Map.Entry<IHiveEntityType, List<Row>> entry : this.hiveStorage.entrySet()) {
                IHiveEntityType name = entry.getKey();
                List<Row> list = entry.getValue();
                if (!list.isEmpty()) {
                    Path jsonPath = Paths.get(path.toString(), name.getEntityClazz().getSimpleName() + ".json");
                    ResourceUtils.saveData(Row.class, list, jsonPath.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error("backup({})", path);
            throw new BackupException(e.getMessage(), e);
        }

    }

    /**
     * Compares two DataHolder objects, printing the difference to the given stream.
     *
     * @param actualData data with this object must be compared.
     * @return
     */
    public boolean compareWithActual(DataHolder actualData) {

        boolean sqlResult = true;
        for (Map.Entry<DatabaseConfig, Map<Class<?>, List>> entry : this.sqlStorage.entrySet()) {
            DatabaseConfig databaseConfig = entry.getKey();
            Map<Class<?>, List> database = entry.getValue();
            boolean res = database.entrySet().stream().allMatch(en -> {
                Class<?> entryClazz = en.getKey();
                List expectedTable = en.getValue();

                List<?> actualTable = actualData.getSqlTable(databaseConfig, entryClazz);
                return compareTables(expectedTable, actualTable, "Sql", entryClazz.getName(), excludingKeyMapFields);
            });
            sqlResult = sqlResult && res;
        }

        boolean hiveResult = true;
        for (Map.Entry<IHiveEntityType, List<Row>> entry : this.hiveStorage.entrySet()) {
            IHiveEntityType name = entry.getKey();
            List<Row> expectedTable = entry.getValue();

            List<?> actualTable = actualData.getHiveTable(name);
            boolean res = compareTables(expectedTable, actualTable, "Hive", name.config().name() + " " + name.toString(), excludingHiveFields);
            hiveResult = hiveResult && res;
        }

        return sqlResult && hiveResult;
    }

    /**
     * Collect data from all tables of the given dao factory that have permissions to read/write.
     *
     * @param daoFactory dao factory.
     * @param databaseConfig data base config.
     */
    public void collectAllReadWriteTables(IDaoFactory daoFactory, DatabaseConfig databaseConfig) {

        Set<Class<?>> classes = daoFactory.knownEntityClasses();
        classes.forEach(clazz -> {
            IService service = IDaoFactory.service(daoFactory, clazz);
            if (service instanceof IReadWriteService) {
                IReadWriteService readWriteService = (IReadWriteService) service;
                List list = readWriteService.readAll();
                putSqlData(databaseConfig, clazz, list);
            }
        });
    }

    /**
     * Collect data from hive tables.
     *
     * @param hive hive database.
     */
    public void collectAllData(IHive hive) {
        hive.entityTypes().forEach(hiveEntityType -> {
            IHiveRepository repository = hive.repository(hiveEntityType);
            Dataset<Row> dataset = repository.read();
            List<Row> table = dataset.collectAsList();
            List<? extends IDataLakeEntity> entities = HiveCommon.rowListToEntityList(table, hiveEntityType.getEntityClazz());
            putHiveData(hiveEntityType, entities);
        });
    }


    public boolean compareActualExpected(DataHolder actualData) {

        return compareWithActual(actualData);
    }


    private void loadHiveData(IHiveEntityType entityType, String fileName) throws IOException {

        if (fileName != null && !fileName.isEmpty() && entityType != null) {
            List<?> list = ResourceUtils.loadData(entityType.getEntityClazz(), fileName);
            LOGGER.info("###LIST-DATA###" + list);
            putHiveData(entityType, list);
        }
    }

    private void loadSqlData(DatabaseConfig databaseConfig, Class<?> clazz, String fileName) throws IOException {

        if (fileName != null && !fileName.isEmpty()) {
            List<?> list = ResourceUtils.loadData(clazz, fileName);
            putSqlData(databaseConfig, clazz, list);
        }
    }


    /**
     * Compares two tables as lists of entities. Difference between them will be printed onto given stream.
     *
     * @param expectedTable expected table.
     * @param actualTable actual table.
     * @param prefix comment before comparing.
     * @param name name of the entities.
     * @param excluding collection of fields that will be ignored during compare.
     * @return
     */
    public static boolean compareTables(List expectedTable, List actualTable, String prefix, String name, String... excluding) {
        if (expectedTable != null && actualTable != null) {
            List expectedExceed = new ArrayList();
            List actualExceed = new ArrayList();
            LOGGER.info("comparing " + prefix + " table " + name);
            if (!separate(expectedTable, actualTable, expectedExceed, actualExceed, excluding)) {
                LOGGER.info("expected size: " + expectedTable.size() + " element type: " + (expectedTable.size() > 0 ? expectedTable.get(0).getClass() : null));
                LOGGER.info("actual   size: " + actualTable.size() + " element type: " + (actualTable.size() > 0 ? actualTable.get(0).getClass() : null));

                LOGGER.info("===== " + prefix + " tables for " + name + " are mismatched =====");
                LOGGER.info("      Rows are presented into expected, but absent into actual:");
                expectedExceed.stream().forEach(e -> LOGGER.info("" + e));
                LOGGER.info("      Rows are presented into actual, but absent into expected:");
                actualExceed.stream().forEach(e -> LOGGER.info("" + e));
                LOGGER.info("=====================================================");

                return false;
            }
        }
        LOGGER.info("... comparing " + prefix + " table " + name + " succeed.");
        return true;

    }

    private static boolean separate(Collection col1, Collection col2, List dif1, List dif2, String... excluding) {

        dif2.addAll(col2);

        for (Object obj : col1) {
            int index = find(dif2, obj, excluding);
            if (index >= 0) { // found
                dif2.remove(index);
            } else { // not found
                dif1.add(obj);
            }
        }

        return dif1.isEmpty() && dif2.isEmpty();
    }

    private static int find(List list, Object obj, String... excluding) {

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (EqualsBuilder.reflectionEquals(obj, list.get(i), excluding)) {
                    return i;
                }
            }
        }

        return -1;
    }

}
