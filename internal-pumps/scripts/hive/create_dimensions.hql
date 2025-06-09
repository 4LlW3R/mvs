USE raw;

CREATE TABLE dim_asset(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    asset_id bigint,
    asset_type_id int,
    description varchar(255),
    connected_trailer boolean,
    registration_number varchar(100),
    site_id bigint,
    fuel_type varchar(100),
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
    notes varchar(100),
    icon varchar(100),
    icon_colour varchar(100),
    colour varchar(100),
    asset_image varchar(100),
    default_image boolean,
    asset_image_url varchar(255),
    user_state varchar(100),
    created_by varchar(100),
    created_date timestamp,
    odometer double,
    engine_hours varchar(100),
    country varchar(100))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_asset_norm(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    asset_id bigint,
    asset_type_id int,
    description varchar(255),
    connected_trailer boolean,
    registration_number varchar(100),
    site_id bigint,
    fuel_type varchar(100),
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
    notes varchar(100),
    icon varchar(100),
    icon_colour varchar(100),
    colour varchar(100),
    asset_image varchar(100),
    default_image boolean,
    asset_image_url varchar(255),
    user_state varchar(100),
    created_by varchar(100),
    created_date timestamp,
    odometer double,
    engine_hours varchar(100),
    country varchar(100))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_driver(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    site_id bigint,
    driver_id bigint,
    name varchar(255),
    image_uri varchar(255),
    fm_driver_id bigint,
    employee_number varchar(255),
    system_driver boolean,
    mobile_number varchar(255),
    email varchar(255),
    extended_driver_id varchar(255),
    extended_driver_id_type varchar(50),
    country varchar(255))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_driver_norm(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    site_id bigint,
    driver_id bigint,
    name varchar(255),
    image_uri varchar(255),
    fm_driver_id bigint,
    employee_number varchar(255),
    system_driver boolean,
    mobile_number varchar(255),
    email varchar(255),
    extended_driver_id varchar(255),
    extended_driver_id_type varchar(50),
    country varchar(255))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_library_event(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    description varchar(255),
    event_type_id bigint,
    event_type varchar(100),
    display_units varchar(100),
    format_type varchar(100),
    value_name varchar(100))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_library_event_norm(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    description varchar(255),
    event_type_id bigint,
    event_type varchar(100),
    display_units varchar(100),
    format_type varchar(100),
    value_name varchar(100))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_location(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    location_id bigint,
    group_id bigint,
    name varchar(255),
    address varchar(255),
    location_type varchar(100),
    shape_type varchar(100),
    radius double,
    shape_wkt varchar(4000),
    deleted boolean,
    colour_on_map varchar(100),
    opacity_on_map double,
    temporary boolean,
    external_reference varchar(100))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_location_norm(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    location_id bigint,
    group_id bigint,
    name varchar(255),
    address varchar(255),
    location_type varchar(100),
    shape_type varchar(100),
    radius double,
    shape_wkt varchar(4000),
    deleted boolean,
    colour_on_map varchar(100),
    opacity_on_map double,
    temporary boolean,
    external_reference varchar(100))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_organisation_group(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    group_id bigint,
    type varchar(255),
    display_time_zone varchar(255),
    name varchar(255))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_organisation_group_norm(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    group_id bigint,
    type varchar(255),
    display_time_zone varchar(255),
    name varchar(255))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_organisation_subgroup(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    group_id bigint,
    parent_org_id bigint,
    parent_subgroup_id bigint,
    name varchar(255),
    type varchar(255))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

CREATE TABLE dim_organisation_subgroup_norm(
    durable_id varchar(36),
    ingested_date_utc timestamp,
    subscription_id bigint,
    lineage_code int,
    persisted_date_utc timestamp,
    group_id bigint,
    parent_org_id bigint,
    parent_subgroup_id bigint,
    name varchar(255),
    type varchar(255))
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");