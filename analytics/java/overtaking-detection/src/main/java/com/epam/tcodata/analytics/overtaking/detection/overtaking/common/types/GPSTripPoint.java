package com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 * I.e. compareTo method make comparison only by time.
 */
@SuppressFBWarnings("EQ_COMPARETO_USE_OBJECT_EQUALS")
public class GPSTripPoint implements Serializable, Comparable<GPSTripPoint> {
    private static final long serialVersionUID = -4497603150379152764L;

    private Timestamp time;
    private double latitude;
    private double longitude;
    private double velocity;
    private String driverDurableId;

    /**
     * All arguments constructor for entity. This is used for aggregation with trips, so subsID and vehicleID excluded
     * from parent class {@link com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSPoint}
     *
     * @param time            parent timestamp
     * @param latitude        parent latitude
     * @param longitude       parent longitude
     * @param velocity        parent velocity
     * @param driverDurableId parent driverID
     */
    public GPSTripPoint(Timestamp time, double latitude, double longitude, double velocity, String driverDurableId) {
        this.time = new Timestamp(time.getTime());
        this.latitude = latitude;
        this.longitude = longitude;
        // to m/s
        this.velocity = velocity / 3.6;
        this.driverDurableId = driverDurableId;
    }

    public GPSTripPoint() {
        /***  Default implementation ***/
    }

    public Timestamp getTime() {
        return time == null ? null : new Timestamp(time.getTime());
    }

    public void setTime(Timestamp time) {
        this.time = time == null ? null : new Timestamp(time.getTime());
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public int compareTo(GPSTripPoint o) {
        if (this.time == null && o == null)
            return 0;
        else if (this.time != null && o == null)
            return 1;
        else if (this.time == null)
            return -1;
        else
            return this.time.compareTo(o.time);
    }

    public String getDriverDurableId() {
        return driverDurableId;
    }

    public void setDriverDurableId(String driverDurableId) {
        this.driverDurableId = driverDurableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GPSTripPoint that = (GPSTripPoint) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Double.compare(that.velocity, velocity) == 0 && Objects.equals(time, that.time) && Objects.equals(driverDurableId, that.driverDurableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, latitude, longitude, velocity, driverDurableId);
    }
}
