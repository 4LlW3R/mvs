package com.epam.tcodata.models.datalake;

import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class AbstractDataLakeEntityTest {

    @Test
    public void shouldReturnOrderedValues() {
        RawPosition rawPosition = new RawPosition();
        rawPosition.setAssetId(111L);
        Object[] actual = rawPosition.orderedValues();

        Object[] expected = new Object[] {null, null, null, null, null, null, null, null, null, null, null, 111L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
        assertArrayEquals(expected, actual);
    }

}
