package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.event.validator.logic.problem.vehicle.ProblemVehicleCode;
import com.epam.tcodata.event.validator.logic.validation.status.ValidationStatusCode;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.spark.api.java.function.FlatMapGroupsFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.KeyValueGroupedDataset;
import scala.Serializable;
import scala.Tuple2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractDetector implements Serializable {

    private static final long serialVersionUID = 4171429082999227861L;

    /**
     * Detects issues depending on rule.
     *
     * @param enrichedDataset dateset
     * @return checked dateset
     */
    public Dataset<EnrichedPreparedEvent> detect(Dataset<EnrichedPreparedEvent> enrichedDataset) {
        Encoder<Tuple2<String, String>> tuple2Encoder = Encoders.tuple(Encoders.STRING(), Encoders.STRING());
        KeyValueGroupedDataset<Tuple2<String, String>, EnrichedPreparedEvent> groupedEnrichedDataset =
                enrichedDataset.groupByKey((MapFunction<EnrichedPreparedEvent, Tuple2<String, String>>) event ->
                        new Tuple2(event.getOrganizationDurableKey(), event.getVehicleDurableKey()), tuple2Encoder);

        return groupedEnrichedDataset
                .flatMapGroups((FlatMapGroupsFunction<Tuple2<String, String>,
                        EnrichedPreparedEvent, EnrichedPreparedEvent>) (key, values) -> {
                        List<EnrichedPreparedEvent> list = IteratorUtils.toList(values);
                        List<EnrichedPreparedEvent> sorted = list.stream()
                                .filter(enrichedPreparedEvent -> enrichedPreparedEvent.getStartDateTime() != null)
                                .sorted(Comparator.comparing(EnrichedPreparedEvent::getStartDateTime)).collect(Collectors.toList());

                        checkRules(sorted);
                        return sorted.iterator();

                    }, Encoders.bean(EnrichedPreparedEvent.class));
    }

    /**
     * Checking rules for specified events.
     *
     * @param sorted events to be checked.
     */
    public void checkRules(List<EnrichedPreparedEvent> sorted) {
        EnrichedPreparedEvent rangeStartEvent = null;
        EnrichedPreparedEvent previous = null;

        for (EnrichedPreparedEvent current : sorted) {
            if (isSuitable(current)) {
                if (rangeStartEvent == null) {
                    rangeStartEvent = current;
                } else {
                    if (isTimesEqualPreviousEventVelocityNull(previous, current)) {
                        setCodes(current, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.VELOCITY_ISSUE);
                    } else {
                        if (isTimesEqualCurrentEventVelocityNotNull(previous, current)) {
                            setCodes(current, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VELOCITY_ISSUE);
                        } else {
                            if (isInRange(rangeStartEvent, current, getRangeFrom(), getRangeTo())) {
                                setCodes(current, ValidationStatusCode.SUSPECT, ProblemVehicleCode.DUPLICATE_EVENTS);
                            } else {
                                rangeStartEvent = current;
                            }
                        }
                    }
                }
                previous = current;
            }
        }
    }

    abstract boolean isSuitable(EnrichedPreparedEvent event);

    abstract int getRangeFrom();

    abstract int getRangeTo();

    // rule 6.1a
    boolean isTimesEqualPreviousEventVelocityNull(EnrichedPreparedEvent previous,
                                                  EnrichedPreparedEvent current) {
        return previous.getStartDateTime().getTime() == current.getStartDateTime().getTime()
                && previous.getStartPositionSpeedKilometresPerHour() == null;
    }

    // rule 6.1b
    boolean isTimesEqualCurrentEventVelocityNotNull(EnrichedPreparedEvent previous,
                                                    EnrichedPreparedEvent current) {
        return previous.getStartDateTime().getTime() == current.getStartDateTime().getTime()
                && current.getStartPositionSpeedKilometresPerHour() != null;
    }

    // rule 6.1
    boolean isInRange(EnrichedPreparedEvent rangeStartEvent,
                      EnrichedPreparedEvent current,
                      int from,
                      int to) {
        return (current.getStartDateTime().getTime() - rangeStartEvent.getStartDateTime().getTime()) > from
                && (current.getStartDateTime().getTime() - rangeStartEvent.getStartDateTime().getTime()) <= to;
    }

    void setCodes(EnrichedPreparedEvent event, int validationStatusCode, int problemVehicleCode) {
        event.setValidationCode(validationStatusCode);
        event.setProblemVehicle(problemVehicleCode);
    }

}
