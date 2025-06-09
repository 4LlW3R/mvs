package com.epam.tcodata.analytics.overtaking.detection.overtaking.common;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;

public interface IParameterEstimator {
    double[] findMovementParams(GPSTripPoint[] a);
    double getDistanceTravelled(double[] params, double t);
    double getSpeedAtTime(double[] params, double t);
}
