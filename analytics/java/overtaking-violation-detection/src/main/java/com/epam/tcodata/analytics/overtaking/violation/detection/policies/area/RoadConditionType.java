package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

public enum RoadConditionType {
    AMBER("Amber"),
    RED("Red");

    private String name;

    RoadConditionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
