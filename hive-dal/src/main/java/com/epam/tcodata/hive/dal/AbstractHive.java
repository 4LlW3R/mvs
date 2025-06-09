package com.epam.tcodata.hive.dal;

import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractHive implements IHive {

    private ConcurrentHashMap<IHiveEntityType, IHiveRepository> repositories = new ConcurrentHashMap<>();
    private HiveConfig hiveConfig;
    private SparkSession sparkSession;

    /**
     * Public main constructor.
     *
     * @param hiveConfig
     * @param sparkSession
     */
    protected AbstractHive(HiveConfig hiveConfig, SparkSession sparkSession) {

        this.hiveConfig = hiveConfig;
        this.sparkSession = sparkSession;

        hiveConfig.entityTypes()
                .forEach(entityType -> this.repositories.put(entityType, createRepository(entityType)));
    }

    @Override
    public String getDatabaseName() {
        return this.hiveConfig.database();
    }

    @Override
    public HiveConfig databaseConfig() {
        return this.hiveConfig;
    }

    @Override
    public Set<IHiveEntityType> entityTypes() {
        return hiveConfig.entityTypes();
    }

    @Override
    public Set<String> getTableNames() {
        return this.hiveConfig.entityTypes().stream().map(IHiveEntityType::tableName).collect(Collectors.toSet());
    }

    @Override
    public SparkSession getSparkSession() {
        return this.sparkSession;
    }

    @Override
    public IHiveRepository repository(IHiveEntityType hiveEntityType) {
        return this.repositories.get(hiveEntityType);
    }

    protected abstract IHiveRepository createRepository(IHiveEntityType entityType);

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
