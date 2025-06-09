package com.epam.tcodata.hive.dal;

import com.epam.tcodata.hive.dal.domain.prepared.PreparedAreaEntityType;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class HiveConfigTest {
    HiveConfig raw = HiveConfig.RAW;
    HiveConfig prepared = HiveConfig.PREPARED;
    private static final String DIM_DRIVER = "dim_driver";
    private static final String DIM_LOCATION = "dim_location";
    private static final String FACT_EVENT = "fact_event";
    private static final String FACT_POSITION = "fact_position";
    private static final String FACT_SUBTRIP = "fact_subtrip";
    private static final String FACT_TRIP = "fact_trip";
    private static final String FACT_TACHO = "fact_tacho";

    @Test
    public void database() {
        assertEquals("raw", raw.database());
        assertEquals("prepared", prepared.database());
    }

    @Test
    public void tables() {
        Set<String> expected1 = new HashSet<>();
        expected1.addAll(Arrays.asList(
                "dim_asset",
                "dim_asset_norm",
                DIM_DRIVER,
                "dim_driver_norm",
                "dim_library_event",
                "dim_library_event_norm",
                DIM_LOCATION,
                "dim_location_norm",
                "dim_organisation_group",
                "dim_organisation_group_norm",
                "dim_organisation_subgroup",
                "dim_organisation_subgroup_norm",
                FACT_EVENT,
                FACT_POSITION,
                FACT_SUBTRIP,
                FACT_TRIP,
                FACT_TACHO,
                "fact_detected_event"
        ));

        assertEquals(expected1, raw.tableNames());

        Set<String> expected2 = new HashSet<>();
        expected2.addAll(Arrays.asList(
                "fact_event_video",
                FACT_SUBTRIP,
                DIM_LOCATION,
                FACT_POSITION,
                "dim_event_description",
                DIM_DRIVER,
                "dim_group",
                FACT_TRIP,
                FACT_TACHO,
                FACT_EVENT,
                "dim_vehicle",
                "fact_validated_event",
                "confirmed_overtaking_violation",
                "road_condition_violation",
                "dim_fuel_type",
                "dim_group_type",
                "dim_location_type",
                "dim_location_shape_type",
                "dim_vehicle_state",
                "dim_vehicle_type",
                "dim_video_channel_type",
                "dim_event_validation_code",
                "dim_event_problem_vehicle_code",
                "dim_overtaking_violation_code"
        ));

        assertEquals(expected2, prepared.tableNames());
    }

    @Test
    public void tableByEntityType() {
        assertEquals(null, raw.tableByEntityType(null));
        assertEquals("dim_asset", raw.tableByEntityType(RawAreaEntityType.ASSET));
        assertEquals(DIM_DRIVER, raw.tableByEntityType(RawAreaEntityType.DRIVER));
        assertEquals("dim_library_event", raw.tableByEntityType(RawAreaEntityType.LIBRARY_EVENT));
        assertEquals(DIM_LOCATION, raw.tableByEntityType(RawAreaEntityType.LOCATION));
        assertEquals("dim_organisation_group", raw.tableByEntityType(RawAreaEntityType.ORGANISATION_GROUP));
        assertEquals("dim_organisation_subgroup", raw.tableByEntityType(RawAreaEntityType.ORGANISATION_SUBGROUP));
        assertEquals(FACT_EVENT, raw.tableByEntityType(RawAreaEntityType.EVENT));
        assertEquals(FACT_POSITION, raw.tableByEntityType(RawAreaEntityType.POSITION));
        assertEquals(FACT_SUBTRIP, raw.tableByEntityType(RawAreaEntityType.SUBTRIP));
        assertEquals(FACT_TRIP, raw.tableByEntityType(RawAreaEntityType.TRIP));
        assertEquals(FACT_TACHO, raw.tableByEntityType(RawAreaEntityType.TACHO));
        assertEquals("fact_detected_event", raw.tableByEntityType(RawAreaEntityType.DETECTED_EVENT));

        assertEquals(null, prepared.tableByEntityType(null));
        assertEquals(FACT_TRIP, prepared.tableByEntityType(PreparedAreaEntityType.TRIP));
        assertEquals(FACT_TACHO, prepared.tableByEntityType(PreparedAreaEntityType.TACHO));
        assertEquals(FACT_SUBTRIP, prepared.tableByEntityType(PreparedAreaEntityType.SUBTRIP));
        assertEquals(FACT_POSITION, prepared.tableByEntityType(PreparedAreaEntityType.POSITION));
        assertEquals(FACT_EVENT, prepared.tableByEntityType(PreparedAreaEntityType.EVENT));
        assertEquals(DIM_DRIVER, prepared.tableByEntityType(PreparedAreaEntityType.DRIVER));
        assertEquals("dim_vehicle", prepared.tableByEntityType(PreparedAreaEntityType.VEHICLE));
        assertEquals("dim_event_description", prepared.tableByEntityType(PreparedAreaEntityType.EVENT_DESCRIPTION));
        assertEquals(DIM_LOCATION, prepared.tableByEntityType(PreparedAreaEntityType.LOCATION));
        assertEquals("confirmed_overtaking_violation", prepared.tableByEntityType(PreparedAreaEntityType.CONFIRMED_OVERTAKING_VIOLATION));
        assertEquals("road_condition_violation", prepared.tableByEntityType(PreparedAreaEntityType.ROAD_CONDITION_VIOLATION));
    }
}