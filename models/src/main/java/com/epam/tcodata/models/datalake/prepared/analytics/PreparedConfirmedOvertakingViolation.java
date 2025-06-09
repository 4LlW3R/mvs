package com.epam.tcodata.models.datalake.prepared.analytics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.AbstractDataLakeEntity;

import java.sql.Timestamp;
import java.util.Arrays;

@SuppressWarnings("CPD-START")
public class PreparedConfirmedOvertakingViolation extends AbstractDataLakeEntity {

    private static final long serialVersionUID = 5165241856427672947L;

    public static class Fields {
        public static final String DOMAIN = "domain";
        public static final String ENTITY_TYPE = "entity_type";
        public static final String SCHEMA_VERSION = "schema_version";
        public static final String ID = "id";
        public static final String POLICY_VERSION = "policy_version";
        public static final String SOURCE_TYPE = "source_type";
        public static final String PASSING_PARTICIPANT_VEHICLE_DURABLE_ID = "passing_participant_vehicle_durable_id";
        public static final String PASSING_PARTICIPANT_DRIVER_DURABLE_ID = "passing_participant_driver_durable_id";
        public static final String PASSING_PARTICIPANT_INTERPOLATED_OVERTAKING_VELOCITY = "passing_participant_interpolated_overtaking_velocity";
        public static final String PASSED_PARTICIPANT_VEHICLE_DURABLE_ID = "passed_participant_vehicle_durable_id";
        public static final String PASSED_PARTICIPANT_DRIVER_DURABLE_ID = "passed_participant_driver_durable_id";
        public static final String PASSED_PARTICIPANT_INTERPOLATED_OVERTAKING_VELOCITY = "passed_participant_interpolated_overtaking_velocity";
        public static final String PASSING_TRAJECTORY_START_LATITUDE = "passing_trajectory_start_latitude";
        public static final String PASSING_TRAJECTORY_START_LONGITUDE = "passing_trajectory_start_longitude";
        public static final String PASSING_TRAJECTORY_START_TIME = "passing_trajectory_start_time";
        public static final String PASSING_TRAJECTORY_END_LATITUDE = "passing_trajectory_end_latitude";
        public static final String PASSING_TRAJECTORY_END_LONGITUDE = "passing_trajectory_end_longitude";
        public static final String PASSING_TRAJECTORY_END_TIME = "passing_trajectory_end_time";
        public static final String PASSED_TRAJECTORY_START_LATITUDE = "passed_trajectory_start_latitude";
        public static final String PASSED_TRAJECTORY_START_LONGITUDE = "passed_trajectory_start_longitude";
        public static final String PASSED_TRAJECTORY_START_TIME = "passed_trajectory_start_time";
        public static final String PASSED_TRAJECTORY_END_LATITUDE = "passed_trajectory_end_latitude";
        public static final String PASSED_TRAJECTORY_END_LONGITUDE = "passed_trajectory_end_longitude";
        public static final String PASSED_TRAJECTORY_END_TIME = "passed_trajectory_end_time";
        public static final String TIME = "time";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String POLICY_AREA_IDS = "policy_area_ids";
        public static final String VIOLATION_IDS = "violation_ids";
        public static final String VALIDATION_COMMENT = "validation_comment";
        public static final String UNCLASSIFIED_VIOLATION_DESCRIPTION = "unclassified_violation_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DOMAIN)
    private String domain;
    @ColumnName(Fields.ENTITY_TYPE)
    private Integer entityType;
    @ColumnName(Fields.SCHEMA_VERSION)
    private Integer schemaVersion;
    @ColumnName(Fields.ID)
    private String id;
    @ColumnName(Fields.POLICY_VERSION)
    private Integer policyVersion;
    @ColumnName(Fields.SOURCE_TYPE)
    private String sourceType;

    @ColumnName(Fields.PASSING_PARTICIPANT_VEHICLE_DURABLE_ID)
    private String passingParticipantVehicleDurableID;
    @ColumnName(Fields.PASSING_PARTICIPANT_DRIVER_DURABLE_ID)
    private String passingParticipantDriverDurableID;
    @ColumnName(Fields.PASSING_PARTICIPANT_INTERPOLATED_OVERTAKING_VELOCITY)
    private Double passingParticipantInterpolatedOvertakingVelocity;

    @ColumnName(Fields.PASSED_PARTICIPANT_VEHICLE_DURABLE_ID)
    private String passedParticipantVehicleDurableID;
    @ColumnName(Fields.PASSED_PARTICIPANT_DRIVER_DURABLE_ID)
    private String passedParticipantDriverDurableID;
    @ColumnName(Fields.PASSED_PARTICIPANT_INTERPOLATED_OVERTAKING_VELOCITY)
    private Double passedParticipantInterpolatedOvertakingVelocity;

