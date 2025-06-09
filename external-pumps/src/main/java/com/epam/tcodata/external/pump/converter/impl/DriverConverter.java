package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.time.Instant;

import static com.epam.tcodata.external.pump.util.ConverterUtil.timestampToDateTime;

public class DriverConverter implements IConverter<Driver, EnrichedDriver, AvroDriver> {

    private static final long serialVersionUID = -8547918794422976828L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedDriver convertToEnriched(Driver driver, AbstractDto dto, IKeyManager keyManager) {
        EnrichedDriver enrichedDriver = new EnrichedDriver(driver);

        Decision decision = keyManager.findOrCreate(driver, ApiVersion.API_2_0, String.valueOf(((DimensionDto) dto).getOrgGroupId()), EntityType.DRIVER);

        String surrogateKeyStr = decision == null ? null : decision.getSurrogateKey().toString();

        enrichedDriver

                //enriched with additional info
                .setDurableId(surrogateKeyStr) //TO DO
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((DimensionDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedDriver;
    }

    @Override
    public AvroDriver convertToAvro(EnrichedDriver enrichedDriver) {
        return AvroDriver.newBuilder()

                // enriched fields
                .setDurableId(enrichedDriver.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedDriver.getIngestedDateUtc()))
                .setSubscriptionId(enrichedDriver.getSubscriptionId())
                .setLineageCode(enrichedDriver.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setSiteId(enrichedDriver.getSiteId())
                .setDriverId(enrichedDriver.getDriverId())
                .setName(enrichedDriver.getName())
                .setImageUri(enrichedDriver.getImageUri())
                .setFmDriverId(enrichedDriver.getFmDriverId())
                .setEmployeeNumber(enrichedDriver.getEmployeeNumber())
                .setSystemDriver(enrichedDriver.getSystemDriver())
                .setMobileNumber(enrichedDriver.getMobileNumber())
                .setEmail(enrichedDriver.getEmail())
                .setExtendedDriverId(enrichedDriver.getExtendedDriverId())
                .setExtendedDriverIdType(enrichedDriver.getExtendedDriverIdType())
                .setCountry(enrichedDriver.getCountry())
                .build();
    }

    @SuppressWarnings("CPD-START")
    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedDriver> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedDriver, Row>) enrichedDriver ->
                RowFactory.create(enrichedDriver.getOrderedValues()));
    }

    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("site_id", DataTypes.LongType, true),
                        DataTypes.createStructField("driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("name", DataTypes.StringType, true),
                        DataTypes.createStructField("image_uri", DataTypes.StringType, true),
                        DataTypes.createStructField("fm_driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("employee_number", DataTypes.StringType, true),
                        DataTypes.createStructField("system_driver", DataTypes.BooleanType, true),
                        DataTypes.createStructField("mobile_number", DataTypes.StringType, true),
                        DataTypes.createStructField("email", DataTypes.StringType, true),
                        DataTypes.createStructField("extended_driver_id", DataTypes.StringType, true),
                        DataTypes.createStructField("extended_driver_id_type", DataTypes.StringType, true),
                        DataTypes.createStructField("country", DataTypes.StringType, true)
                }
        );
    }
}
