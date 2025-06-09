package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.HermiteEstimator;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingPathSegment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.getDiffInSeconds;
import static com.epam.tcodata.analytics.overtaking.detection.overtaking.common.Utils.haversineDistance;

public class GPSTrajectory implements Serializable {
    private static final long serialVersionUID = -5847798228010925555L;

    private static final GPSTripPoint[][] EMPTY_TRIP = {};
    // Threshold for consecutive pairs of points. If the difference is higher,
    // then there was a 0 velocity points in between, so no real overtaking case
    // could have happened.
    private static final long TWO_MINUTES_IN_SECONDS = 120L;
    // $vehicleID_$subscriptionID
    private static final String TRIP_ID_TEMPLATE = "%s_%s";
    private static final double LOW_SPEED_BOUND = 0.1;
    private static final double SPEED_DIFFERENCE_LOWER_BOUND = 0.15;
    private static final double SPEED_SCALER_FOR_DISTANCE = 0.01;
    private static final double MAX_ANGLE_FOR_MOVING_IN_SAME_DIRECTION_IN_RADIANS = 0.15;
    private static final double MAX_REASONABLE_SPEED = 120 / 3.6;
    private static final long THREE_MS_IN_SEC = 3 * 1000L;
    // 70 meters
    private static final double MINIMAL_COMMON_DISTANCE = 7e-3;
    private static final double MINIMAL_SEGMENT_LENTH_METERS = 30;


    private GPSTripPoint[][] data;
    private String vehicleDurableId;
    private String driverDurableId;
    private Timestamp tripStart;
    private Timestamp tripEnd;

    public GPSTrajectory() {
    }

    /**
     * Creates GPSTrajectory associated with subsID and vehicleID.
     * Stores all points for that trajectory in time-sorted order.
     */
    public GPSTrajectory(GPSTripPoint[] points, String vehicleDurableId) {
        this.vehicleDurableId = vehicleDurableId;
        if (points.length == 0) {
            this.data = EMPTY_TRIP;
        } else {
            this.data = new GPSTripPoint[points.length][];
            this.driverDurableId = points[0].getDriverDurableId();
            // aggregating points in time-sorted order to make
            // pairwise comparison with time
            Arrays.sort(points);
            for (int i = 0; i < points.length - 1; i++) {
                long diff = getDiffInSeconds(points[i].getTime(), points[i + 1].getTime());
                if (diff < TWO_MINUTES_IN_SECONDS && diff > 0)
                    this.data[i] = new GPSTripPoint[] {points[i], points[i + 1]};
            }
            this.tripStart = points[0].getTime();
            this.tripEnd = points[points.length - 1].getTime();
        }
    }

