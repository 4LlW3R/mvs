package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.IParameterEstimator;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.sql.Timestamp;
import java.util.Arrays;

import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.getDiffInSeconds;
import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.haversineDistance;

public class VectorStats {
    private final Timestamp startTime;
    private final Timestamp endTime;
    private final long duration;
    private final double[] paramsA;
    private final long startTimeADiff;
    private final double segmentALength;
    private final Vector2D segmentAStart;
    private final Vector2D segmentAEnd;
    private final double[] paramsB;
    private final long startTimeBDiff;
    private final double segmentBLength;
    private final Vector2D segmentBStart;
    private final Vector2D segmentBEnd;
    private final Vector2D vectorA;
    private final Vector2D vectorB;
    private final Timestamp startTimeB;
    private final Timestamp startTimeA;
    private final Timestamp endTimeA;
    private final Timestamp endTimeB;
    private final IParameterEstimator estimator;
    private boolean aSyncedInB;
    private boolean bSyncedInA;

    private VectorStats(GPSTripPoint[] a, GPSTripPoint[] b, IParameterEstimator estimator) {
        this.startTime = a[0].getTime().after(b[0].getTime()) ? a[0].getTime() : b[0].getTime();
        this.endTime = a[1].getTime().before(b[1].getTime()) ? a[1].getTime() : b[1].getTime();

        this.segmentAStart = new Vector2D(a[0].getLatitude(), a[0].getLongitude());
        this.segmentAEnd = new Vector2D(a[1].getLatitude(), a[1].getLongitude());
        this.segmentBStart = new Vector2D(b[0].getLatitude(), b[0].getLongitude());
        this.segmentBEnd = new Vector2D(b[1].getLatitude(), b[1].getLongitude());

        this.duration = getDiffInSeconds(startTime, endTime);

        this.segmentALength = haversineDistance(a[0].getLatitude(), a[0].getLongitude(),
                a[1].getLatitude(), a[1].getLongitude()) * 1000;
        this.segmentBLength = haversineDistance(b[0].getLatitude(), b[0].getLongitude(),
                b[1].getLatitude(), b[1].getLongitude()) * 1000;

        this.paramsA = estimator.findMovementParams(a);
        this.paramsB = estimator.findMovementParams(b);

        this.startTimeADiff = getDiffInSeconds(startTime, a[0].getTime());
        this.startTimeBDiff = getDiffInSeconds(startTime, b[0].getTime());

        this.startTimeA = a[0].getTime();
        this.startTimeB = b[0].getTime();
        this.endTimeA = a[1].getTime();
        this.endTimeB = b[1].getTime();

        this.vectorA = new Vector2D(new double[]{
                a[1].getLatitude() - a[0].getLatitude(),
                a[1].getLongitude() - a[0].getLongitude()
        });

        this.vectorB = new Vector2D(new double[]{
                b[1].getLatitude() - b[0].getLatitude(),
                b[1].getLongitude() - b[0].getLongitude()
        });

        this.estimator = estimator;
    }

    public static VectorStats calculateVectorStats(GPSTripPoint[] a, GPSTripPoint[] b, IParameterEstimator estimator) {
        return new VectorStats(a, b, estimator);
    }

    public Timestamp getStartTime() {
        return new Timestamp(startTime.getTime());
    }

    public Timestamp getEndTime() {
        return new Timestamp(endTime.getTime());
    }

    public long getDuration() {
        return duration;
    }

    public double[] getParamsA() {
        return Arrays.copyOf(paramsA, paramsA.length);
    }

    public long getStartTimeADiff() {
        return startTimeADiff;
    }

    public double getSegmentALength() {
        return segmentALength;
    }

    public Vector2D getSegmentAStart() {
        return segmentAStart;
    }

    public Vector2D getSegmentAEnd() {
        return segmentAEnd;
    }

    public double[] getParamsB() {
        return Arrays.copyOf(paramsB, paramsB.length);
    }

    public long getStartTimeBDiff() {
        return startTimeBDiff;
    }

    public double getSegmentBLength() {
        return segmentBLength;
    }

    public Vector2D getSegmentBStart() {
        return segmentBStart;
    }

    public Vector2D getSegmentBEnd() {
        return segmentBEnd;
    }

    public Vector2D getVectorA() {
        return vectorA;
    }

    public Vector2D getVectorB() {
        return vectorB;
    }

    public IParameterEstimator getEstimator() {
        return estimator;
    }

    public Timestamp getStartTimeB() {
        return new Timestamp(startTimeB.getTime());
    }

    public Timestamp getStartTimeA() {
        return new Timestamp(startTimeA.getTime());
    }

    public Timestamp getEndTimeA() {
        return new Timestamp(endTimeA.getTime());
    }

    public Timestamp getEndTimeB() {
        return new Timestamp(endTimeB.getTime());
    }

    public boolean isaSyncedInB() {
        return aSyncedInB;
    }

    public void setaSyncedInB(boolean aSyncedInB) {
        this.aSyncedInB = aSyncedInB;
    }

    public boolean isbSyncedInA() {
        return bSyncedInA;
    }

    public void setbSyncedInA(boolean bSyncedInA) {
        this.bSyncedInA = bSyncedInA;
    }
}