    @ColumnName(Fields.PASSING_TRAJECTORY_START_LATITUDE)
    private Double passingTrajectoryStartLatitude;
    @ColumnName(Fields.PASSING_TRAJECTORY_START_LONGITUDE)
    private Double passingTrajectoryStartLongitude;
    @ColumnName(Fields.PASSING_TRAJECTORY_START_TIME)
    private Timestamp passingTrajectoryStartTime;
    @ColumnName(Fields.PASSING_TRAJECTORY_END_LATITUDE)
    private Double passingTrajectoryEndLatitude;
    @ColumnName(Fields.PASSING_TRAJECTORY_END_LONGITUDE)
    private Double passingTrajectoryEndLongitude;
    @ColumnName(Fields.PASSING_TRAJECTORY_END_TIME)
    private Timestamp passingTrajectoryEndTime;

    @ColumnName(Fields.PASSED_TRAJECTORY_START_LATITUDE)
    private Double passedTrajectoryStartLatitude;
    @ColumnName(Fields.PASSED_TRAJECTORY_START_LONGITUDE)
    private Double passedTrajectoryStartLongitude;
    @ColumnName(Fields.PASSED_TRAJECTORY_START_TIME)
    private Timestamp passedTrajectoryStartTime;
    @ColumnName(Fields.PASSED_TRAJECTORY_END_LATITUDE)
    private Double passedTrajectoryEndLatitude;
    @ColumnName(Fields.PASSED_TRAJECTORY_END_LONGITUDE)
    private Double passedTrajectoryEndLongitude;
    @ColumnName(Fields.PASSED_TRAJECTORY_END_TIME)
    private Timestamp passedTrajectoryEndTime;

    @ColumnName(Fields.TIME)
    private Timestamp time;
    @ColumnName(Fields.LATITUDE)
    private Double latitude;
    @ColumnName(Fields.LONGITUDE)
    private Double longitude;
    @ColumnName(Fields.POLICY_AREA_IDS)
    private String[] policyAreaIDs;
    @ColumnName(Fields.VIOLATION_IDS)
    private Integer[] violationIDs;
    @ColumnName(Fields.VALIDATION_COMMENT)
    private String validationComment;
    @ColumnName(Fields.UNCLASSIFIED_VIOLATION_DESCRIPTION)
    private String unclassifiedViolationDescription;

    public PreparedConfirmedOvertakingViolation() {
        violationIDs = new Integer[0];
        policyAreaIDs = new String[0];
    }


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPolicyVersion() {
        return policyVersion;
    }

    public void setPolicyVersion(Integer policyVersion) {
        this.policyVersion = policyVersion;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }


    public String getPassingParticipantVehicleDurableID() {
        return passingParticipantVehicleDurableID;
    }

    public void setPassingParticipantVehicleDurableID(String passingParticipantVehicleDurableID) {
        this.passingParticipantVehicleDurableID = passingParticipantVehicleDurableID;
    }

    public String getPassingParticipantDriverDurableID() {
        return passingParticipantDriverDurableID;
    }

    public void setPassingParticipantDriverDurableID(String passingParticipantDriverDurableID) {
        this.passingParticipantDriverDurableID = passingParticipantDriverDurableID;
    }

    public Double getPassingParticipantInterpolatedOvertakingVelocity() {
        return passingParticipantInterpolatedOvertakingVelocity;
    }

    public void setPassingParticipantInterpolatedOvertakingVelocity(Double passingParticipantInterpolatedOvertakingVelocity) {
        this.passingParticipantInterpolatedOvertakingVelocity = passingParticipantInterpolatedOvertakingVelocity;
    }

    public String getPassedParticipantVehicleDurableID() {
        return passedParticipantVehicleDurableID;
    }

    public void setPassedParticipantVehicleDurableID(String passedParticipantVehicleDurableID) {
        this.passedParticipantVehicleDurableID = passedParticipantVehicleDurableID;
    }

    public String getPassedParticipantDriverDurableID() {
        return passedParticipantDriverDurableID;
    }

    public void setPassedParticipantDriverDurableID(String passedParticipantDriverDurableID) {
        this.passedParticipantDriverDurableID = passedParticipantDriverDurableID;
    }

    public Double getPassedParticipantInterpolatedOvertakingVelocity() {
        return passedParticipantInterpolatedOvertakingVelocity;
    }

    public void setPassedParticipantInterpolatedOvertakingVelocity(Double passedParticipantInterpolatedOvertakingVelocity) {
        this.passedParticipantInterpolatedOvertakingVelocity = passedParticipantInterpolatedOvertakingVelocity;
    }

    public Double getPassingTrajectoryStartLatitude() {
        return passingTrajectoryStartLatitude;
    }

    public void setPassingTrajectoryStartLatitude(Double passingTrajectoryStartLatitude) {
        this.passingTrajectoryStartLatitude = passingTrajectoryStartLatitude;
    }

    public Double getPassingTrajectoryStartLongitude() {
        return passingTrajectoryStartLongitude;
    }

    public void setPassingTrajectoryStartLongitude(Double passingTrajectoryStartLongitude) {
        this.passingTrajectoryStartLongitude = passingTrajectoryStartLongitude;
    }

