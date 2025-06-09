CREATE DATABASE IF NOT EXISTS prepared;
USE prepared;

CREATE TABLE IF NOT EXISTS prepared.road_condition_violation (
    domain STRING,
    entity_type INT,
    schema_version INT,
    id STRING,
    subscription_id BIGINT,
    vehicle_id STRING,
    driver_id STRING,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    average_speed DOUBLE,
    max_speed DOUBLE,
    road_condition_id STRING,
    creation_time TIMESTAMP,
    start_fact_gps_id STRING,
    end_fact_gps_id STRING,
    start_latitude DOUBLE,
    start_longitude DOUBLE,
    end_latitude DOUBLE,
    end_longitude DOUBLE)
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");