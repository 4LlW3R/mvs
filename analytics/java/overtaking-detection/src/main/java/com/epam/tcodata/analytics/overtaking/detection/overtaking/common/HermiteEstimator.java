package com.epam.tcodata.analytics.overtaking.detection.overtaking.common;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;

import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.getDiffInSeconds;
import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.haversineDistance;

public class HermiteEstimator implements IParameterEstimator {
    @Override
    public double[] findMovementParams(GPSTripPoint[] segment) {
        long t = getDiffInSeconds(segment[0].getTime(), segment[1].getTime());
        double l = haversineDistance(segment[0].getLatitude(), segment[0].getLongitude(),
                segment[1].getLatitude(), segment[1].getLongitude()) * 1000;
        double v1 = segment[0].getVelocity();
        double v2 = segment[1].getVelocity();
        double det = -t * t * t * t * 1.00;
        double adet = 2 * t * l - v1 * t * t - v2 * t * t;
        double bdet = -3 * l * t * t + 2 * t * t * t * v1 + t * t * t * v2;
        double cdet = -t * t * t * t * v1;

        return new double[] {
                adet / det,
                bdet / det,
                cdet / det,
                0
        };
    }

    @Override
    public double getDistanceTravelled(double[] params, double t) {
        return t * t * t * params[0] + t * t * params[1] + t * params[2] + params[3];
    }

    @Override
    public double getSpeedAtTime(double[] params, double t) {
        return 3 * t * t * params[0] + 2 * t * params[1] + params[2];
    }
}
