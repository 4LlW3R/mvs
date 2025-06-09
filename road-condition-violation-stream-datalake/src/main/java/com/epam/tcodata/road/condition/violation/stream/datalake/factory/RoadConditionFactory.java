package com.epam.tcodata.road.condition.violation.stream.datalake.factory;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.road.condition.violation.stream.datalake.converter.IRoadConditionConverter;
import com.epam.tcodata.road.condition.violation.stream.datalake.converter.RoadConditionConverter;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class RoadConditionFactory implements IRoadConditionFactory {

    private static final long serialVersionUID = 4689100008833017775L;

    private ISecretStorageFactory secretStorageFactory = null;

    @Override
    public ISecretStorage createSecretStorage() {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(getSecretStorageProperties());
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.ROAD_CONDITION_VIOLATION, secretStorage);
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
    public IRoadConditionConverter createRoadConditionConverter() {
        return new RoadConditionConverter();
    }
    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    protected Properties getSecretStorageProperties() {
        return new Properties();
    }

}
