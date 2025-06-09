package com.epam.tcodata.models;

public enum ApplicationType {

    /**
     * External Pump application.
     */
    EXTERNAL_PUMP(
            ApplicationSuperType.PUMP,
            1,
            "external_pump_cg"),

    /**
     * Internal Pump application.
     */
    INTERNAL_PUMP(
            ApplicationSuperType.PUMP,
            2,
            "internal_pump_cg"),

    /**
     * Overtaking detection analytical application.
     */
    OVERTAKING_DETECTION(
            ApplicationSuperType.ANALYTICS,
            3,
            "overtaking_detection_cg"),

    /**
     * Overtaking violation detection analytical application.
     */
    OVERTAKING_VIOLATION_DETECTION(
            ApplicationSuperType.ANALYTICS,
            4,
            "overtaking_violation_detection_cg"),

    /**
     * Overtaking violations stream datalake application.
     */
    OVERTAKING_VIOLATION_STREAM_DATALAKE(
            ApplicationSuperType.PERSISTENCE,
            5,
            "overtaking_violation_stream_datalake_cg"),

    /**
     * Road condition violation detection application.
     */
    ROAD_CONDITION_VIOLATION_DETECTION(
            ApplicationSuperType.ANALYTICS,
            6,
            "road_condition_violation_detection_cg"),

    /**
     * Road condition violations stream datalake application.
     */
    ROAD_CONDITION_VIOLATION_STREAM_DATALAKE(
            ApplicationSuperType.PERSISTENCE,
            7,
            "road_condition_violation_stream_datalake_cg"),

    /**
     * Spark Driver Job responsible for uploading Driver/Asset data from Azure EventHub into Redis.
     */
    REDIS_MANAGER(
            ApplicationSuperType.REDIS,
            8,
            "redis_manager_cg"
    );

    private ApplicationSuperType superType;
    private int code;
    private String consumerGroup;

    ApplicationType(ApplicationSuperType superType, int code, String consumerGroup) {
        this.superType = superType;
        this.code = code;
        this.consumerGroup = consumerGroup;
    }

    /**
     * Returns super type.
     *
     * @return super type
     */
    public ApplicationSuperType getSuperType() {
        return superType;
    }

    /**
     * Returns numeric representation of current code.
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns consumer group name, used by this application.
     *
     * @return consumer group
     */
    public String getConsumerGroup() {
        return consumerGroup;
    }
}
