USE prepared;

CREATE TABLE IF NOT EXISTS dim_fuel_type(
    durable_id varchar(36),
    fuel_type_code varchar(20),
    fuel_type_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_group_type(
    durable_id varchar(36),
    external_id bigint,
    group_type_code varchar(20),
    group_type_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_location_type(
    durable_id varchar(36),
    external_id bigint,
    location_type_code varchar(20),
    location_type_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_location_shape_type(
    durable_id varchar(36),
    external_id bigint,
    shape_type_code varchar(20),
    shape_type_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_vehicle_state(
    durable_id varchar(36),
    state_code varchar(20),
    state_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_vehicle_type(
    durable_id varchar(36),
    external_id bigint,
    vehicle_type_code varchar(20),
    vehicle_type_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_video_channel_type(
    durable_id varchar(36),
    channel_code varchar(20),
    channel_description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_event_validation_code(
    durable_id varchar(36),
    external_id varchar(20),
    description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_event_problem_vehicle_code(
    durable_id varchar(36),
    external_id bigint,
    shape_type_code varchar(20),
    description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_overtaking_violation_code(
    durable_id varchar(36),
    external_id bigint,
    description varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

