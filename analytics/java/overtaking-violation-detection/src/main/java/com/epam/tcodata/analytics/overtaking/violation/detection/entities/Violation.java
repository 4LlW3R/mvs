package com.epam.tcodata.analytics.overtaking.violation.detection.entities;

import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.Area;

import java.io.Serializable;

public class Violation implements Serializable {
    public static final Violation NO_VIOLATION = new Violation(Area.AREA_WITHOUT_RESTRICTION.getId(), Type.NO_VIOLATION);
    private static final long serialVersionUID = -8415862484480084779L;
    /**
     * By default is equal to {@link Area#AREA_WITHOUT_RESTRICTION} id. Not that only in case of area-specific policy.
     */
    private String policyAreaId;
    private int violationId;

    public Violation() {
    }

    public Violation(String policyAreaId, Violation.Type type) {
        this.policyAreaId = policyAreaId;
        this.violationId = type.getViolationId();
    }

    public Violation(Violation.Type type) {
        this.violationId = type.getViolationId();
        this.policyAreaId = Area.AREA_WITHOUT_RESTRICTION.getId();
    }

    public static boolean filterViolations(Violation violation) {
        return violation.violationId != NO_VIOLATION.getViolationId();
    }

    /**
     * Filter areas.
     * @param violation specified violation.
     * @return result of filtering.
     */
    public static boolean filterAreas(Violation violation) {
        if (violation != null) {
            boolean nonEqAreaWRestriction = !violation.getPolicyAreaId().equals(Area.AREA_WITHOUT_RESTRICTION.getId());
            boolean nonEqErrArea = !violation.getPolicyAreaId().equals(Area.ERRONEOUS_AREA.getId());
            return nonEqAreaWRestriction && nonEqErrArea;
        }
        return false;
    }

    public String getPolicyAreaId() {
        return policyAreaId;
    }

    public void setPolicyAreaId(String policyAreaId) {
        this.policyAreaId = policyAreaId;
    }

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public enum Type {
        NO_VIOLATION(1),
        // Overtaking related violations
        NIGHT_TIME_OVERTAKING(2),
        COMMUTE_HOURS_OVERTAKING(3),
        VEHICLE_IN_FRONT_IS_FAST_OVERTAKING(4),
        SPEED_LIMIT_EXCEEDED_OVERTAKING(5),
        OVERTAKING_DURING_ROAD_CONDITION(6),
        NO_OVERTAKING_ZONE_OVERTAKING(7),
        BUS_OVERTAKING(8);

        private int violationId;

        Type(int violationId) {
            this.violationId = violationId;
        }

        public int getViolationId() {
            return violationId;
        }
    }
}
