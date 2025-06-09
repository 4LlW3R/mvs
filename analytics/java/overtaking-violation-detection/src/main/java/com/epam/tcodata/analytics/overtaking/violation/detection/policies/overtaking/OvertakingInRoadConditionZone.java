package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.GeoUtils;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.RoadConditionArea;

import java.util.List;
import java.util.function.Predicate;

public class OvertakingInRoadConditionZone implements IPolicy<OvertakingMetaData> {

    private static final long serialVersionUID = 6252773761096296924L;
    private List<RoadConditionArea> roadConditionAreas;

    public OvertakingInRoadConditionZone(List<RoadConditionArea> roadConditionAreas) {
        this.roadConditionAreas = roadConditionAreas;
    }

    @Override
    public Violation applyPolicy(OvertakingMetaData entity) {
        double lon = entity.getEvent().getLongitude();
        double lat = entity.getEvent().getLatitude();
        long pointTs = entity.getEvent().getTime().getMillis();
        return roadConditionAreas
                .stream()
                .filter(conditionContainsEvent(lon, lat, pointTs))
                .findFirst()
                .map(x -> new Violation(x.getUuid(), Violation.Type.OVERTAKING_DURING_ROAD_CONDITION))
                .orElse(Violation.NO_VIOLATION);
    }

    private Predicate<RoadConditionArea> conditionContainsEvent(double lon, double lat, long ts) {
        return area -> {
            boolean areaContainsPoint = area.getGeometries().stream().anyMatch(g -> GeoUtils.pointIsInArea(lon, lat, g));

            boolean pointAfterConditionStarted = ts > area.getStartDateTime().getMillis();
            boolean pointBeforeConditionEnded = area.getEndDateTime() == null
                    || ts < area.getEndDateTime().getMillis();
            return areaContainsPoint && pointAfterConditionStarted && pointBeforeConditionEnded;
        };
    }
}
