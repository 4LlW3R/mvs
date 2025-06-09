package com.epam.tcodata.sql.dal.util;

import com.epam.tcodata.models.DataPumpResultCode;
import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.service.pumps.IMixOffsetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

public class PumpDbUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumpDbUtils.class);

    private static final String RANDOM_DATE = "1900-01-01T00:30:00Z";
    private static final String TIME_00_02_00 = "00:02:00";

    private PumpDbUtils() {
    }

    public static void clearTables(IDaoFactory factory) {
        IMixOffsetService<MixOffset> mixOffsetService = service(factory, MixOffset.class);
        mixOffsetService.deleteAll();
        LOGGER.info("Clearing tables");
    }

    public static void populateMixOffsetTable(IDaoFactory factory) {
        IMixOffsetService<MixOffset> mixOffsetService = service(factory, MixOffset.class);
        mixOffsetService.insert(createMixOffsetTestData());
    }

    private static List<MixOffset> createMixOffsetTestData() {
        List<MixOffset> mixOffsetList = new ArrayList<>();
        MixOffset mixOffset1 =
                new MixOffset(
                        1,
                        1,
                        EntityType.POSITION.getCode(),
                        EntitySuperType.FACT.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf(TIME_00_02_00),
                        3,
                        "exception1"
                );
        MixOffset mixOffset2 =
                new MixOffset(
                        2,
                        2,
                        EntityType.EVENT.getCode(),
                        EntitySuperType.FACT.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf(TIME_00_02_00),
                        3,
                        "exception2"
                );
        MixOffset mixOffset3 =
                new MixOffset(
                        3,
                        1,
                        EntityType.LOCATION.getCode(),
                        EntitySuperType.DIMENSION.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf(TIME_00_02_00),
                        3,
                        "exception3"
                );
        MixOffset mixOffset4 =
                new MixOffset(
                        4,
                        1,
                        EntityType.DRIVER.getCode(),
                        EntitySuperType.DIMENSION.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf(TIME_00_02_00),
                        3,
                        null
                );
        MixOffset mixOffset5 =
                new MixOffset(
                        5,
                        1,
                        EntityType.TRIP.getCode(),
                        EntitySuperType.FACT.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf(TIME_00_02_00),
                        3,
                        null
                );
        MixOffset mixOffset6 =
                new MixOffset(
                        6,
                        2,
                        EntityType.TRIP.getCode(),
                        EntitySuperType.FACT.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf(TIME_00_02_00),
                        3,
                        null
                );
        mixOffsetList.add(mixOffset1);
        mixOffsetList.add(mixOffset2);
        mixOffsetList.add(mixOffset3);
        mixOffsetList.add(mixOffset4);
        mixOffsetList.add(mixOffset5);
        mixOffsetList.add(mixOffset6);
        return mixOffsetList;
    }
}
