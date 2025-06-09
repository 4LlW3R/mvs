package com.epam.tcodata.application.manager.service;

import com.epam.tcodata.models.SignalType;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.EntityType;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class SignalServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalServiceTest.class);

    private SignalService signalManager;

    @Before
    public void init() {
        this.signalManager = new SignalService();
    }

    @Test
    public void shouldPrepareSignals() {
        Set<ApplicationType> applicationTypes = new HashSet<>(Arrays.asList(ApplicationType.EXTERNAL_PUMP, ApplicationType.INTERNAL_PUMP, ApplicationType.OVERTAKING_DETECTION));
        Set<EntityType> entityTypes = new HashSet<>(Arrays.asList(EntityType.POSITION, EntityType.LOCATION));
        SignalType signal = SignalType.STOP;
        String message = "Message";
        List<Signal> actual = signalManager.prepareSignals(applicationTypes, entityTypes, signal, message);

        Timestamp timestamp = Timestamp.from(Instant.now());
        actual.forEach(sg -> sg.setTimestamp(timestamp));

        List<Signal> expected = new ArrayList<>();
        expected.add(new Signal(0,
                ApplicationType.EXTERNAL_PUMP.getCode(),
                ApplicationType.EXTERNAL_PUMP.getSuperType().getCode(),
                EntityType.POSITION.getCode(),
                EntityType.POSITION.getSuperType().getCode(),
                timestamp,
                SignalType.STOP.getCode(),
                message));
        expected.add(new Signal(0,
                ApplicationType.EXTERNAL_PUMP.getCode(),
                ApplicationType.EXTERNAL_PUMP.getSuperType().getCode(),
                EntityType.LOCATION.getCode(),
                EntityType.LOCATION.getSuperType().getCode(),
                timestamp,
                SignalType.STOP.getCode(),
                message));
        expected.add(new Signal(0,
                ApplicationType.INTERNAL_PUMP.getCode(),
                ApplicationType.INTERNAL_PUMP.getSuperType().getCode(),
                EntityType.POSITION.getCode(),
                EntityType.POSITION.getSuperType().getCode(),
                timestamp,
                SignalType.STOP.getCode(),
                message));
        expected.add(new Signal(0,
                ApplicationType.INTERNAL_PUMP.getCode(),
                ApplicationType.INTERNAL_PUMP.getSuperType().getCode(),
                EntityType.LOCATION.getCode(),
                EntityType.LOCATION.getSuperType().getCode(),
                timestamp,
                SignalType.STOP.getCode(),
                message));
        expected.add(new Signal(0,
                ApplicationType.OVERTAKING_DETECTION.getCode(),
                ApplicationType.OVERTAKING_DETECTION.getSuperType().getCode(),
                null,
                null,
                timestamp,
                SignalType.STOP.getCode(),
                message));

        LOGGER.info(String.valueOf(actual));
        LOGGER.info(String.valueOf(expected));

        TestCase.assertTrue(actual.containsAll(expected));
    }

}
