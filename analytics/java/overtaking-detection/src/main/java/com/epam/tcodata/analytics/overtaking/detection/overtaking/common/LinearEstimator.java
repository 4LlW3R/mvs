package com.epam.tcodata.analytics.overtaking.detection.overtaking.common;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;

import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.getDiffInSeconds;
import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.haversineDistance;

public class LinearEstimator implements IParameterEstimator {

    @Override
    public double[] findMovementParams(GPSTripPoint[] a) {
        long duration = getDiffInSeconds(a[0].getTime(), a[1].getTime());
        double segmentALength = haversineDistance(a[0].getLatitude(), a[0].getLongitude(),
                a[1].getLatitude(), a[1].getLongitude()) * 1000;
        // f(t) = at + b;
        // t = 0 -> b = 0;
        // t = duration -> length = a*duration
        return new double[]{segmentALength / duration, 0};
    }

    @Override
    public double getDistanceTravelled(double[] params, double t) {
        return params[0] * t;
    }

    @Override
    public double getSpeedAtTime(double[] params, double t) {
        return params[0];
    }
}
