package com.epam.tcodata.redis.manager.converter;

import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;

import java.io.Serializable;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@Deprecated
public class DriverConverter implements Serializable {

    private static final long serialVersionUID = 2518557458476184753L;

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedDriver
     */
    public static EnrichedDriver convertToEnriched(AvroDriver avro) {
        Driver.DriverBuilder driverBuilder = new Driver.DriverBuilder()
                .setName(checkedToString(avro.getName()))
                .setEmployeeNumber(checkedToString(avro.getEmployeeNumber()));

        Driver driver = driverBuilder.build();

        return new EnrichedDriver(driver)
                .setDurableId(checkedToString(avro.getDurableId()));
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeDriver
     */
    public static RawDriver convertToDataLake(EnrichedDriver enriched) {
        RawDriver dataLakeDriver = new RawDriver();
        dataLakeDriver.setDurableId(enriched.getDurableId());
        dataLakeDriver.setName(enriched.getName());
        dataLakeDriver.setEmployeeNumber(enriched.getEmployeeNumber());
        return dataLakeDriver;
    }
}
