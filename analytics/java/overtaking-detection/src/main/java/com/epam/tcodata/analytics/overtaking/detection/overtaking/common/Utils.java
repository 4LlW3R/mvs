package com.epam.tcodata.analytics.overtaking.detection.overtaking.common;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.VectorStats;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.sql.Timestamp;

public abstract class Utils {
    private Utils(){}
    private static final short EARTH_RADIUS = 6371;

    /**
     * Founds difference between two timestamps in seconds.
     */
    public static long getDiffInSeconds(Timestamp ts1, Timestamp ts2) {
        long t1 = ts1.getTime();
        long t2 = ts2.getTime();
        return Math.abs(t2 - t1) / 1000;
    }

    /**
     * Calculates haversine distance between two points on a map.
     */
    public static double haversineDistance(double latA, double longA, double latB, double longB) {
        double latDistance = Math.toRadians(latB - latA);
        double longDistance = Math.toRadians(longB - longA);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latA)) * Math.cos(Math.toRadians(latB))
                * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * Returns new timestamp in milliseconds which is shifted from the input on {@code shift}.
     *
     * @param startTime - initial timestamp
     * @param shift     - amount of shift (double cause calculates in percentage, e.g. 5% of whole duration)
     * @return shifted timestamp
     */
    public static long addTime(Timestamp startTime, double shift) {
        return startTime.getTime() / 1000 + (long) shift;
    }

    /**
     * Checks if direction changed using vectors projection on X and Y axes.
     *
     * @param stats - {@link VectorStats} instance
     * @return true if one vector overlaps other in X or Y projection.
     */
    public static boolean didDirectionChanged(VectorStats stats) {
        boolean overlappingOnX = isOverlappingForXProjection(stats.getSegmentAStart(),
                stats.getSegmentBStart(), stats.getSegmentAEnd(), stats.getSegmentBEnd());
        boolean overlappingOnY = isOverlappingForYProjection(stats.getSegmentAStart(),
                stats.getSegmentBStart(), stats.getSegmentAEnd(), stats.getSegmentBEnd());
        return overlappingOnX || overlappingOnY;
    }

    private static boolean isOverlappingForXProjection(Vector2D segmentAStart, Vector2D segmentBStart,
                                                       Vector2D segmentAEnd, Vector2D segmentBEnd) {
        boolean bInA = segmentAStart.getX() <= segmentBStart.getX() && segmentAEnd.getX() > segmentBEnd.getX();
        boolean aInB = segmentBStart.getX() <= segmentAStart.getX() && segmentBEnd.getX() > segmentAEnd.getX();
        return bInA || aInB;
    }

    private static boolean isOverlappingForYProjection(Vector2D segmentAStart, Vector2D segmentBStart,
                                                       Vector2D segmentAEnd, Vector2D segmentBEnd) {
        boolean bInA = segmentAStart.getY() <= segmentBStart.getY() && segmentAEnd.getY() > segmentBEnd.getY();
        boolean aInB = segmentBStart.getY() <= segmentAStart.getY() && segmentBEnd.getY() > segmentAEnd.getY();
        return bInA || aInB;
    }



}
