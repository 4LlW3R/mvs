package com.epam.tcodata.internal.pump.converter.dimension;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class DriverConverter extends AbstractEntityConverter<AvroDriver, EnrichedDriver, RawDriver> {

    private static final long serialVersionUID = 2518557458476184753L;

    /**
     * Main public constructor.
     */
    public DriverConverter() {
        /***  Default implementation ***/
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedDriver
     */
    @Override
    public EnrichedDriver convertToEnriched(AvroDriver avro) {
        Driver.DriverBuilder driverBuilder = new Driver.DriverBuilder()
                .setSiteId(avro.getSiteId())
                .setDriverId(avro.getDriverId())
                .setName(checkedToString(avro.getName()))
                .setImageUri(checkedToString(avro.getImageUri()))
                .setFmDriverId(avro.getFmDriverId())
                .setEmployeeNumber(checkedToString(avro.getEmployeeNumber()))
                .setSystemDriver(avro.getSystemDriver())
                .setMobileNumber(checkedToString(avro.getMobileNumber()))
                .setEmail(checkedToString(avro.getEmail()))
                .setExtendedDriverId(checkedToString(avro.getExtendedDriverId()))
                .setExtendedDriverIdType(checkedToString(avro.getExtendedDriverIdType()))
                .setCountry(checkedToString(avro.getCountry()));

        Driver driver = driverBuilder.build();

        return new EnrichedDriver(driver)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeDriver
     */

    @Override
    public RawDriver convertToRaw(EnrichedDriver enriched, Timestamp persistedDate) {
        RawDriver dataLakeDriver = new RawDriver();
        // DataLakeEntity fields
        dataLakeDriver.setDurableId(enriched.getDurableId());
        dataLakeDriver.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeDriver.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeDriver.setLineageCode(enriched.getLineageCode());
        dataLakeDriver.setPersistedDateUtc(persistedDate);
        // DataLakeDriver fields
        dataLakeDriver.setSiteId(enriched.getSiteId());
        dataLakeDriver.setDriverId(enriched.getDriverId());
        dataLakeDriver.setName(enriched.getName());
        dataLakeDriver.setImageUri(enriched.getImageUri());
        dataLakeDriver.setFmDriverId(enriched.getFmDriverId());
        dataLakeDriver.setEmployeeNumber(enriched.getEmployeeNumber());
        dataLakeDriver.setSystemDriver(enriched.getSystemDriver());
        dataLakeDriver.setMobileNumber(enriched.getMobileNumber());
        dataLakeDriver.setEmail(enriched.getEmail());
        dataLakeDriver.setExtendedDriverId(enriched.getExtendedDriverId());
        dataLakeDriver.setExtendedDriverIdType(enriched.getExtendedDriverIdType());
        dataLakeDriver.setCountry(enriched.getCountry());
        return dataLakeDriver;
    }

    @Override
    public StructType getSchemaForWrite() {
        return new StructType(
                new StructField[]{
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("persisted_date_utc", DataTypes.TimestampType, true),
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
