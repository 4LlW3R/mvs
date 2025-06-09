USE prepared;

CREATE TABLE IF NOT EXISTS fact_event(
    `durable_id` varchar(36),
    `organization_durable_key` varchar(36),
    `external_id` bigint,
    `persisted_date_utc` timestamp,
    `driver_durable_key` varchar(36),
    `vehicle_durable_key` varchar(36),
    `total_occurances` bigint,
    `total_time_seconds` int,
    `event_type_durable_key` varchar(36),
    `event_type_id` bigint,
    `driver_id` bigint,
    `asset_id` bigint,
    `value` double,
    `end_date_time` timestamp,
    `start_date_time` timestamp,
    `event_category` varchar(255),
    `start_odometer_kilometres` double,
    `start_position_timestamp` timestamp,
    `start_position_longitude` double,
    `start_position_latitude` double,
    `start_position_position_id` bigint,
    `start_position_speed_kilometres_per_hour` double,
    `end_odometer_kilometres` double,
    `end_position_timestamp` timestamp,
    `end_position_longitude` double,
    `end_position_latitude` double,
    `end_position_position_id` bigint,
    `end_position_speed_kilometres_per_hour` double,
    `value_type` varchar(255),
    `value_units` varchar(255),
    `location_id` bigint,
    `speed_limit` double,
    `overtaking_source_type` string,
    `overtaking_interpolated_latitude` double,
    `overtaking_interpolated_longitude` double,
    `violation_id` int,
    `road_condition_id` string)
PARTITIONED BY (
    `year` int,
    `week_number` int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS fact_event_video(
    `durable_id` varchar(36),
    `organization_durable_key` varchar(36),
    `persisted_date_utc` timestamp,
    `parent_fact_event_durable_key` varchar(36),
    `video_channel_type_durable_key` varchar(36),
    `video_channel_type_code` varchar(255),
    `media_url` varchar(255))
PARTITIONED BY (
    `year` int,
    `week_number` int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS fact_position(
    `durable_id` varchar(36),
    `organization_durable_key` varchar(36),
    `external_id` bigint,
    `persisted_date_utc` timestamp,
    `driver_durable_key` varchar(36),
    `vehicle_durable_key` varchar(36),
    `timestamp` timestamp,
    `longitude` double,
    `latitude` double,
    `driver_id` bigint,
    `asset_id` bigint,
    `avl` boolean,
    `source` varchar(36),
    `odometer_kilometres` double,
    `ignition_on` boolean,
    `age_of_reading_seconds` bigint,
    `pdop` int,
    `vdop` int,
    `hdop` int,
    `number_of_satellites` int,
    `heading` int,
    `altitude_metres` int,
    `speed_kilometres_per_hour` double,
    `distance_since_reading_kilometres` int,
    `formatted_address` varchar(255),
    `speed_limit` double)
PARTITIONED BY (
    `year` int,
    `week_number` int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");


CREATE TABLE IF NOT EXISTS fact_subtrip(
    `durable_id` varchar(36),
    `organization_durable_key` varchar(36),
    `external_id` bigint,
    `persisted_date_utc` timestamp,
    `parent_trip_key` varchar(36),
    `sub_trip_start` timestamp,
    `start_position_id` bigint,
    `start_position_timestamp` timestamp,
    `start_position_longitude` double,
    `start_position_latitude` double,
    `start_position_speed_kilometres_per_hour` double,
    `depart` timestamp,
    `halt` timestamp,
    `sub_trip_end` timestamp,
    `end_position_id` bigint,
    `end_position_timestamp` timestamp,
    `end_position_longitude` double,
    `end_position_latitude` double,
    `driving_time` int,
    `standing_time` int,
    `duration` int,
    `distance_kilometres` double,
    `start_odometer_kilometres` double,
    `end_odometer_kilometres` double,
    `start_engine_seconds` int,
    `end_engine_seconds` int,
    `engine_seconds` int,
    `pulse_value` double,
    `fuel_used_litres` double,
    `max_speed_kilometers_per_hour` double,
    `max_acceleration_kilometers_per_hour_per_second` double,
    `max_deceleration_kilometers_per_hour_per_second` double,
    `max_rpm` double)
PARTITIONED BY (
    `year` int,
    `week_number` int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS fact_trip(
    `durable_id` varchar(36),
    `organization_durable_key` varchar(36),
    `external_id` bigint,
    `persisted_date_utc` timestamp,
    `driver_durable_key` varchar(36),
    `vehicle_durable_key` varchar(36),
    `asset_id` bigint,
    `driver_id` bigint,
    `trip_start` timestamp,
    `trip_end` timestamp,
    `notes` varchar(4000),
    `pulse_parameter_name` varchar(36),
    `engine_seconds` int,
    `start_position_id` bigint,
    `start_position_timestamp` timestamp,
    `start_position_longitude` double,
    `start_position_latitude` double,
    `start_position_speed_kilometres_per_hour` double,
    `end_position_id` bigint,
    `end_position_timestamp` timestamp,
    `end_position_longitude` double,
    `end_position_latitude` double,
    `first_depart` timestamp,
    `last_halt` timestamp,
    `driving_time` double,
    `standing_time` double,
    `duration` double,
    `distance_kilometers` double,
    `start_odometer_kilometers` double,
    `end_odometer_kilometers` double,
    `start_engine_seconds` int,
    `end_engine_seconds` int,
    `pulse_value` double,
    `fuel_used_litres` double,
    `max_speed_kilometers_per_hour` double,
    `max_acceleration_kilometers_per_hour_per_second` double,
    `max_deceleration_kilometers_per_hour_per_second` double,
    `max_rpm` double)
PARTITIONED BY (
    `year` int,
    `week_number` int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS fact_tacho(
    `durable_id` varchar(36),
    `organization_durable_key` varchar(36),
    `external_id` bigint,
    `persisted_date_utc` timestamp,
    `asset_id` bigint,
    `durable_asset_id` varchar(36),
    `line_name` varchar(100),
    `value_date_time` timestamp,
    `value` int,
    `start_date_time` timestamp,
    `end_date_time` timestamp)
PARTITIONED BY (
    `year` int,
    `week_number` int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");