    /**
     * Main overtaking detection routine - checks if two trajectories had an overtaking event (events).
     * Basically, it is applying several heuristic filters to the data.
     */
    public List<OvertakingEvent> findOvertake(GPSTrajectory other) {
        ArrayList<OvertakingEvent> ovtEvents = new ArrayList<>();
        if (this.data == EMPTY_TRIP || other.data == EMPTY_TRIP)
            return ovtEvents;

        String tripA = String.format(TRIP_ID_TEMPLATE, this.vehicleDurableId, this.driverDurableId);
        String tripB = String.format(TRIP_ID_TEMPLATE, other.vehicleDurableId, other.driverDurableId);
        if (tripA.equals(tripB)) {
            return ovtEvents;
        }
        if (FastMath.max(this.tripStart.getTime(), other.tripStart.getTime())
                    > FastMath.min(this.tripEnd.getTime(), other.tripEnd.getTime())) {
            return ovtEvents;
        }
        ovtEvents = findOvtEvents(other, ovtEvents);
        // aggregate consecutive overtakings to eliminate possible duplicates
        return aggregateConsecutiveEvents(ovtEvents);
    }
    private ArrayList<OvertakingEvent> findOvtEvents(GPSTrajectory other, ArrayList<OvertakingEvent> ovtEvents) {
        for (GPSTripPoint[] a : this.data) {
            for (GPSTripPoint[] b : other.data) {

                if (a == null || b == null) continue;

                VectorStats currentStats = VectorStats.calculateVectorStats(a, b, new HermiteEstimator());

                if (currentStats.getSegmentALength() <= MINIMAL_SEGMENT_LENTH_METERS
                        || currentStats.getSegmentBLength() <= MINIMAL_SEGMENT_LENTH_METERS
                        || currentStats.getParamsA().length == 0 || currentStats.getParamsB().length == 0
                        || !(currentStats.getStartTime().before(currentStats.getEndTime()))
                        || drivingOppositeDirection(currentStats)
                ) continue;

                PointOfInterest poi = PointOfInterest.fromApproximateLawOfMotion(currentStats);

                if (minimalDistanceIsTooLarge(poi)
                        || speedDifferenceIsTooSmall(poi)
                        || onesSpeedIsTooLow(poi)
                ) continue;

                long durationA = currentStats.getEndTimeA().getTime() - currentStats.getStartTimeA().getTime();
                long durationB = currentStats.getEndTimeB().getTime() - currentStats.getStartTimeB().getTime();

                if (durationA < THREE_MS_IN_SEC || durationB < THREE_MS_IN_SEC
                        || !trajectoriesIntersects(currentStats)
                ) continue;

                boolean checkProjsAOnB = checkProjectionsWithSyncUp(currentStats);

                if (!(checkProjsAOnB))
                    continue;

                double speedA = fixSpeed(a, poi.getSpeedA());
                double speedB = fixSpeed(b, poi.getSpeedB());

                int idx = calculateIdx(speedA, speedB, currentStats);

                OvertakingEvent event = new OvertakingEvent(
                        speedA,
                        speedB,
                        poi.getTime(),
                        poi.getLatitude(),
                        poi.getLongitude(),
                        this.vehicleDurableId,
                        other.vehicleDurableId,
                        this.driverDurableId,
                        other.driverDurableId,
                        new OvertakingPathSegment(a),
                        new OvertakingPathSegment(b),
                        idx);
                ovtEvents.add(event);
            }
        }
        return ovtEvents;
    }

    private int calculateIdx(double speedA, double speedB, VectorStats currentStats) {
        if (speedA > speedB && currentStats.isbSyncedInA()) {
            return OvertakingEvent.A_OVERTOOK_B;
        } else if (speedB > speedA && currentStats.isaSyncedInB()) {
            return OvertakingEvent.B_OVERTOOK_A;
        } else {
            return OvertakingEvent.NOT_DETECTED;
        }
    }

    private boolean trajectoriesIntersects(VectorStats stats) {
        boolean aStartProjectionOnB = pointProjectionIsInVector(stats.getSegmentAStart(), stats.getSegmentBStart(), stats.getSegmentBEnd());
        boolean aEndProjectionOnB = pointProjectionIsInVector(stats.getSegmentAEnd(), stats.getSegmentBStart(), stats.getSegmentBEnd());
        boolean bStartProjectionOnA = pointProjectionIsInVector(stats.getSegmentBStart(), stats.getSegmentAStart(), stats.getSegmentAEnd());
        boolean bEndProjectionOnA = pointProjectionIsInVector(stats.getSegmentBEnd(), stats.getSegmentAStart(), stats.getSegmentAEnd());
        return aStartProjectionOnB || aEndProjectionOnB || bStartProjectionOnA || bEndProjectionOnA;
    }

    private List<OvertakingEvent> aggregateConsecutiveEvents(List<OvertakingEvent> ovtEvents) {
        ArrayList<OvertakingEvent> result = new ArrayList<>();
        if (!ovtEvents.isEmpty()) {
            double maxSpeedDiff = FastMath.abs(ovtEvents.get(0).getSpeedA() - ovtEvents.get(0).getSpeedB());
            for (int i = 0; i < ovtEvents.size() - 1; i++) {
                int index = 0;
                while (i < ovtEvents.size() - 1 && overtakingVehiclesAreTheSame(ovtEvents.get(i), ovtEvents.get(i + 1))) {
                    OvertakingEvent current = ovtEvents.get(i);
                    double speedDiff = FastMath.abs(current.getSpeedA() - current.getSpeedB());
                    if (speedDiff > maxSpeedDiff) {
                        maxSpeedDiff = speedDiff;
                        index = i;
                    }
                    i++;
                }
                if (index != 0) {
                    result.add(ovtEvents.get(index));
                }
            }
            if (result.isEmpty()) {
                result.addAll(ovtEvents);
            }
        }
        return result;
    }

