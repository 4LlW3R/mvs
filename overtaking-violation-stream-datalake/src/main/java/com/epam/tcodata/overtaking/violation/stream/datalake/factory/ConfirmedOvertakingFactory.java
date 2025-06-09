package com.epam.tcodata.overtaking.violation.stream.datalake.factory;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.overtaking.violation.stream.datalake.converter.ConfirmedOvertakingConverter;
import com.epam.tcodata.overtaking.violation.stream.datalake.converter.IConfirmedOvertakingConverter;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class ConfirmedOvertakingFactory implements IConfirmedOvertakingFactory {

    private static final long serialVersionUID = 7199130955937734672L;

    private ISecretStorageFactory secretStorageFactory = null;

    @Override
    public ISecretStorage createSecretStorage() throws Exception {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(getSecretStorageProperties());
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.CONFIRMED_OVERTAKING_VIOLATION, secretStorage);
    }

    @Override
    public IHive createRawHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.RAW, sparkSession);
    }

    @Override
    public IHive createPreparedHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.PREPARED, sparkSession);
    }

    @Override
    public IConfirmedOvertakingConverter createConfirmedOvertakingConverter() {
        return new ConfirmedOvertakingConverter();
    }


    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    protected Properties getSecretStorageProperties() {
        return new Properties();
    }

}
