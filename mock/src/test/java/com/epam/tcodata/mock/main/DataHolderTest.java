package com.epam.tcodata.mock.main;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedPosition;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class DataHolderTest {

    @Test
    public void compareTablesTest() {
        PreparedPosition position1 = new PreparedPosition();
        position1.setDurableId(UUID.nameUUIDFromBytes("".getBytes()).toString());
        position1.setAssetId(100L);
        position1.setPersistedDateUtc(Timestamp.from(Instant.now()));

        PreparedPosition position2 = new PreparedPosition();
        position2.setDurableId(UUID.nameUUIDFromBytes("".getBytes()).toString());
        position2.setAssetId(100L);
        position2.setPersistedDateUtc(Timestamp.from(Instant.now()));

        List<PreparedPosition> actual = Arrays.asList(position1);
        List<PreparedPosition> expected = Arrays.asList(position2);

        boolean res = DataHolder.compareTables(actual, expected, "", "", "persistedDateUtc");
        assertTrue(res);
    }

}