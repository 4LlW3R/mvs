package com.epam.tcodata.analytics.road.condition.violation.detection.domain;

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
