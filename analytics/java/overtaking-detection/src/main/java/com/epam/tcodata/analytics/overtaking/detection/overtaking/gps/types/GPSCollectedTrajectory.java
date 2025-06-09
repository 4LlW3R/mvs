package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;

import java.io.Serializable;
import java.util.Arrays;

public class GPSCollectedTrajectory implements Serializable {

    private static final long serialVersionUID = -5470746486147541306L;

    private GPSTripPoint[] trajectory;
    private String vehicleDurableId;


    public GPSCollectedTrajectory() {
        /***  Default implementation ***/
    }

    public GPSTripPoint[] getTrajectory() {
        return trajectory == null ? null : Arrays.copyOf(trajectory, trajectory.length);
    }

    public void setTrajectory(GPSTripPoint[] trajectory) {
        this.trajectory = trajectory == null ? null : Arrays.copyOf(trajectory, trajectory.length);
    }

    public String getVehicleDurableId() {
        return vehicleDurableId;
    }

    public void setVehicleDurableId(String vehicleDurableId) {
        this.vehicleDurableId = vehicleDurableId;
    }
}
