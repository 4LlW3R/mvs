package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.OperatorBuffer;
import com.esri.core.geometry.OperatorDisjoint;
import com.esri.core.geometry.OperatorDistance;
import com.esri.core.geometry.OperatorProximity2D;
import com.esri.core.geometry.OperatorSimpleRelation;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class GeoUtils {
    private GeoUtils(){}

    public static final SpatialReference DEFAULT_SPATIAL_REFERENCE = SpatialReference.create(4326);
    public static final double ONE_METER_IN_DEGREES = 1.0 / 111159.0;
    public static final double LINE_BUFFER_RADIUS = 10.0 * ONE_METER_IN_DEGREES;
    private static final OperatorSimpleRelation DISJOINT_OP = OperatorDisjoint.local();
    private static final OperatorBuffer BUFFER_OP = OperatorBuffer.local();
    private static final OperatorProximity2D PROXIMITY_OP = OperatorProximity2D.local();
    private static final Logger LOGGER = LoggerFactory.getLogger(GeoUtils.class);

    /**
     * Identifies if the given point is in given area or on its bounday.
     *
     * @param lat  - latitude
     * @param lon  - longitude
     * @param area - {@link Geometry} object
     * @return true of point is in area, false otherwise
     */
    public static boolean pointIsInArea(double lon, double lat, Geometry area) {
        Point point = new Point(lon, lat);
        return pointIsInArea(point, area);
    }

    /**
     * Identifies if the given point is in given area or on its bounday.
     *
     * @param area - {@link Geometry} object
     * @return true of point is in area, false otherwise
     */
    public static boolean pointIsInArea(Point point, Geometry area) {
        return !DISJOINT_OP.execute(area, point, DEFAULT_SPATIAL_REFERENCE, null);
    }

    /**
     * Creates new geometry with buffer of specified radius.
     *
     * @param geom   - initial geometry
     * @param radius - radius in decimal degrees
     * @param ref    - {@link SpatialReference}
     * @return buffered geometry
     */
    public static Geometry bufferGeometry(Geometry geom, double radius, SpatialReference ref) {
        return BUFFER_OP.execute(geom, ref, radius, null);
    }

    /**
     * Call to {@link GeoUtils#bufferGeometry} with default params: radius is {@link GeoUtils#LINE_BUFFER_RADIUS},
     * spatial reference is {@link GeoUtils#DEFAULT_SPATIAL_REFERENCE}.
     */
    public static Geometry bufferGeometry(Geometry geom) {
        return BUFFER_OP.execute(geom, DEFAULT_SPATIAL_REFERENCE, LINE_BUFFER_RADIUS, null);
    }

    /**
     * Compares two directed segments mpA and mpB direction with geometry direction, snapping them to geometry first,
     * if possible.
     * If geometry is not directed (i.e. it is not a polyline) false is returned.
     *
     * @param closestLine - geometry
     * @param mpA - start and end of first segment
     * @param mpB - start and end of second segment
     * @return true if all three are directed equally
     */
    public static boolean isTheSameDirection(Geometry closestLine, MultiPoint mpA, MultiPoint mpB) {
        int[] snappedA = snapToRoad(closestLine, mpA);
        int[] snappedB = snapToRoad(closestLine, mpB);

        boolean sameDirectionWithA = checkDirection(snappedA, closestLine);
        boolean sameDirectionWithB = checkDirection(snappedB, closestLine);

        return sameDirectionWithA && sameDirectionWithB;
    }

    /**
     * Checks snapped geometries (i.e. disjoint operation on those geometries will return false) direction.
     *
     * @return true if geometry is polyline and directed the same as the points, else false.
     */
    public static boolean checkDirection(int[] vertices, Geometry geometry) {
        if (geometry instanceof Polyline) {
            Polyline line = (Polyline) geometry;
            double l1 = getDistanceToVertexFromPathStart(vertices[0], line);
            double l2 = getDistanceToVertexFromPathStart(vertices[1], line);
            return l2 > l1;
        } else {
            LOGGER.debug("Can't check direction of a non directed geometry.");
            return false;
        }
    }

    /**
     * Snaps multipoint of two to polyline.
     *
     */
    public static int[] snapToRoad(Geometry geometry, MultiPoint mp) {
        int[] result = new int[2];
        result[0] = PROXIMITY_OP.getNearestCoordinate(geometry, mp.getPoint(0), false).getVertexIndex();
        result[1] = PROXIMITY_OP.getNearestCoordinate(geometry, mp.getPoint(1), false).getVertexIndex();
        return result;
    }

    private static double getDistanceToVertexFromPathStart(int vertex, Polyline line) {
        if (vertex == 0) {
            return 0;
        } else if (vertex == line.getPointCount() - 1) return line.calculateLength2D();
        else {
            Polyline segmented = new Polyline();
            segmented.addSegmentsFromPath(line, 0, 0, vertex, true);
            return segmented.calculatePathLength2D(0) / GeoUtils.ONE_METER_IN_DEGREES;
        }
    }

    private static double meanDistanceFromSegmentEndsToGeometry(Geometry geometry, MultiPoint mp) {
        OperatorDistance dist = OperatorDistance.local();
        double distanceFromStart = dist.execute(geometry, mp.getPoint(0), null);
        double distanceFromEnd = dist.execute(geometry, mp.getPoint(0), null);
        return (distanceFromStart + distanceFromEnd) / 2;
    }

    /**
     * Geometry comparator that finds closest geometry to a given pair of segments (multipoints of two).
     */
    public static Comparator<Geometry> compareMeanDistancesFromPoints(MultiPoint a, MultiPoint b) {
        return (g1, g2) -> {
            double meanDistFromAToO1 = GeoUtils.meanDistanceFromSegmentEndsToGeometry(g1, a);
            double meanDistFromAToO2 = GeoUtils.meanDistanceFromSegmentEndsToGeometry(g2, a);
            double meanDistFromBToO1 = GeoUtils.meanDistanceFromSegmentEndsToGeometry(g1, b);
            double meanDistFromBToO2 = GeoUtils.meanDistanceFromSegmentEndsToGeometry(g2, b);
            return Double.compare(meanDistFromAToO1 / 2 + meanDistFromBToO1 / 2,
                    meanDistFromAToO2 / 2 + meanDistFromBToO2 / 2);
        };
    }
}
