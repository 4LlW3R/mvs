package com.epam.tcodata.event.validator.factory;

import com.epam.tcodata.event.validator.converter.IEventConverter;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public interface IEventValidatorFactory extends Serializable {

    ISecretStorage createSecretStorage() throws Exception;

    IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception;

    IHive createPreparedHive(SparkSession sparkSession);

    IEventConverter createConverter();
}
