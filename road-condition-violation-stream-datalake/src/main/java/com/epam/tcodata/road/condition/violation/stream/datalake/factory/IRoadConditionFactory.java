package com.epam.tcodata.road.condition.violation.stream.datalake.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.road.condition.violation.stream.datalake.converter.IRoadConditionConverter;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public interface IRoadConditionFactory extends Serializable {

    ISecretStorage createSecretStorage();

    IEventHub createEventHub(ISecretStorage secretStorage);

    IHive createRawHive(SparkSession sparkSession);

    IHive createPreparedHive(SparkSession sparkSession);

    IRoadConditionConverter createRoadConditionConverter();

    default IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new PumpsDaoFactory(secretStorage);
    }
}

