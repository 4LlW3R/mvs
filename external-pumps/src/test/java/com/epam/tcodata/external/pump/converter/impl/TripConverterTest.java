package com.epam.tcodata.external.pump.converter.impl;


import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.SubTrip;
import com.epam.tcodata.models.mix.fact.Trip;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class TripConverterTest {

    @Test
    public void testSubTripId() throws Exception {
        // Setup
        IConverter<Trip, EnrichedTrip, AvroTrip> tripConverter = new TripConverter();
        Trip inputTrip = createTestTripWithSimbleSubtrips(5);
        FactDto<Trip> factDto = new FactDto<>(
                null,
                0L,
                7,
                "",
                new Time(0L),
                0L);

        IKeyManager keyManager = Mockito.mock(IKeyManager.class);
        when(keyManager.factDurableKey(any(EntityType.class), anyString()))
                .thenReturn(UUID.fromString("00000000-0000-0000-c000-000000000046"));

        EnrichedTrip enrichedTrip = tripConverter.convertToEnriched(inputTrip, factDto, keyManager);

        List<Long> expectedSubTripIds = Arrays.asList(0L, 1L, 2L, 3L, 4L);

        // Main logic run
        List<EnrichedSubTrip> outputSubtrips = enrichedTrip.getEnrichedSubTripList();

        // Checking
        List<Long> actual = outputSubtrips.stream().map(enrichedSubTrip -> enrichedSubTrip.getSubTripId()).collect(Collectors.toList());
        TestCase.assertEquals(expectedSubTripIds, actual);
    }

    private static Trip createTestTripWithSimbleSubtrips(int numberOfSubtrips) {
        Trip trip = new Trip();
        trip.setTripId(0L);
        List<SubTrip> subTrips = new ArrayList<>();
        for (int i = 0; i < numberOfSubtrips; ++i) {
            SubTrip subTrip = new SubTrip();
            subTrip.setStartPositionId(0L);
            subTrips.add(subTrip);
        }
        trip.setSubTripList(subTrips);
        return trip;
    }

}