    private double fixSpeed(GPSTripPoint[] segment, double estSpeed) {
        double maxKnown = Math.max(segment[0].getVelocity(), segment[1].getVelocity());
        return estSpeed > MAX_REASONABLE_SPEED && estSpeed > maxKnown ? maxKnown : estSpeed;
    }

    private boolean onesSpeedIsTooLow(PointOfInterest poi) {
        return poi.getSpeedA() < LOW_SPEED_BOUND || poi.getSpeedB() < LOW_SPEED_BOUND;
    }

    private boolean checkProjectionsWithSyncUp(VectorStats stats) {
        Vector2D segmentAStart = new Vector2D(1.0, stats.getSegmentAStart());
        Vector2D segmentAEnd = new Vector2D(1.0, stats.getSegmentAEnd());
        Vector2D segmentBStart = new Vector2D(1.0, stats.getSegmentBStart());
        Vector2D segmentBEnd = new Vector2D(1.0, stats.getSegmentBEnd());
        // adjust segment start
        if (stats.getStartTimeA().equals(stats.getStartTime())) {
            double progB = stats.getEstimator().getDistanceTravelled(stats.getParamsB(), stats.getStartTimeBDiff())
                                   / stats.getSegmentBLength();
            segmentBStart = stats.getSegmentBStart().add(progB, stats.getSegmentBEnd().subtract(stats.getSegmentBStart()));
        } else {
            double progA = stats.getEstimator().getDistanceTravelled(stats.getParamsA(), stats.getStartTimeADiff())
                                   / stats.getSegmentALength();
            segmentAStart = stats.getSegmentAStart().add(progA, stats.getSegmentAEnd().subtract(stats.getSegmentAStart()));
        }
        // adjust segment end
        if (stats.getEndTimeA().equals(stats.getEndTime())) {
            long bTimeDifference = getDiffInSeconds(stats.getEndTime(), stats.getStartTimeB());
            double progB = stats.getEstimator().getDistanceTravelled(stats.getParamsB(), bTimeDifference)
                                   / stats.getSegmentBLength();
            segmentBEnd = stats.getSegmentBStart().add(progB, stats.getSegmentBEnd().subtract(stats.getSegmentBStart()));
        } else {
            long aTimeDifference = getDiffInSeconds(stats.getEndTime(), stats.getStartTimeA());
            double progA = stats.getEstimator().getDistanceTravelled(stats.getParamsA(), aTimeDifference)
                                   / stats.getSegmentALength();
            segmentAEnd = stats.getSegmentAStart().add(progA, stats.getSegmentAEnd().subtract(stats.getSegmentAStart()));
        }
        boolean aSyncedInB = pointProjectionIsInVector(segmentAStart, segmentBStart, segmentBEnd)
                                     && pointProjectionIsInVector(segmentAEnd, segmentBStart, segmentBEnd);
        boolean bSyncedInA = pointProjectionIsInVector(segmentBStart, segmentAStart, segmentAEnd)
                                     && pointProjectionIsInVector(segmentBEnd, segmentAStart, segmentAEnd);
        boolean distanceDifferenceIsSignificant = distanceFromEndsExceedsThreshold(segmentAStart, segmentBStart, segmentBEnd)
                                                          && distanceFromEndsExceedsThreshold(segmentAEnd, segmentBStart, segmentBEnd)
                                                          && distanceFromEndsExceedsThreshold(segmentBStart, segmentAStart, segmentAEnd)
                                                          && distanceFromEndsExceedsThreshold(segmentBEnd, segmentAStart, segmentAEnd);
        stats.setaSyncedInB(aSyncedInB);
        stats.setbSyncedInA(bSyncedInA);
        return (aSyncedInB || bSyncedInA) && distanceDifferenceIsSignificant;
    }

