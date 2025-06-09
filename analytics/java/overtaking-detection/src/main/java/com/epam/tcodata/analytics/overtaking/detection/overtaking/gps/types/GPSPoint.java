package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types;

import java.io.Serializable;
import java.sql.Timestamp;

public class GPSPoint implements Serializable {

    private static final long serialVersionUID = 7635726948258753724L;

    private double latitude;
    private double longitude;
    private Timestamp time;
    private double velocity;
    private String vehicleDurableId;
    private String driverDurableId;

    public GPSPoint() {
        /***  Default implementation ***/
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

    public Timestamp getTime() {
        return time == null ? null : new Timestamp(time.getTime());
    }

    public void setTime(Timestamp time) {
        this.time = time != null ? new Timestamp(time.getTime()) : null;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public String getVehicleDurableId() {
        return vehicleDurableId;
    }

    public void setVehicleDurableId(String vehicleDurableId) {
        this.vehicleDurableId = vehicleDurableId;
    }

    public String getDriverDurableId() {
        return driverDurableId;
    }

    public void setDriverDurableId(String driverDurableId) {
        this.driverDurableId = driverDurableId;
    }

    @Override
    public String toString() {
        return "GPSPoint{"
                + "latitude=" + latitude
                + ", longitude=" + longitude
                + ", time=" + time
                + ", velocity=" + velocity
                + ", vehicleDurableId='" + vehicleDurableId + '\''
                + ", driverDurableId='" + driverDurableId + '\''
                + '}';
    }
}
