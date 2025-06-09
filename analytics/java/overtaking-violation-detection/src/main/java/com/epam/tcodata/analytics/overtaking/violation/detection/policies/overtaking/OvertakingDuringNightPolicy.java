package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.util.Calendar;

import static com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.Utils.TENGIZ_TIMEZONE;

public class OvertakingDuringNightPolicy implements IPolicy<OvertakingMetaData> {

    private Violation checkNightTime(OvertakingEventAvro event) {
        Location location = new Location(event.getLatitude(), event.getLongitude());
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TENGIZ_TIMEZONE);
        Calendar overtakingTime = Calendar.getInstance();
        overtakingTime.setTimeInMillis(event.getTime().getMillis());
        overtakingTime.setTimeZone(TENGIZ_TIMEZONE);
        int overtakingHour = overtakingTime.get(Calendar.HOUR_OF_DAY);
        int sunriseHour = calculator.getOfficialSunriseCalendarForDate(overtakingTime).get(Calendar.HOUR_OF_DAY);
        int sunsetHour = calculator.getOfficialSunsetCalendarForDate(overtakingTime).get(Calendar.HOUR_OF_DAY);
        return (overtakingHour < sunriseHour) || (overtakingHour > sunsetHour)
                       ? new Violation(Violation.Type.NIGHT_TIME_OVERTAKING)
                       : Violation.NO_VIOLATION;

    }


    @Override
    public Violation applyPolicy(OvertakingMetaData entity) {
        if (entity.isSlowMovingVehiclePresent()) {
            return Violation.NO_VIOLATION;
        } else {
            return checkNightTime(entity.getEvent());
        }
    }
}
