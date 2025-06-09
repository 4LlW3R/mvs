package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps;

import com.epam.tcodata.storage.avro.entities.events.overtaking.PassingVehicleIdx;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class EntitySerDeSuiteTest extends OvertakingTestUsingCSVBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySerDeSuiteTest.class);

    @Test
    public void testInnerOvertakingEntityToAvro() throws IOException {
        List<com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent> ovt = runDetection("/real/gps_1.csv");
        List<com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro> avro =
                ovt.stream()
                        .map(com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent::toAvro)
                        .collect(Collectors.toList());
        assertEquals(1, avro.size());
        LOGGER.info(String.valueOf(avro.get(0)));
        assertEquals(PassingVehicleIdx.A_OVERTOOK_B, avro.get(0).getAOvertookB());
    }
}
