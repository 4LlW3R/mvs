package com.epam.tcodata.event.validator.factory.impl;

import com.epam.tcodata.event.validator.converter.EventConverter;
import com.epam.tcodata.event.validator.converter.IEventConverter;
import com.epam.tcodata.event.validator.factory.IEventValidatorFactory;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class EventValidatorFactory implements IEventValidatorFactory {

    private ISecretStorageFactory secretStorageFactory = null;

    @Override
    public ISecretStorage createSecretStorage() throws Exception {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(getScretStorageProperties());
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new PumpsDaoFactory(secretStorage);
    }

    @Override
    public IHive createPreparedHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.PREPARED, sparkSession);
    }

    @Override
    public IEventConverter createConverter() {
        return new EventConverter();
    }

    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    protected Properties getScretStorageProperties() {
        return new Properties();
    }


}
