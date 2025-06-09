package com.epam.tcodata.analytics.overtaking.violation.detection.policies;

public enum PolicyVersion {

    DEFAULT_POLICY(1);

    private int id;

    PolicyVersion(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
