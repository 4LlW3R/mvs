package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import org.joda.time.DateTimeZone;

public class OvertakingDuringCommuteHoursPolicy implements IPolicy<OvertakingMetaData> {
    private static final int COMMUTE_HOURS_AM_BEGIN = 5;
    private static final int COMMUTE_HOURS_AM_END = 7;
    private static final int COMMUTE_HOURS_PM_BEGIN = 17;
    private static final int COMMUTE_HOURS_PM_END = 20;

    private static Violation checkCommuteHours(OvertakingEventAvro event) {
        int hour = event.getTime()
                           .withZone(DateTimeZone.forTimeZone(Utils.TENGIZ_TIMEZONE))
                           .getHourOfDay();
        if (hour > COMMUTE_HOURS_AM_BEGIN && hour < COMMUTE_HOURS_AM_END
                    || hour > COMMUTE_HOURS_PM_BEGIN && hour < COMMUTE_HOURS_PM_END) {
            return new Violation(Violation.Type.COMMUTE_HOURS_OVERTAKING);
        }
        return Violation.NO_VIOLATION;
    }

    @Override
    public Violation applyPolicy(OvertakingMetaData entity) {
        if (entity.isSlowMovingVehiclePresent()) {
            return Violation.NO_VIOLATION;
        } else {
            return checkCommuteHours(entity.getEvent());
        }
    }
}