    public Timestamp getPassingTrajectoryStartTime() {
        return (passingTrajectoryStartTime == null) ? null : new Timestamp(passingTrajectoryStartTime.getTime());
    }

    public void setPassingTrajectoryStartTime(Timestamp passingTrajectoryStartTime) {
        this.passingTrajectoryStartTime = (passingTrajectoryStartTime == null) ? null : new Timestamp(passingTrajectoryStartTime.getTime());
    }

    public Double getPassingTrajectoryEndLatitude() {
        return passingTrajectoryEndLatitude;
    }

    public void setPassingTrajectoryEndLatitude(Double passingTrajectoryEndLatitude) {
        this.passingTrajectoryEndLatitude = passingTrajectoryEndLatitude;
    }

    public Double getPassingTrajectoryEndLongitude() {
        return passingTrajectoryEndLongitude;
    }

    public void setPassingTrajectoryEndLongitude(Double passingTrajectoryEndLongitude) {
        this.passingTrajectoryEndLongitude = passingTrajectoryEndLongitude;
    }

    public Timestamp getPassingTrajectoryEndTime() {
        return (passingTrajectoryEndTime == null) ? null : new Timestamp(passingTrajectoryEndTime.getTime());
    }

    public void setPassingTrajectoryEndTime(Timestamp passingTrajectoryEndTime) {
        this.passingTrajectoryEndTime = (passingTrajectoryEndTime == null) ? null : new Timestamp(passingTrajectoryEndTime.getTime());
    }

    public Double getPassedTrajectoryStartLatitude() {
        return passedTrajectoryStartLatitude;
    }

    public void setPassedTrajectoryStartLatitude(Double passedTrajectoryStartLatitude) {
        this.passedTrajectoryStartLatitude = passedTrajectoryStartLatitude;
    }

    public Double getPassedTrajectoryStartLongitude() {
        return passedTrajectoryStartLongitude;
    }

    public void setPassedTrajectoryStartLongitude(Double passedTrajectoryStartLongitude) {
        this.passedTrajectoryStartLongitude = passedTrajectoryStartLongitude;
    }

    public Timestamp getPassedTrajectoryStartTime() {
        return (passedTrajectoryStartTime == null) ? null : new Timestamp(passedTrajectoryStartTime.getTime());
    }

    public void setPassedTrajectoryStartTime(Timestamp passedTrajectoryStartTime) {
        this.passedTrajectoryStartTime = (passedTrajectoryStartTime == null) ? null : new Timestamp(passedTrajectoryStartTime.getTime());
    }

    public Double getPassedTrajectoryEndLatitude() {
        return passedTrajectoryEndLatitude;
    }

    public void setPassedTrajectoryEndLatitude(Double passedTrajectoryEndLatitude) {
        this.passedTrajectoryEndLatitude = passedTrajectoryEndLatitude;
    }

    public Double getPassedTrajectoryEndLongitude() {
        return passedTrajectoryEndLongitude;
    }

    public void setPassedTrajectoryEndLongitude(Double passedTrajectoryEndLongitude) {
        this.passedTrajectoryEndLongitude = passedTrajectoryEndLongitude;
    }

    public Timestamp getPassedTrajectoryEndTime() {
        return (passedTrajectoryEndTime == null) ? null : new Timestamp(passedTrajectoryEndTime.getTime());
    }

    public void setPassedTrajectoryEndTime(Timestamp passedTrajectoryEndTime) {
        this.passedTrajectoryEndTime = (passedTrajectoryEndTime == null) ? null : new Timestamp(passedTrajectoryEndTime.getTime());
    }

    public Timestamp getTime() {
        return (time == null) ? null : new Timestamp(time.getTime());
    }

    public void setTime(Timestamp time) {
        this.time = (time == null) ? null : new Timestamp(time.getTime());
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String[] getPolicyAreaIDs() {
        return policyAreaIDs != null ? Arrays.copyOf(policyAreaIDs, policyAreaIDs.length, String[].class) : null;
    }

    public void setPolicyAreaIDs(String[] policyAreaIDs) {
        this.policyAreaIDs = Arrays.copyOf(policyAreaIDs, policyAreaIDs.length, String[].class);
    }

    public Integer[] getViolationIDs() {
        return violationIDs != null ? Arrays.copyOf(violationIDs, violationIDs.length, Integer[].class) : null;
    }

    public void setViolationIDs(Integer[] violationIDs) {
        this.violationIDs = Arrays.copyOf(violationIDs, violationIDs.length);
    }

    public String getValidationComment() {
        return validationComment;
    }

    public void setValidationComment(String validationComment) {
        this.validationComment = validationComment;
    }

    public String getUnclassifiedViolationDescription() {
        return unclassifiedViolationDescription;
    }

    public void setUnclassifiedViolationDescription(String unclassifiedViolationDescription) {
        this.unclassifiedViolationDescription = unclassifiedViolationDescription;
    }
}
