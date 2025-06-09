package com.epam.tcodata.mock.hive.dal;

import com.epam.tcodata.hive.dal.AbstractHive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.mock.hive.dal.repository.MockHiveRepository;
import org.apache.spark.sql.SparkSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockHive extends AbstractHive {

    private static final long serialVersionUID = 6483987397711124176L;

    private static volatile Map<HiveConfig, MockHive> singltons = new ConcurrentHashMap<>();

    /**
     * Public main constructor.
     *
     * @param hiveConfig
     * @param sparkSession
     */
    public MockHive(HiveConfig hiveConfig, SparkSession sparkSession) {
        super(hiveConfig, sparkSession);

    }

    /**
     * Singleton entry point.
     *
     * @return instance of MockHive.
     */
    public static MockHive instance(HiveConfig hiveConfig, SparkSession sparkSession) {
        return singltons.computeIfAbsent(hiveConfig, config -> new MockHive(config, sparkSession));
    }

    @Override
    protected IHiveRepository createRepository(IHiveEntityType entityType) {
        return new MockHiveRepository<>(entityType, this);
    }


}
