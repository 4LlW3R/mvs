package com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.sql.Timestamp;

public class OvertakingPathSegment implements Serializable {
    private static final long serialVersionUID = -6621269671248195356L;

    private double startLatitude;
    private double startLongitude;
    private Timestamp startTime;
    private double endLatitude;
    private double endLongitude;
    private Timestamp endTime;

    public OvertakingPathSegment() {
    }

    /**
     * All arguments constructor.
     */
    public OvertakingPathSegment(double startLatitude, double startLongitude, Timestamp startTime, double endLatitude, double endLongitude, Timestamp endTime) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.startTime = new Timestamp(startTime.getTime());
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.endTime = new Timestamp(endTime.getTime());
    }

    /**
     * Constructor from array of segment ends.
     *
     * @param segment - two points array of {@link GPSTripPoint}
     */
    public OvertakingPathSegment(GPSTripPoint[] segment) {
        this(
                segment[0].getLatitude(),
                segment[0].getLongitude(),
                segment[0].getTime(),
                segment[1].getLatitude(),
                segment[1].getLongitude(),
                segment[1].getTime());
    }

    /**
     * Transforms to corresponding Avro entity.
     *
     * @return {@link com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingPathSegmentAvro}
     */
    public com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingPathSegmentAvro toAvro() {
        return com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingPathSegmentAvro.newBuilder()
                .setStartLatitude(startLatitude)
                .setStartLongitude(startLongitude)
                .setStartTime(new DateTime(startTime).withZoneRetainFields(DateTimeZone.UTC))
                .setEndLatitude(endLatitude)
                .setEndLongitude(endLongitude)
                .setEndTime(new DateTime(endTime).withZoneRetainFields(DateTimeZone.UTC))
                .build();
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

    public Timestamp getStartTime() {
        return startTime == null ? null : new Timestamp(startTime.getTime());
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime == null ? null : new Timestamp(startTime.getTime());
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

    public Timestamp getEndTime() {
        return endTime == null ? null : new Timestamp(endTime.getTime());
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime == null ? null : new Timestamp(endTime.getTime());
    }
}

