package com.epam.tcodata.raw.prepared.etl.factory;

import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractSDMFactory<T extends RawEntity, U extends PreparedEntity>
        implements ISDMFactory<T, U> {

    private Class<T> rawEntityClass;
    private Class<U> preparedEntityClass;
    private ISecretStorageFactory secretStorageFactory = null;
    protected Map<String, String> parameters = new HashMap<>();

    protected AbstractSDMFactory(Class<T> rawEntityClass, Class<U> preparedEntityClass) {
        this.rawEntityClass = rawEntityClass;
        this.preparedEntityClass = preparedEntityClass;
    }

    @Override
    public void setInitParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public Class<T> getRawEntityClass() {
        return this.rawEntityClass;
    }

    @Override
    public Class<U> getPreparedEntityClass() {
        return this.preparedEntityClass;
    }

    @Override
    public ISecretStorage createSecretStorage() throws Exception {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(new Properties());
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new PumpsDaoFactory(secretStorage);
    }

    @Override
    public IHive createRawHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.RAW, sparkSession);
    }

    @Override
    public IHive createPreparedHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.PREPARED, sparkSession);
    }

    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
