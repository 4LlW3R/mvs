package com.epam.tcodata.hive.dal;

import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.HiveRepository;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import org.apache.spark.sql.SparkSession;

public class Hive extends AbstractHive {

    private static final long serialVersionUID = -6118586117254211261L;

    /**
     * Public main constructor.
     *
     * @param hiveConfig
     * @param sparkSession
     */
    public Hive(HiveConfig hiveConfig, SparkSession sparkSession) {
        super(hiveConfig, sparkSession);

    }

    @Override
    protected IHiveRepository createRepository(IHiveEntityType hiveEntityType) {
        return new HiveRepository(hiveEntityType, this);
    }
}