    private boolean overtakingVehiclesAreTheSame(OvertakingEvent current, OvertakingEvent previous) {
        boolean currentAOvertookB = current.getSpeedB() > current.getSpeedA() == previous.getSpeedB() > previous.getSpeedA();
        boolean sameVehiclesParticipating =
                current.getVehicleDurableIdA().equals(previous.getVehicleDurableIdA())
                        && current.getVehicleDurableIdB().equals(previous.getVehicleDurableIdB());
        return sameVehiclesParticipating && currentAOvertookB;
    }

    private boolean speedDifferenceIsTooSmall(PointOfInterest poi) {
        return FastMath.abs(poi.getSpeedA() - poi.getSpeedB()) < SPEED_DIFFERENCE_LOWER_BOUND;
    }

    private boolean minimalDistanceIsTooLarge(PointOfInterest poi) {
        double scaledDistance = SPEED_SCALER_FOR_DISTANCE * FastMath.min(poi.getSpeedA(), poi.getSpeedB());
        return poi.getMinimalDistance() > scaledDistance;
    }

    private boolean pointProjectionIsInVector(Vector2D p, Vector2D v1, Vector2D v2) {
        double dx = v2.getX() - v1.getX();
        double dy = v2.getY() - v1.getY();
        double innerProduct = (p.getX() - v1.getX()) * dx + (p.getY() - v1.getY()) * dy;
        return 0 < innerProduct && innerProduct < (dx * dx + dy * dy);
    }

    private boolean distanceFromEndsExceedsThreshold(Vector2D p, Vector2D a, Vector2D b) {
        Vector2D ap = p.subtract(a);
        Vector2D ab = b.subtract(a);
        Vector2D proj = a.add(ap.dotProduct(ab) / ab.dotProduct(ab), ab);
        double distToA = haversineDistance(proj.getX(), proj.getY(), a.getX(), a.getY());
        double distToB = haversineDistance(proj.getX(), proj.getY(), b.getX(), b.getY());
        return distToA > MINIMAL_COMMON_DISTANCE && distToB > MINIMAL_COMMON_DISTANCE;
    }

    private boolean drivingOppositeDirection(VectorStats stats) {
        // angle in radians
        double alpha = stats.getVectorA().getNorm() == 0 || stats.getVectorB().getNorm() == 0
                               ? 0 : Vector2D.angle(stats.getVectorA(), stats.getVectorB());
        return alpha > MAX_ANGLE_FOR_MOVING_IN_SAME_DIRECTION_IN_RADIANS || alpha == 0;
    }

    //<editor-fold desc="Getters and setters">
    public GPSTripPoint[][] getData() {
        return data == null ? null : Arrays.copyOf(data, data.length);
    }

    public void setData(GPSTripPoint[][] data) {
        this.data = data == null ? null : Arrays.copyOf(data, data.length);
    }

    public String getVehicleDurableId() {
        return vehicleDurableId;
    }

    public void setVehicleDurableId(String vehicleDurableId) {
        this.vehicleDurableId = vehicleDurableId;
    }

    public String getDriverDurableId() {
        return driverDurableId;
    }

    public void setDriverDurableId(String driverDurableId) {
        this.driverDurableId = driverDurableId;
    }

    public Timestamp getTripStart() {
        return tripStart == null ? null : new Timestamp(tripStart.getTime());
    }

    public void setTripStart(Timestamp tripStart) {
        this.tripStart = tripStart == null ? null : new Timestamp(tripStart.getTime());
    }

    public Timestamp getTripEnd() {
        return tripEnd == null ? null : new Timestamp(tripEnd.getTime());
    }

    public void setTripEnd(Timestamp tripEnd) {
        this.tripEnd = tripEnd == null ? null : new Timestamp(tripEnd.getTime());
    }

    //</editor-fold>
}
