package com.epam.tcodata.overtaking.violation.stream.datalake.converter;

import com.epam.tcodata.common.ConverterUtils;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedConfirmedOvertakingViolation;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroConfirmedOvertakingViolation;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class ConfirmedOvertakingConverter implements IConfirmedOvertakingConverter {

    private static final long serialVersionUID = -1933541428982039015L;

    @Override
    /**
     * Method to convert confirmedOvertakingEventWithViolations to overtakingEvents.
     *
     * @param confirmedOvertakingEventWithViolations confirmedOvertakingEventWithViolations iterator
     */
    public PreparedConfirmedOvertakingViolation convertToConfirmedOvertakingViolation(AvroConfirmedOvertakingViolation avroViolation) {
        PreparedConfirmedOvertakingViolation violation = new PreparedConfirmedOvertakingViolation();
        violation.setDomain(checkedToString(avroViolation.getDomain()));
        violation.setEntityType(avroViolation.getEntityType());
        violation.setSchemaVersion(avroViolation.getSchemaVersion());
        violation.setId(checkedToString(avroViolation.getId()));
        violation.setPolicyVersion(avroViolation.getPolicyVersion());
        violation.setSourceType(avroViolation.getSourceType().name());
        violation.setPassingParticipantVehicleDurableID(checkedToString(avroViolation.getPassingParticipant().getVehicleDurableID()));
        violation.setPassingParticipantDriverDurableID(checkedToString(avroViolation.getPassingParticipant().getDriverDurableID()));
        violation.setPassingParticipantInterpolatedOvertakingVelocity(avroViolation.getPassingParticipant().getInterpolatedOvertakingVelocity());
        violation.setPassedParticipantVehicleDurableID(checkedToString(avroViolation.getPassedParticipant().getVehicleDurableID()));
        violation.setPassedParticipantDriverDurableID(checkedToString(avroViolation.getPassedParticipant().getDriverDurableID()));
        violation.setPassedParticipantInterpolatedOvertakingVelocity(avroViolation.getPassedParticipant().getInterpolatedOvertakingVelocity());
        violation.setPassingTrajectoryStartLatitude(avroViolation.getPassingTrajectory().getStartLatitude());
        violation.setPassingTrajectoryStartLongitude(avroViolation.getPassingTrajectory().getStartLongitude());
        violation.setPassingTrajectoryStartTime(new Timestamp(avroViolation.getPassingTrajectory().getStartTime().getMillis()));
        violation.setPassingTrajectoryEndLatitude(avroViolation.getPassingTrajectory().getEndLatitude());
        violation.setPassingTrajectoryEndLongitude(avroViolation.getPassingTrajectory().getEndLongitude());
        violation.setPassingTrajectoryEndTime(new Timestamp(avroViolation.getPassingTrajectory().getEndTime().getMillis()));
        violation.setPassedTrajectoryStartLatitude(avroViolation.getPassedTrajectory().getStartLatitude());
        violation.setPassedTrajectoryStartLongitude(avroViolation.getPassedTrajectory().getStartLongitude());
        violation.setPassedTrajectoryStartTime(new Timestamp(avroViolation.getPassedTrajectory().getStartTime().getMillis()));
        violation.setPassedTrajectoryEndLatitude(avroViolation.getPassedTrajectory().getEndLatitude());
        violation.setPassedTrajectoryEndLongitude(avroViolation.getPassedTrajectory().getEndLongitude());
        violation.setPassedTrajectoryEndTime(new Timestamp(avroViolation.getPassedTrajectory().getEndTime().getMillis()));
        violation.setTime(new Timestamp(avroViolation.getTime().getMillis()));
        violation.setLatitude(avroViolation.getLatitude());
        violation.setLongitude(avroViolation.getLongitude());
        violation.setPolicyAreaIDs(avroViolation.getPolicyAreaIds().stream().map(CharSequence::toString).toArray(String[]::new));
        violation.setViolationIDs(avroViolation.getViolationIDs().toArray(new Integer[avroViolation.getViolationIDs().size()]));
        violation.setValidationComment(checkedToString(avroViolation.getValidationComment()));
        violation.setUnclassifiedViolationDescription(checkedToString(avroViolation.getUnclassifiedViolationDescription()));
        return violation;
    }

    /**
     * Method to convert confirmedOvertakingEventWithViolations to factDetectedEvents.
     *
     * @param avroViolation    confirmedOvertakingEventWithViolations rdd
     * @param eventTypeId      event type
     * @param persistedDateUtc persisted time
     */
    public RawDetectedEvent convertToDataLakeEvent(AvroConfirmedOvertakingViolation avroViolation,
                                                   Long eventTypeId,
                                                   Timestamp persistedDateUtc) {
        RawDetectedEvent dataLakeEvent = new RawDetectedEvent();
        dataLakeEvent.setYear(ConverterUtils.timestampToYear(persistedDateUtc));
        dataLakeEvent.setWeekNumber(ConverterUtils.timestampToWeekNumber(persistedDateUtc));
        dataLakeEvent.setDurableId(checkedToString(avroViolation.getId()));
        dataLakeEvent.setPersistedDateUtc(persistedDateUtc);
        dataLakeEvent.setDriverDurableKey(checkedToString(avroViolation.getPassingParticipant().getDriverDurableID()));
        dataLakeEvent.setVehicleDurableKey(checkedToString(avroViolation.getPassingParticipant().getVehicleDurableID()));
        dataLakeEvent.setEventTypeId(eventTypeId);
        dataLakeEvent.setStartDateTime(new Timestamp(avroViolation.getTime().getMillis()));
        dataLakeEvent.setValue(avroViolation.getPassingParticipant().getInterpolatedOvertakingVelocity());
        dataLakeEvent.setStartPositionLatitude(avroViolation.getPassingTrajectory().getStartLatitude());
        dataLakeEvent.setStartPositionLongitude(avroViolation.getPassingTrajectory().getStartLongitude());
        dataLakeEvent.setStartPositionTimestamp(new Timestamp(avroViolation.getPassingTrajectory().getStartTime().getMillis()));
        dataLakeEvent.setEndPositionLatitude(avroViolation.getPassingTrajectory().getEndLatitude());
        dataLakeEvent.setEndPositionLongitude(avroViolation.getPassingTrajectory().getEndLongitude());
        dataLakeEvent.setEndPositionTimestamp(new Timestamp(avroViolation.getPassingTrajectory().getEndTime().getMillis()));
        dataLakeEvent.setSourceType(avroViolation.getSourceType().name());
        dataLakeEvent.setInterpolatedPointLatitude(avroViolation.getLatitude());
        dataLakeEvent.setInterpolatedPointLongitude(avroViolation.getLongitude());
        dataLakeEvent.setViolationIDs(avroViolation.getViolationIDs().toArray(new Integer[avroViolation.getViolationIDs().size()]));
        return dataLakeEvent;
    }
}
