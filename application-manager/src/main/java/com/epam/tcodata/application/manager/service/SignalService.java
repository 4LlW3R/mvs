package com.epam.tcodata.application.manager.service;

import com.epam.tcodata.models.SignalType;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.EntityType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignalService {

    /**
     * Responsible for sending signals to applications.
     */
    public SignalService() {
        /***  Default implementation ***/
    }

    /**
     * Method creates Signals.
     *
     * @param applicationTypes applications which should receive signal.
     * @param entityTypes      affected entities.
     * @param signalType       signal type.
     * @param message          text message.
     */
    public List<Signal> prepareSignals(Set<ApplicationType> applicationTypes,
                                       Set<EntityType> entityTypes,
                                       SignalType signalType,
                                       String message) {

        return applicationTypes.stream()
                .flatMap((Function<ApplicationType, Stream<Signal>>) applicationType -> {
                    switch (applicationType.getSuperType()) {
                        case PUMP:
                            return prepareSignalsForPump(applicationType, entityTypes, signalType, message).stream();
                        case ANALYTICS:
                            return Stream.of(prepareSignalsForAnalytics(applicationType, signalType, message));
                        case PERSISTENCE:
                            return Stream.of(prepareSignalsForPersistence(applicationType, signalType, message));
                        default:
                            String msg = "Unknown application type";
                            throw new IllegalArgumentException(msg);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<Signal> prepareSignalsForPump(ApplicationType applicationType,
                                               Set<EntityType> entityTypes,
                                               SignalType signalType,
                                               String message) {

        return entityTypes.stream()
                .map(entityType -> new Signal(0,
                        applicationType.getCode(),
                        applicationType.getSuperType().getCode(),
                        entityType.getCode(),
                        entityType.getSuperType().getCode(),
                        Timestamp.from(Instant.now()),
                        signalType.getCode(),
                        message))
                .collect(Collectors.toList());
    }

    private Signal prepareSignalsForAnalytics(ApplicationType applicationType,
                                              SignalType signalType,
                                              String message) {

        return new Signal(0,
                applicationType.getCode(),
                applicationType.getSuperType().getCode(),
                null,
                null,
                Timestamp.from(Instant.now()),
                signalType.getCode(),
                message);
    }

    private Signal prepareSignalsForPersistence(ApplicationType applicationType,
                                                SignalType signalType,
                                                String message) {

        return new Signal(0,
                applicationType.getCode(),
                applicationType.getSuperType().getCode(),
                null,
                null,
                Timestamp.from(Instant.now()),
                signalType.getCode(),
                message);
    }

}
