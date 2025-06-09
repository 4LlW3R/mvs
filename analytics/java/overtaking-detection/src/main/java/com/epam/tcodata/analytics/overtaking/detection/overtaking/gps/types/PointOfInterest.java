package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import java.sql.Timestamp;

import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.haversineDistance;

public class PointOfInterest {
    private final double minimalDistance;
    private final double speedA;
    private final double speedB;
    private final Timestamp time;
    private final double latitude;
    private final double longitude;

    private PointOfInterest(VectorStats stats) {
        double minimalDist = Double.MAX_VALUE;
        double finalSpeedA = 0;
        double finalSpeedB = 0;
        double finalTime = 0;
        double overtakeLatitude = 0;
        double overtakeLongitude = 0;
        for (double s = 0; s < 1; s += 0.005) {
            long currentTime = Utils.addTime(stats.getStartTime(), s * stats.getDuration());
            double progA = stats.getEstimator().getDistanceTravelled(stats.getParamsA(),
                    stats.getStartTimeADiff() + stats.getDuration() * s) / stats.getSegmentALength();
            double progB = stats.getEstimator().getDistanceTravelled(stats.getParamsB(),
                    stats.getStartTimeBDiff() + stats.getDuration() * s) / stats.getSegmentBLength();
            Vector2D pointA = stats.getSegmentAStart().add(progA, stats.getSegmentAEnd().subtract(stats.getSegmentAStart()));
            Vector2D pointB = stats.getSegmentBStart().add(progB, stats.getSegmentBEnd().subtract(stats.getSegmentBStart()));
            double distanceBetweenCars = haversineDistance(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
            if (distanceBetweenCars < minimalDist) {
                minimalDist = distanceBetweenCars;
                overtakeLatitude = pointA.getX();
                overtakeLongitude = pointA.getY();
                finalSpeedA = FastMath.abs(stats.getEstimator().getSpeedAtTime(stats.getParamsA(),
                        stats.getStartTimeADiff() + stats.getDuration() * s));
                finalSpeedB = FastMath.abs(stats.getEstimator().getSpeedAtTime(stats.getParamsB(),
                        stats.getStartTimeBDiff() + stats.getDuration() * s));
                finalTime = currentTime;
            }
        }
        this.minimalDistance = minimalDist;
        this.latitude = overtakeLatitude;
        this.longitude = overtakeLongitude;
        this.speedA = finalSpeedA;
        this.speedB = finalSpeedB;
        this.time = new Timestamp(FastMath.round(finalTime * 1000));
    }

    public static PointOfInterest fromApproximateLawOfMotion(VectorStats stats) {
        return new PointOfInterest(stats);
    }

    //<editor-fold desc="getters-setters">
    public double getMinimalDistance() {
        return minimalDistance;
    }

    public Timestamp getTime() {
        return time == null ? null : new Timestamp(time.getTime());
    }

    public double getSpeedA() {
        return speedA;
    }

    public double getSpeedB() {
        return speedB;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    //</editor-fold>
}
