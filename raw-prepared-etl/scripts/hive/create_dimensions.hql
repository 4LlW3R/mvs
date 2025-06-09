USE prepared;

CREATE TABLE IF NOT EXISTS dim_driver(
    durable_id varchar(36),
    organization_durable_key varchar(36),
    external_id bigint,
    persisted_date_utc timestamp,
    org_group_durable_key varchar(36),
    name varchar(255),
    fm_driver_id bigint,
    employee_number varchar(255),
    system_driver boolean,
    mobile_number varchar(255),
    email varchar(255),
    extended_driver_id varchar(255),
    extended_driver_id_type int,
    country varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_event_description(
    durable_id varchar(36),
    organization_durable_key varchar(36),
    external_id bigint,
    persisted_date_utc timestamp,
    description varchar(255),
    event_type varchar(100),
    display_units varchar(100),
    format_type varchar(100),
    value_name varchar(100))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_group(
    durable_id varchar(36),
    external_id bigint,
    persisted_date_utc timestamp,
    name varchar(255),
    group_type_durable_key varchar(36),
    group_type_code varchar(255),
    display_time_zone varchar(255),
    parent_group_durable_key varchar(36),
    parent_group_id bigint,
    fm_org_group_id int)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_location(
    durable_id varchar(36),
    organization_durable_key varchar(36),
    external_id bigint,
    persisted_date_utc timestamp,
    org_group_durable_key varchar(36),
    name varchar(255),
    address varchar(255),
    location_type_durable_key varchar(36),
    location_type_code varchar(255),
    shape_type_durable_key varchar(36),
    shape_type_code varchar(255),
    radius double,
    shape_wkt string,
    deleted boolean,
    colour_on_map varchar(100),
    opacity_on_map double,
    temporary boolean,
    external_reference varchar(255))
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");

CREATE TABLE IF NOT EXISTS dim_vehicle(
    durable_id varchar(36),
    organization_durable_key varchar(36),
    external_id bigint,
    persisted_date_utc timestamp,
    vehicle_type_durable_key varchar(36),
    vehicle_type_code varchar(255),
    description varchar(100),
    connected_trailer boolean,
    registration_number varchar(100),
    org_group_durable_key varchar(36),
    fuel_type_durable_key varchar(36),
    fuel_type_code varchar(255),
    target_fuel_consumption double,
    target_fuel_consumption_units varchar(100),
    target_hourly_fuel_consumption double,
    target_hourly_fuel_consumption_units varchar(100),
    fleet_number varchar(100),
    make varchar(100),
    model varchar(100),
    year varchar(100),
    vin_number varchar(100),
    engine_number varchar(100),
    fm_vehicle_id bigint,
    additional_mobile_device varchar(100),
    notes varchar(4000),
    vehicle_state_durable_key varchar(36),
    vehicle_state_code varchar(100),
    created_by varchar(100),
    created_date timestamp,
    odometer double,
    engine_seconds bigint)
ROW FORMAT SERDE
    "org.apache.hadoop.hive.ql.io.orc.OrcSerde"
STORED AS INPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat"
OUTPUTFORMAT
    "org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat"
TBLPROPERTIES (
    "auto.purge"="true");
