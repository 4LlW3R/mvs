package com.epam.tcodata.analytics.overtaking.violation.detection.entities;

import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.SpeedZoneArea;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.Utils;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class OvertakingMetaData implements Serializable {
    private static final long serialVersionUID = 802984239662203009L;
    private OvertakingEventAvro event;
    private boolean isSlowMovingVehiclePresent;
    private SpeedZoneArea speedZoneArea;

    public OvertakingMetaData() {
    }

    /**
     * Class contains useful meta data for overtaking violations detection, stored to not run spatial queries several times.
     *
     * @param event - {@link OvertakingEventAvro}
     * @param speedZoneAreas - speed limit zones ({@link SpeedZoneArea}) from Redis
     */
    public OvertakingMetaData(OvertakingEventAvro event, List<SpeedZoneArea> speedZoneAreas) {
        this.event = event;
        this.speedZoneArea = Utils.getCorrectSpeedZone(event.getLongitude(), event.getLatitude(), speedZoneAreas);
        this.isSlowMovingVehiclePresent = Utils.slowMovingVehicleIsPresent(event, speedZoneArea.getSpeedLimit());
    }

    public OvertakingEventAvro getEvent() {
        return event;
    }

    public void setEvent(OvertakingEventAvro event) {
        this.event = event;
    }

    public boolean isSlowMovingVehiclePresent() {
        return isSlowMovingVehiclePresent;
    }

    public void setSlowMovingVehiclePresent(boolean slowMovingVehiclePresent) {
        isSlowMovingVehiclePresent = slowMovingVehiclePresent;
    }

    public SpeedZoneArea getSpeedZoneArea() {
        return speedZoneArea;
    }

    public void setSpeedZoneArea(SpeedZoneArea speedZoneArea) {
        this.speedZoneArea = speedZoneArea;
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
