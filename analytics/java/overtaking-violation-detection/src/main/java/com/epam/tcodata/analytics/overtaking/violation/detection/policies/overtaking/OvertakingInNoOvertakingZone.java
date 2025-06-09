package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.Area;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.GeoUtils;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.NoOvertakingArea;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OvertakingInNoOvertakingZone implements IPolicy<OvertakingMetaData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OvertakingInNoOvertakingZone.class);
    private List<NoOvertakingArea> noOvertakingPolygons;
    private List<NoOvertakingArea> noOvertakingLines;

    public OvertakingInNoOvertakingZone() {
    }

    /**
     * Overtaking violation in no overtaking zone.
     * Incapsulates logic of mapping point to road and calculation of direction.
     *
     * @param noOvertakingZones - list of {@link NoOvertakingArea} from {@link com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.AreasLookup}
     */
    public OvertakingInNoOvertakingZone(List<NoOvertakingArea> noOvertakingZones) {
        this.noOvertakingLines = noOvertakingZones
                .stream()
                .filter(NoOvertakingArea::hasDirection)
                .collect(Collectors.toList());
        this.noOvertakingPolygons = noOvertakingZones
                .stream()
                .filter(area -> !area.hasDirection())
                .collect(Collectors.toList());
    }


    @Override
    public Violation applyPolicy(OvertakingMetaData entity) {
        return checkZoneForViolation(entity);
    }

    private Violation checkZoneForViolation(OvertakingMetaData entity) {
        String zoneId = getNoOvertakingZone(entity.getEvent());
        boolean notInNoOvertakingZone = zoneId.equals(Area.AREA_WITHOUT_RESTRICTION.getId());
        return notInNoOvertakingZone ? Violation.NO_VIOLATION : new Violation(zoneId, Violation.Type.NO_OVERTAKING_ZONE_OVERTAKING);
    }

    private String getNoOvertakingZone(OvertakingEventAvro entity) {
        NoOvertakingArea result = null;
        for (NoOvertakingArea polygon : noOvertakingPolygons) {
            if (GeoUtils.pointIsInArea(entity.getLongitude(), entity.getLatitude(), polygon.getGeometry())) {
                result = polygon;
                break;
            }
        }

        if (result == null) {
            LOGGER.debug("Overtaking is not in any of no overtaking polygon zone. Checking one sided roads...");
            return mapToRoad(entity);
        } else {
            return result.getId();
        }
    }

    private String mapToRoad(OvertakingEventAvro entity) {
        List<NoOvertakingArea> lines = this.noOvertakingLines
                .stream()
                .filter(areaContainsPoint(entity))
                .collect(Collectors.toList());
        if (lines.isEmpty()) {
            LOGGER.debug("Overtaking is not in any of no overtaking zone");
            return Area.AREA_WITHOUT_RESTRICTION.getId();
        } else {
            Area correctSegment = getSegmentWithSameDirection(entity, lines);
            return correctSegment.getId();
        }
    }

    private Predicate<Area> areaContainsPoint(OvertakingEventAvro entity) {
        return a -> {
            Geometry geom = a.getGeometry();
            Geometry buffer = GeoUtils.bufferGeometry(geom, GeoUtils.LINE_BUFFER_RADIUS, GeoUtils.DEFAULT_SPATIAL_REFERENCE);
            return Arrays
                    .stream(getCoords(entity))
                    .anyMatch(p -> GeoUtils.pointIsInArea(p[0], p[1], buffer));
        };
    }

    private Area getSegmentWithSameDirection(OvertakingEventAvro entity, List<NoOvertakingArea> lines) {
        MultiPoint mpA = new MultiPoint();
        mpA.add(entity.getTrajectoryA().getStartLongitude(), entity.getTrajectoryA().getStartLatitude());
        mpA.add(entity.getTrajectoryA().getEndLongitude(), entity.getTrajectoryA().getEndLatitude());

        MultiPoint mpB = new MultiPoint();
        mpB.add(entity.getTrajectoryB().getStartLongitude(), entity.getTrajectoryB().getStartLatitude());
        mpB.add(entity.getTrajectoryB().getEndLongitude(), entity.getTrajectoryB().getEndLatitude());

        // to choose correct segment, calculate mean distance from both segments to lines
        // and chose line with minimal distance
        NoOvertakingArea closestLine = lines
                .stream()
                .min(compareMeanDistanceToPoints(mpA, mpB))
                .orElse(NoOvertakingArea.getDefaultArea());

        return GeoUtils.isTheSameDirection(closestLine.getGeometry(), mpA, mpB) ? closestLine : Area.AREA_WITHOUT_RESTRICTION;
    }

    private Comparator<NoOvertakingArea> compareMeanDistanceToPoints(MultiPoint a, MultiPoint b) {
        return Comparator.comparing(NoOvertakingArea::getGeometry, GeoUtils.compareMeanDistancesFromPoints(a, b));
    }

    private double[][] getCoords(OvertakingEventAvro entity) {
        double[][] coords = new double[4][];
        coords[0] = new double[] {entity.getTrajectoryA().getStartLongitude(), entity.getTrajectoryA().getStartLatitude()};
        coords[1] = new double[] {entity.getTrajectoryA().getEndLongitude(), entity.getTrajectoryA().getEndLatitude()};
        coords[2] = new double[] {entity.getTrajectoryB().getStartLongitude(), entity.getTrajectoryB().getStartLatitude()};
        coords[3] = new double[] {entity.getTrajectoryB().getEndLongitude(), entity.getTrajectoryB().getEndLatitude()};
        return coords;
    }

    public List<NoOvertakingArea> getNoOvertakingPolygons() {
        return noOvertakingPolygons;
    }

    public void setNoOvertakingPolygons(List<NoOvertakingArea> noOvertakingPolygons) {
        this.noOvertakingPolygons = noOvertakingPolygons;
    }

    public List<NoOvertakingArea> getNoOvertakingLines() {
        return noOvertakingLines;
    }

    public void setNoOvertakingLines(List<NoOvertakingArea> noOvertakingLines) {
        this.noOvertakingLines = noOvertakingLines;
    }
}
