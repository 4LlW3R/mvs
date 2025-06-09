package com.epam.tcodata.analytics.road.condition.violation.detection.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class RoadConditionViolation implements Serializable {
    private static final String ID_TEMPLATE = "%s-%s-%s";
    private static final long serialVersionUID = 1995293342546521412L;
    private String id;
    private Long subscriptionId;
    private String vehicleId;
    private String driverId;
    private Integer violationId;
    private String policyAreaId;
    private Timestamp creationTimeUTC;
    private Timestamp startTime;
    private Timestamp endTime;
    private Double averageSpeed;
    private Double maxSpeed;
    private String startFactGpsId;
    private String endFactGpsId;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;

    public RoadConditionViolation() {
        /***  Default implementation ***/
    }

    /**
     * Simple Java bean to handle aggregation from trajectory data to policy needed information. Possibly, should remove this and transfer to avro directly.
     */
    public static RoadConditionViolation fromTrajectory(List<GPSPointWithArea> trajectory, Timestamp creationTime) {
        RoadConditionViolation viol = new RoadConditionViolation();
        GPSPointWithArea start = trajectory.get(0);
        GPSPointWithArea end = trajectory.get(trajectory.size() - 1);
        viol.setSubscriptionId(start.getSubscriptionId());
        viol.setVehicleId(start.getVehicleId());
        viol.setDriverId(start.getDriverId());
        viol.setStartTime(start.getTime());
        viol.setEndTime(trajectory.get(trajectory.size() - 1).getTime());
        viol.setCreationTimeUTC(creationTime);
        viol.setPolicyAreaId(start.getAreaId());
        // getAsDouble from Optional without check as there is always
        // a value unless there was serialization issues (in which case job should fail)
        DoubleSummaryStatistics stats = trajectory
                                                .stream()
                                                .mapToDouble(GPSPointWithArea::getVelocity)
                                                .summaryStatistics();
        viol.setAverageSpeed(stats.getAverage());
        viol.setMaxSpeed(stats.getMax());
        viol.setId(String.format(ID_TEMPLATE, viol.subscriptionId, viol.vehicleId, creationTime.getTime()));

        viol.setStartFactGpsId(start.getExternalId());
        viol.setEndFactGpsId(end.getExternalId());

        viol.setStartLatitude(start.getLatitude());
        viol.setStartLongitude(start.getLongitude());

        viol.setEndLatitude(end.getLatitude());
        viol.setEndLongitude(end.getLongitude());
        return viol;
    }

    //region Getters and setters

    public static String getIdTemplate() {
        return ID_TEMPLATE;
    }

    public Timestamp getStartTime() {
        return startTime == null ? null : new Timestamp(startTime.getTime());
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime == null ? null : new Timestamp(startTime.getTime());
    }

    public Timestamp getEndTime() {
        return endTime == null ? null : new Timestamp(endTime.getTime());
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime == null ? null : new Timestamp(endTime.getTime());
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Integer getViolationId() {
        return violationId;
    }

    public void setViolationId(Integer violationId) {
        this.violationId = violationId;
    }

    public String getPolicyAreaId() {
        return policyAreaId;
    }

    public void setPolicyAreaId(String policyAreaId) {
        this.policyAreaId = policyAreaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreationTimeUTC() {
        return creationTimeUTC == null ? null : new Timestamp(creationTimeUTC.getTime());
    }

    public void setCreationTimeUTC(Timestamp creationTimeUTC) {
        this.creationTimeUTC = creationTimeUTC == null ? null : new Timestamp(creationTimeUTC.getTime());
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getStartFactGpsId() {
        return startFactGpsId;
    }

    public void setStartFactGpsId(String startFactGpsId) {
        this.startFactGpsId = startFactGpsId;
    }

    public String getEndFactGpsId() {
        return endFactGpsId;
    }

    public void setEndFactGpsId(String endFactGpsId) {
        this.endFactGpsId = endFactGpsId;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    //endregion
}
