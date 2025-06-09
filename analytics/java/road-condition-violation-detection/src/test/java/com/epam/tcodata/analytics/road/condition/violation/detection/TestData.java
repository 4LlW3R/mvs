package com.epam.tcodata.analytics.road.condition.violation.detection;

import com.epam.tcodata.analytics.road.condition.violation.detection.domain.*;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestData {
    private TestData(){}
    public static final String TEST_VEHICLE_ID = "1";
    public static final Long TEST_SUBSCRIPTION_ID = 1L;
    public static final String TEST_DRIVER_ID = "1";
    private static final String DATE_20170717_01_23_50 = "2017-07-17 01:23:50";
    private static final String DATE_20170717_01_23_58 = "2017-07-17 01:23:58";
    private static final String DATE_20170717_01_24_09 = "2017-07-17 01:24:09";
    private static final String DATE_20170717_01_24_36 = "2017-07-17 01:24:36";

    static List<GPSPoint> getPositionsForAmberSimpleViolation() {
        List<GPSPoint> list = new ArrayList<>();
        list.add(getPoint(46.4196716309, 53.4259681702, Timestamp.valueOf(DATE_20170717_01_23_50), 45));
        list.add(getPoint(46.4197517395, 53.4259490967, Timestamp.valueOf(DATE_20170717_01_23_58), 50));
        list.add(getPoint(46.4190307617, 53.4256896973, Timestamp.valueOf(DATE_20170717_01_24_09), 61));
        list.add(getPoint(46.4168907166, 53.4246902466, Timestamp.valueOf(DATE_20170717_01_24_36), 41));
        return list;
    }

    static List<GPSPoint> getPositionsForAmberTwoViolations() {
        List<GPSPoint> list = new ArrayList<>();
        list.add(getPoint(46.4197517395, 53.4259490967, Timestamp.valueOf("2017-07-17 01:24:00"), 30));
        list.add(getPoint(46.4196716309, 53.4259681702, Timestamp.valueOf(DATE_20170717_01_23_50), 45));
        list.add(getPoint(46.4197517395, 53.4259490967, Timestamp.valueOf(DATE_20170717_01_23_58), 50));
        list.add(getPoint(46.4190307617, 53.4256896973, Timestamp.valueOf(DATE_20170717_01_24_09), 60));
        list.add(getPoint(46.4168907166, 53.4246902466, Timestamp.valueOf(DATE_20170717_01_24_36), 41));
        return list;
    }

    static List<GPSPoint> getPositionsForAmberNoViolationSpeedLow() {
        List<GPSPoint> list = new ArrayList<>();
        list.add(getPoint(46.4196716309, 53.4259681702, Timestamp.valueOf(DATE_20170717_01_23_50), 22));
        list.add(getPoint(46.4197517395, 53.4259490967, Timestamp.valueOf(DATE_20170717_01_23_58), 22));
        list.add(getPoint(46.4190307617, 53.4256896973, Timestamp.valueOf(DATE_20170717_01_24_09), 24));
        list.add(getPoint(46.4168907166, 53.4246902466, Timestamp.valueOf(DATE_20170717_01_24_36), 26));
        return list;
    }

    static List<GPSPoint> getPositionsForAmberNoViolationOutOfArea() {
        List<GPSPoint> list = new ArrayList<>();
        list.add(getPoint(46.3196716309, 53.4259681702, Timestamp.valueOf(DATE_20170717_01_23_50), 22));
        list.add(getPoint(46.3197517395, 53.4259490967, Timestamp.valueOf(DATE_20170717_01_23_58), 22));
        list.add(getPoint(46.3190307617, 53.4256896973, Timestamp.valueOf(DATE_20170717_01_24_09), 24));
        list.add(getPoint(46.3168907166, 53.4246902466, Timestamp.valueOf(DATE_20170717_01_24_36), 45));
        return list;
    }

    static RoadConditionViolation createViolation(double averageSpeed, Timestamp start, Timestamp end, String policyAreaId) {
        RoadConditionViolation viol = new RoadConditionViolation();
        viol.setSubscriptionId(TEST_SUBSCRIPTION_ID);
        viol.setVehicleId(TEST_VEHICLE_ID);
        viol.setDriverId(TEST_DRIVER_ID);
        viol.setEndTime(end);
        viol.setStartTime(start);
        viol.setAverageSpeed(averageSpeed);
        viol.setPolicyAreaId(policyAreaId);
        viol.setId("1-1-1");
        viol.setCreationTimeUTC(Timestamp.from(Instant.ofEpochMilli(1)));
        viol.setStartFactGpsId("1");
        viol.setEndFactGpsId("2");

        viol.setStartLatitude(0.0);
        viol.setStartLongitude(0.0);

        viol.setEndLatitude(0.0);
        viol.setEndLongitude(0.0);
        return viol;
    }

    static List<GPSPoint> getLatePointOutOfGroup() {
        GPSPoint p = getPoint(0.0, 0.0, Timestamp.from(Instant.now()), 0.0);
        p.setSubscriptionId(2L);
        return Collections.singletonList(p);
    }

    static List<RoadConditionArea> sampleAmberArea(DateTime startDt, DateTime endDt, RoadConditionType type) {
        RoadConditionArea area = AreaTestCase.getExampleArea();
        area.setStartDateTime(startDt);
        area.setEndDateTime(endDt);
        area.setRoadConditionType(type);
        return Collections.singletonList(area);
    }

    private static GPSPoint getPoint(double lat, double lon, Timestamp time, double velocity) {
        GPSPoint point = new GPSPoint();
        point.setVehicleId(TEST_VEHICLE_ID);
        point.setSubscriptionId(TEST_SUBSCRIPTION_ID);
        point.setDriverId(TEST_DRIVER_ID);
        point.setLatitude(lat);
        point.setLongitude(lon);
        point.setTime(time);
        point.setVelocity(velocity);
        return point;
    }

    @SuppressWarnings("unused")
    private static GPSPointWithArea getAmberPoint(double lat, double lon, Timestamp time, int velocity) {
        return new GPSPointWithArea(getPoint(lat, lon, time, velocity), "amber", RoadConditionType.AMBER.getName());
    }

}
