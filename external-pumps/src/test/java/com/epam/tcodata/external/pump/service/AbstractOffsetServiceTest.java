package com.epam.tcodata.external.pump.service;

import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.factory.impl.ExternalPositionFactory;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotSame;

public class AbstractOffsetServiceTest {

    @Test
    public void testFixingOverdueOffsets() {
        Map<Long, MixOffset> offsets = new HashMap<>();

        MixOffset mustBeChangedOffset = new MixOffset();
        mustBeChangedOffset.setLastProcessedTime(Instant.parse("2019-01-01T00:00:00Z"));
        offsets.put(1L, mustBeChangedOffset);

        MixOffset originalOffset = new MixOffset();
        originalOffset.setLastProcessedTime(Instant.parse("2019-01-01T00:00:00Z"));

        IExternalFactory f = new ExternalPositionFactory();
        offsets.forEach((key, offset) -> AbstractOffsetService.fixOverdueOffset(key, offset, f));

        System.out.println(offsets);

        assertNotSame(mustBeChangedOffset, originalOffset);
    }

}
