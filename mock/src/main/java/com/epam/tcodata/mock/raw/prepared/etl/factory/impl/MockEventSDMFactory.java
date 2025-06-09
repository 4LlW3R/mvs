package com.epam.tcodata.mock.raw.prepared.etl.factory.impl;

import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.mock.hive.dal.MockHive;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.mock.sql.dal.impl.pumps.MockPumpsDaoFactory;
import com.epam.tcodata.raw.prepared.etl.factory.impl.EventSDMFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.spark.sql.SparkSession;

public class MockEventSDMFactory extends EventSDMFactory {

    private static final long serialVersionUID = 2391258701424964361L;

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new MockPumpsDaoFactory(secretStorage, super.parameters);
    }

    @Override
    public IHive createRawHive(SparkSession sparkSession) {
        return MockHive.instance(HiveConfig.RAW, sparkSession);
    }

    @Override
    public IHive createPreparedHive(SparkSession sparkSession) {
        return MockHive.instance(HiveConfig.PREPARED, sparkSession);
    }

    @Override
    protected ISecretStorageFactory createSecretStorageFactory() {
        return new MockSecretStorageFactory(super.parameters);
    }
}
