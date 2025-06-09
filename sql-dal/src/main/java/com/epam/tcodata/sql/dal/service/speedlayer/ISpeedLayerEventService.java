package com.epam.tcodata.sql.dal.service.speedlayer;

import com.epam.tcodata.sql.dal.IStorable;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;

import java.util.List;

public interface ISpeedLayerEventService {

    @SqlBatch("INSERT INTO sl.SpeedLayerEvent VALUES(:observedDay,:durableId,:ingestedDateUtc,:subscriptionId,:lineageCode,:persistedDateUtc,:driverDurableKey,:vehicleDurableKey,:totalOccurrences,:totalTimeSeconds,:eventTypeId,:eventId,:driverId,:assetId,:value,:endDateTime,:startDateTime,:eventCategory,:startOdometerKilometres,:startPositionTimestamp,:startPositionLongitude,:startPositionLatitude,:startPositionPositionId,:startPositionSpeedKilometresPerHour,:endOdometerKilometres,:endPositionTimestamp,:endPositionLongitude,:endPositionLatitude,:endPositionPositionId,:endPositionSpeedKilometresPerHour,:valueType,:valueUnits,:mediaUrlsRoad,:mediaUrlsCab,:mediaUrlsCamera3,:mediaUrlsCamera4,:locationId,:speedLimit)")
    int[] insertBatch(@BindBean List<IStorable> events);
}
