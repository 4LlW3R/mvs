package com.epam.tcodata.raw.prepared.etl;

import com.epam.tcodata.models.datalake.raw.dimension.RawLibraryEvent;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ReferenceSupplierTest {
    @Test(expected = IllegalStateException.class)
    public void testCollectWithDuplicates_failed() {
        RawLibraryEvent rw1 = new RawLibraryEvent();
        rw1.setEventTypeId(100L);

        RawLibraryEvent rw2 = new RawLibraryEvent();
        rw2.setEventTypeId(100L);

        List<RawLibraryEvent> libraryEventList = Arrays.asList(rw1, rw2);
        Map<Long, RawLibraryEvent> collect = libraryEventList.stream()
                .collect(Collectors.toMap(libraryEvent -> libraryEvent.getEventTypeId(), libraryEvent -> libraryEvent));
    }

    @Test
    public void testCollectWithDuplicates_passed() {
        RawLibraryEvent rw1 = new RawLibraryEvent();
        rw1.setEventTypeId(100L);

        RawLibraryEvent rw2 = new RawLibraryEvent();
        rw2.setEventTypeId(100L);

        List<RawLibraryEvent> libraryEventList = Arrays.asList(rw1, rw2);
        Map<Long, RawLibraryEvent> collect = libraryEventList.stream()
                .collect(Collectors.toMap(RawLibraryEvent::getEventTypeId, Function.identity(), (existing, replacement) -> existing));

        assertEquals(collect.keySet().size(), 1);
    }
}