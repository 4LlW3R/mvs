package com.epam.tcodata.sql.dal.service.speedlayer;

import com.epam.tcodata.sql.dal.IStorable;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;

import java.util.List;

public interface ISpeedLayerPositionService {

    @SqlBatch("INSERT INTO sl.SpeedLayerPosition VALUES(:observedDay,:durableId,:ingestedDateUtc,:subscriptionId,:lineageCode,:persistedDateUtc,:driverDurableKey,:vehicleDurableKey,:timestamp,:longitude,:latitude,:driverId,:assetId,:positionId,:avl,:source,:odometerKilometres,:ignitionOn,:ageOfReadingSeconds,:pdop,:vdop,:hdop,:numberOfSatellites,:heading,:altitudeMetres,:speedKilometresPerHour,:distanceSinceReadingKilometres,:formattedAddress,:speedLimit)")
    int[] insertBatch(@BindBean List<IStorable> positions);
}
