package com.epam.tcodata.mock.raw.prepared.etl;

import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;

public class DriverTest {

    @Test
    public void testDriver() throws Exception {

//        PositionSDMFactory factory = new MockPositionSDMFactory();
//
//        String appName = "DriverTest_" + factory.getRawEntityClass().getSimpleName();
//        SparkSession sparkSession = SparkSession.builder()
//                .config(new SparkConf()
//                        .setMaster("local[*]")
//                        .setAppName(appName))
//                .getOrCreate();
//
//        IHive rawHive = factory.createRawHive(sparkSession);
//        IHiveEntityType rawHiveEntityType = rawHive.databaseConfig().entityTypeByEntity(RawPosition.class, false);
//        IHiveRepository rawRepository = rawHive.repository(rawHiveEntityType);
//
//        IHive preparedHive = factory.createPreparedHive(sparkSession);
//        IHiveEntityType preparedHiveEntityType = preparedHive.databaseConfig().entityTypeByEntity(PreparedPosition.class, false);
//        IHiveRepository preparedRepository = preparedHive.repository(preparedHiveEntityType);
//
//        IDaoFactory pumpDaoFactory = factory.createPumpDaoFactory();
//        IHiveOffsetService service = IDaoFactory.service(pumpDaoFactory, HiveOffset.class);
//
//        Instant from = Instant.now();
//        RawPosition pos1 = createRawPosition(1, 100, 200);
//        RawPosition pos2 = createRawPosition(2, 101, 201);
//        List<RawPosition> rawList = Arrays.asList(pos1, pos2);
//        Encoder<RawPosition> bean = Encoders.bean(RawPosition.class);
//        JavaRDD<RawPosition> enrichedJavaRDD = new JavaSparkContext(sparkSession.sparkContext()).parallelize(rawList);
//        Dataset<RawPosition> dataset1 = sparkSession.createDataset(enrichedJavaRDD.rdd(), bean);
//        Instant to = Instant.now();
//
//        rawRepository.write(dataset1, SaveMode.Append);
//
//        HiveOffset hiveOffset = new HiveOffset();
//        hiveOffset.setTimeFrom(from);
//        hiveOffset.setTimeTo(to);
//        hiveOffset.setIngestedDateUtc(Instant.now());
//        hiveOffset.setEntityType(factory.getEntityType().getCode());
//        service.insert(Arrays.asList(hiveOffset));
//
//        // launch driver
//        Driver.proceedAllWork(factory, sparkSession);
//
//        Dataset<RawPosition> preparedDataset = preparedRepository.read();
//        List<RawPosition> preparedList = preparedDataset.collectAsList();
//
//        assertEquals(rawList.size(), preparedList.size());
    }

    private RawPosition createRawPosition(long id, long driverId, long assetId) {
        RawPosition res = new RawPosition();
        res.setPositionId(id);
        res.setDriverId(driverId);
        res.setAssetId(assetId);
        res.setIngestedDateUtc(Timestamp.from(Instant.now()));
        return res;
    }
}