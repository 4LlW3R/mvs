package com.epam.tcodata.hive.dal.repository;

public class HiveRepositoryPositionTest {
/*
    private static final String FORMAT = "orc";
    private static final String URL = "temp/";

    private Encoder<RawPosition> encoder;
    private StructType entitySchema;
//    private StructType rowSchema;
    private IHiveEntityType hiveEntityType = RawAreaEntityType.POSITION;
    private List<RawPosition> list;
    private UUID uuid1 = UUID.randomUUID();
    private UUID uuid2 = UUID.randomUUID();

    private static SparkSession sparkSession;

//    @BeforeClass
//    public static void setUpClass() throws Exception {
//        sparkSession = SparkSession.builder()
//                .config(new SparkConf()
//                        .setAppName("app-name")
//                        .setMaster("local[3]"))
//                .getOrCreate();
//    }
//
//    @Before
//    public void setUp() {
//        this.entitySchema =  HiveCommon.getEntitySchema(hiveEntityType.getEntityClazz());
////        this.rowSchema =  HiveCommon.getRowSchema(hiveEntityType.getEntityClazz());
//        this.encoder = Encoders.bean((Class<RawPosition>) hiveEntityType.getEntityClazz());
//        RawPosition driver1 = createPosition(100L, uuid1.toString(), "Robb Stark", 2019, 1);
//        RawPosition driver2 = createPosition(101L, uuid2.toString(), "Jon Snow", 2019, 2);
//        this.list = Arrays.asList(driver1, driver2);
//    }
//
//    @Test
//    public void readWrite() {
//
//        JavaRDD<RawPosition> enrichedJavaRDD = new JavaSparkContext(sparkSession.sparkContext()).parallelize(list);
//        Encoder<RawPosition> bean = Encoders.bean(RawPosition.class);
//        Dataset<RawPosition> dataset = sparkSession.createDataset(enrichedJavaRDD.rdd(), bean);
//        dataset.printSchema();
//
//        JavaRDD<RawPosition> rawPositionJavaRDD = dataset.javaRDD();
//        JavaRDD<Row> rowJavaRDD = rawPositionJavaRDD.map((Function<RawPosition, Row>) factDetectedEvent -> {
//            Row row = RowFactory.create(factDetectedEvent.orderedValues());
//            System.out.println("### " + row);
//            return row;
//        });
//        Dataset<Row> dataFrame = sparkSession.createDataFrame(rowJavaRDD, this.entitySchema);
//
//        dataFrame.printSchema();
//
//        dataFrame.write()
//                .format("hive")
//                .mode(SaveMode.Append)
//                .insertInto(hiveEntityType.tableName());
//
//
////        dataset.write()
////                .format(FORMAT)
////                .mode(SaveMode.Overwrite)
////                .save(URL + hiveEntityType.tableName());
////
////        Dataset<RawPosition> as = sparkSession.read()
////                .orc(URL + hiveEntityType.tableName())
////                .as(encoder);
////
////        List<RawPosition> rawDrivers = as.collectAsList();
////
////        assertEquals(2, rawDrivers.size());
////        assertEquals(uuid1.toString(), rawDrivers.get(0).getDurableId());
////        assertEquals(uuid2.toString(), rawDrivers.get(1).getDurableId());
//    }
//
////    @Test
////    public void readWriteRows() {
////
////        ExpressionEncoder<Row> enc = RowEncoder.apply(rowSchema);
////        List<Row> rows = HiveCommon.convertToRowList(this.list, entitySchema);
////        Dataset<Row> dataset = sparkSession.createDataset(rows, enc);
////
////        System.out.println(">> rowschema = " + this.rowSchema);
////        System.out.println(">> entityschema = " + this.entitySchema);
////
////        Dataset<RawPosition> rawDriverDataset = HiveCommon.convertToEntityDataset(dataset, this.hiveEntityType, this.encoder);
////        rawDriverDataset.write()
////                .format(FORMAT)
////                .mode(SaveMode.Overwrite)
////                .partitionBy("year", "weekNumber")
////                .save(URL + hiveEntityType.tableName());
////
////        Column where = new Column(RawPosition.Fields.SOURCE).$eq$eq$eq("Robb Stark");
////        Dataset<RawPosition> as = sparkSession.read()
////                .orc(URL + hiveEntityType.tableName())
////                .as(encoder)
////                .where(where);
////
////        List<RawPosition> rawDrivers = as.collectAsList();
////
////        assertEquals(1, rawDrivers.size());
////        assertEquals(uuid1.toString(), rawDrivers.get(0).getDurableId());
////    }

    private static RawPosition createPosition(long driverId, String durableId, String source, int year, int weekNumber) {
        RawPosition position = new RawPosition();
        position.setDurableId(durableId);
        position.setIngestedDateUtc(null);
        position.setSource(source);
        position.setPersistedDateUtc(Timestamp.from(Instant.now()));
        position.setDriverId(driverId);
        position.setLineageCode(100);
        position.setSubscriptionId(567L);
        position.setYear(year);
        position.setWeekNumber(weekNumber);
        return position;
    }
*/
}