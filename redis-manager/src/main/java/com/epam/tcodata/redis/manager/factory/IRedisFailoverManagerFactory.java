package com.epam.tcodata.redis.manager.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.Map;

public interface IRedisFailoverManagerFactory extends Serializable {

    /**
     * Set extra parameters, that this factory may use for its own purposes.
     *
     * @param parameters
     */
    default void setInitParameters(Map<String, String> parameters) {
    }

    IHive createRawHive(SparkSession sparkSession);

    ISecretStorage createSecretStorage();

    IEventHub createdDriverEventHub(ISecretStorage secretStorage);

    IEventHub createdAssetEventHub(ISecretStorage secretStorage);

    IRedis createDriverRedis(ISecretStorage secretStorage);

    IRedis createVehicleRedis(ISecretStorage secretStorage);
}


