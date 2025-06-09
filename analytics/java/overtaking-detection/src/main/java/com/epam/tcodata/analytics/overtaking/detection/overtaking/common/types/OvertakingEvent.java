package com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types;

import com.epam.tcodata.storage.avro.entities.events.overtaking.PassingVehicleIdx;
import com.epam.tcodata.storage.events.DomainCode;
import com.epam.tcodata.storage.events.EventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.sql.Timestamp;

public class OvertakingEvent implements Serializable {
    // $subscriptionIDA_$vehicleIDA-$subscriptionIDB_$vehicleIDB_$time-in-milliseconds
    private static final long serialVersionUID = 6121588861270907120L;

    private static final String OVERTAKING_ID_TEMPLATE = "%s_%s_%s";
    private static final double MPS_TO_KMPH = 3.6;
    private static final int NOT_INITIALIZED = -1;
    public static final int NOT_DETECTED = 0;
    public static final int A_OVERTOOK_B = 1;
    public static final int B_OVERTOOK_A = 2;


    private double speedA;
    private double speedB;
    private Timestamp overtakingTime;
    private double overtakingLatitude;
    private double overtakingLongitude;
    private String vehicleDurableIdA;
    private String vehicleDurableIdB;
    private String driverDurableIdA;
    private String driverDurableIdB;
    private OvertakingPathSegment trajectoryA;
    private OvertakingPathSegment trajectoryB;
    private int passingVehicleIndicator = NOT_INITIALIZED;

    public OvertakingEvent() {
    }

    /**
     * All arguments constructor.
     */
    public OvertakingEvent(double speedA,
                           double speedB,
                           Timestamp overtakingTime,
                           double overtakingLatitude,
                           double overtakingLongitude,
                           String vehicleDurableIdA,
                           String vehicleDurableIdB,
                           String driverDurableIdA,
                           String driverDurableIdB,
                           OvertakingPathSegment trajectoryA,
                           OvertakingPathSegment trajectoryB,
                           int passingVehicleId) {
        this.speedA = speedA * MPS_TO_KMPH;
        this.speedB = speedB * MPS_TO_KMPH;
        this.overtakingTime = new Timestamp(overtakingTime.getTime());
        this.overtakingLatitude = overtakingLatitude;
        this.overtakingLongitude = overtakingLongitude;
        this.vehicleDurableIdA = vehicleDurableIdA;
        this.vehicleDurableIdB = vehicleDurableIdB;
        this.driverDurableIdA = driverDurableIdA;
        this.driverDurableIdB = driverDurableIdB;
        this.trajectoryA = trajectoryA;
        this.trajectoryB = trajectoryB;
        this.passingVehicleIndicator = passingVehicleId;
    }

    /**
     * Transform {@code this} to corresponding Avro entity {@link com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro}.
     *
     * @return {@link com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro}
     */
    public com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro toAvro() {
        return com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro.newBuilder()
                .setDomain(DomainCode.DOMAIN_OVERTAKING.getDomain())
                .setEntityType(EventType.OVERTAKING.getEventTypeId())
                .setSchemaVersion(1)
                .setId(getOvertakingId())
                .setAOvertookB(getCorrectIdx(passingVehicleIndicator))
                .setVehicleDurableIdA(vehicleDurableIdA)
                .setVehicleDurableIdB(vehicleDurableIdB)
                .setDriverDurableIdA(driverDurableIdA)
                .setDriverDurableIdB(driverDurableIdB)
                .setVelocityA(speedA)
                .setVelocityB(speedB)
                .setTime(new DateTime(overtakingTime).withZoneRetainFields(DateTimeZone.UTC))
                .setLatitude(overtakingLatitude)
                .setLongitude(overtakingLongitude)
                .setTrajectoryA(trajectoryA.toAvro())
                .setTrajectoryB(trajectoryB.toAvro())
                .build();
    }

    /**
     * TO DO.
     *
     * @param passingVehicleId T ODO
     * @return TO DO
     */
    public PassingVehicleIdx getCorrectIdx(int passingVehicleId) {
        switch (passingVehicleId) {
            case NOT_INITIALIZED:
                return PassingVehicleIdx.NOT_INITIALIZED;
            case NOT_DETECTED:
                return PassingVehicleIdx.NOT_DETECTED;
            case A_OVERTOOK_B:
                return PassingVehicleIdx.A_OVERTOOK_B;
            case B_OVERTOOK_A:
                return PassingVehicleIdx.B_OVERTOOK_A;
            default:
                throw new IllegalArgumentException(
                        "Unsupported Passing Vehicle Idx value: should be one of (-1, 0, 1, 2), actual: "
                                + passingVehicleId);
        }
    }

    /**
     * TO DO.
     *
     * @return TO DO
     */
    public String getOvertakingId() {
        return String.format(OVERTAKING_ID_TEMPLATE,
                vehicleDurableIdA,
                vehicleDurableIdB,
                overtakingTime.getTime());

    }

    public double getSpeedA() {
        return speedA;
    }

    public void setSpeedA(double speedA) {
        this.speedA = speedA;
    }

    public double getSpeedB() {
        return speedB;
    }

    public void setSpeedB(double speedB) {
        this.speedB = speedB;
    }

    public Timestamp getOvertakingTime() {
        return overtakingTime == null ? null : new Timestamp(overtakingTime.getTime());
    }

    public void setOvertakingTime(Timestamp overtakingTime) {
        this.overtakingTime = overtakingTime != null ? new Timestamp(overtakingTime.getTime()) : null;
    }

    public double getOvertakingLatitude() {
        return overtakingLatitude;
    }

    public void setOvertakingLatitude(double overtakingLatitude) {
        this.overtakingLatitude = overtakingLatitude;
    }

    public double getOvertakingLongitude() {
        return overtakingLongitude;
    }

    public void setOvertakingLongitude(double overtakingLongitude) {
        this.overtakingLongitude = overtakingLongitude;
    }

    public String getVehicleDurableIdA() {
        return vehicleDurableIdA;
    }

    public void setVehicleDurableIdA(String vehicleDurableIdA) {
        this.vehicleDurableIdA = vehicleDurableIdA;
    }

    public String getVehicleDurableIdB() {
        return vehicleDurableIdB;
    }

    public void setVehicleDurableIdB(String vehicleDurableIdB) {
        this.vehicleDurableIdB = vehicleDurableIdB;
    }

    public String getDriverDurableIdA() {
        return driverDurableIdA;
    }

    public void setDriverDurableIdA(String driverDurableIdA) {
        this.driverDurableIdA = driverDurableIdA;
    }

    public String getDriverDurableIdB() {
        return driverDurableIdB;
    }

    public void setDriverDurableIdB(String driverDurableIdB) {
        this.driverDurableIdB = driverDurableIdB;
    }

    public OvertakingPathSegment getTrajectoryA() {
        return trajectoryA;
    }

    public void setTrajectoryA(OvertakingPathSegment trajectoryA) {
        this.trajectoryA = trajectoryA;
    }

    public OvertakingPathSegment getTrajectoryB() {
        return trajectoryB;
    }

    public void setTrajectoryB(OvertakingPathSegment trajectoryB) {
        this.trajectoryB = trajectoryB;
    }

    public int getPassingVehicleIndicator() {
        return passingVehicleIndicator;
    }

    public void setPassingVehicleIndicator(int passingVehicleIndicator) {
        this.passingVehicleIndicator = passingVehicleIndicator;
    }
}
