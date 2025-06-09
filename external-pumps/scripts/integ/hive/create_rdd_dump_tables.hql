USE raw; 

DROP TABLE IF EXISTS fact_trip_dump;
CREATE EXTERNAL TABLE fact_trip_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 driver_durable_key STRING,
 vehicle_durable_key STRING,
 trip_id STRING,
 asset_id STRING,
 driver_id STRING,
 trip_start STRING,
 trip_end STRING,
 notes STRING,
 pulse_parameter_name STRING,
 engine_seconds STRING,
 start_position_id STRING,
 start_position_timestamp STRING,
 start_position_longitude STRING,
 start_position_latitude STRING,
 start_position_position_id STRING,
 start_position_speed_kilometres_per_hour STRING,
 start_position_asset_id STRING,
 start_position_driver_id STRING,
 start_position_speed_limit STRING,
 start_position_altitude_metres STRING,
 start_position_heading STRING,
 start_position_number_of_satellites STRING,
 start_position_hdop STRING,
 start_position_vdop STRING,
 start_position_pdop STRING,
 start_position_age_of_reading_seconds STRING,
 start_position_distance_since_reading_kilometres STRING,
 start_position_ignition_on STRING,
 start_position_odometer_kilometres STRING,
 start_position_formatted_address STRING,
 start_position_source STRING,
 start_position_avl STRING,
 end_position_id STRING,
 end_position_timestamp STRING,
 end_position_longitude STRING,
 end_position_latitude STRING,
 end_position_position_id STRING,
 end_position_speed_kilometres_per_hour STRING,
 end_position_asset_id STRING,
 end_position_driver_id STRING,
 end_position_speed_limit STRING,
 end_position_altitude_metres STRING,
 end_position_heading STRING,
 end_position_number_of_satellites STRING,
 end_position_hdop STRING,
 end_position_vdop STRING,
 end_position_pdop STRING,
 end_position_age_of_reading_seconds STRING,
 end_position_distance_since_reading_kilometres STRING,
 end_position_ignition_on STRING,
 end_position_odometer_kilometres STRING,
 end_position_formatted_address STRING,
 end_position_source STRING,
 end_position_avl STRING,
 first_depart STRING,
 last_halt STRING,
 driving_time STRING,
 standing_time STRING,
 duration STRING,
 distance_kilometers STRING,
 start_odometer_kilometers STRING,
 end_odometer_kilometers STRING,
 start_engine_seconds STRING,
 end_engine_seconds STRING,
 pulse_value STRING,
 fuel_used_litres STRING,
 max_speed_kilometers_per_hour STRING,
 max_acceleration_kilometers_per_hour_per_second STRING,
 max_deceleration_kilometers_per_hour_per_second STRING,
 max_rpm STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_Trip'
tblproperties ("skip.header.line.count"="1");


DROP TABLE IF EXISTS fact_subtrip_dump;
CREATE EXTERNAL TABLE fact_subtrip_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 sub_trip_id STRING,
 parent_trip_key STRING,
 sub_trip_start STRING,
 start_position_id STRING,
 start_position_timestamp STRING,
 start_position_longitude STRING,
 start_position_latitude STRING,
 start_position_position_id STRING,
 start_position_speed_kilometres_per_hour STRING,
 start_position_asset_id STRING,
 start_position_driver_id STRING,
 start_position_speed_limit STRING,
 start_position_altitude_metres STRING,
 start_position_heading STRING,
 start_position_number_of_satellites STRING,
 start_position_hdop STRING,
 start_position_vdop STRING,
 start_position_pdop STRING,
 start_position_age_of_reading_seconds STRING,
 start_position_distance_since_reading_kilometres STRING,
 start_position_ignition_on STRING,
 start_position_odometer_kilometres STRING,
 start_position_formatted_address STRING,
 start_position_source STRING,
 start_position_avl STRING,
 depart STRING,
 halt STRING,
 sub_trip_end STRING,
 end_position_id STRING,
 end_position_timestamp STRING,
 end_position_longitude STRING,
 end_position_latitude STRING,
 end_position_position_id STRING,
 end_position_speed_kilometres_per_hour STRING,
 end_position_asset_id STRING,
 end_position_driver_id STRING,
 end_position_speed_limit STRING,
 end_position_altitude_metres STRING,
 end_position_heading STRING,
 end_position_number_of_satellites STRING,
 end_position_hdop STRING,
 end_position_vdop STRING,
 end_position_pdop STRING,
 end_position_age_of_reading_seconds STRING,
 end_position_distance_since_reading_kilometres STRING,
 end_position_ignition_on STRING,
 end_position_odometer_kilometres STRING,
 end_position_formatted_address STRING,
 end_position_source STRING,
 end_position_avl STRING,
 driving_time STRING,
 standing_time STRING,
 duration STRING,
 distance_kilometres STRING,
 start_odometer_kilometres STRING,
 end_odometer_kilometres STRING,
 start_engine_seconds STRING,
 end_engine_seconds STRING,
 engine_seconds STRING,
 pulse_value STRING,
 fuel_used_litres STRING,
 max_speed_kilometers_per_hour STRING,
 max_acceleration_kilometers_per_hour_per_second STRING,
 max_deceleration_kilometers_per_hour_per_second STRING,
 max_rpm STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_SubTrip'
tblproperties ("skip.header.line.count"="1");



DROP TABLE IF EXISTS fact_position_dump;
CREATE EXTERNAL TABLE fact_position_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 driver_durable_key STRING,
 vehicle_durable_key STRING,
 timestamp1 STRING,
 longitude STRING,
 latitude STRING,
 driver_id STRING,
 asset_id STRING,
 position_id STRING,
 avl STRING,source STRING,
 odometer_kilometres STRING,
 ignition_on STRING,
 age_of_reading_seconds STRING,
 pdop STRING,
 vdop STRING,
 hdop STRING,
 number_of_satellites STRING,
 heading STRING,
 altitude_metres STRING,
 speed_kilometres_per_hour STRING,
 distance_since_reading_kilometres STRING,
 formatted_address STRING,
 speed_limit STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_Position'
tblproperties ("skip.header.line.count"="1");


DROP TABLE IF EXISTS fact_event_dump;
CREATE EXTERNAL TABLE fact_event_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 driver_durable_key STRING,
 vehicle_durable_key STRING,
 total_occurances STRING,
 total_time_seconds STRING,
 event_type_id STRING,
 event_id STRING,
 driver_id STRING,
 asset_id STRING,
 value STRING,
 end_date_time STRING,
 start_date_time STRING,
 event_category STRING,
 start_odometer_kilometres STRING,
 start_position_timestamp STRING,
 start_position_longitude STRING,
 start_position_latitude STRING,
 start_position_position_id STRING,
 start_position_speed_kilometres_per_hour STRING,
 start_position_asset_id STRING,
 start_position_driver_id STRING,
 start_position_speed_limit STRING,
 start_position_altitude_metres STRING,
 start_position_heading STRING,
 start_position_number_of_satellites STRING,
 start_position_hdop STRING,
 start_position_vdop STRING,
 start_position_pdop STRING,
 start_position_age_of_reading_seconds STRING,
 start_position_distance_since_reading_kilometres STRING,
 start_position_ignition_on STRING,
 start_position_odometer_kilometres STRING,
 start_position_formatted_address STRING,
 start_position_source STRING,
 start_position_avl STRING,
 end_odometer_kilometres STRING,
 end_position_timestamp STRING,
 end_position_longitude STRING,
 end_position_latitude STRING,
 end_position_position_id STRING,
 end_position_speed_kilometres_per_hour STRING,
 end_position_asset_id STRING,
 end_position_driver_id STRING,
 end_position_speed_limit STRING,
 end_position_altitude_metres STRING,
 end_position_heading STRING,
 end_position_number_of_satellites STRING,
 end_position_hdop STRING,
 end_position_vdop STRING,
 end_position_pdop STRING,
 end_position_age_of_reading_seconds STRING,
 end_position_distance_since_reading_kilometres STRING,
 end_position_ignition_on STRING,
 end_position_odometer_kilometres STRING,
 end_position_formatted_address STRING,
 end_position_source STRING,
 end_position_avl STRING,
 value_type STRING,
 value_units STRING,
 road STRING,
 cab STRING,
 camera3 STRING,
 camera4 STRING,
 location_id STRING,
 speed_limit STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_Event'
tblproperties ("skip.header.line.count"="1");


DROP TABLE IF EXISTS dim_driver_dump;
CREATE EXTERNAL TABLE dim_driver_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 site_id STRING,
 driver_id STRING,
 name STRING,
 image_uri STRING,
 fm_driver_id STRING,
 employee_number STRING,
 system_driver STRING,
 mobile_number STRING,
 email STRING,
 extended_driver_id STRING,
 extended_driver_id_type STRING,
 country STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_Driver'
tblproperties ("skip.header.line.count"="1");


DROP TABLE IF EXISTS dim_asset_dump;
CREATE EXTERNAL TABLE dim_asset_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 asset_id STRING,
 asset_type_id STRING,
 description STRING,
 connected_trailer STRING,
 registration_number STRING,
 site_id STRING,
 fuel_type STRING,
 target_fuel_consumption STRING,
 target_fuel_consumption_units STRING,
 target_hourly_fuel_consumption STRING,
 target_hourly_fuel_consumption_units STRING,
 fleet_number STRING,
 make STRING,
 model STRING,
 year STRING,
 vin_number STRING,
 engine_number STRING,
 fm_vehicle_id STRING,
 additional_mobile_device STRING,
 notes STRING,
 icon STRING,
 icon_colour STRING,
 colour STRING,
 asset_image STRING,
 default_image STRING,
 asset_image_url STRING,
 user_state STRING,
 created_by STRING,
 created_date STRING,
 odometer STRING,
 engine_hours STRING,
 country STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_Asset'
tblproperties ("skip.header.line.count"="1");


DROP TABLE IF EXISTS dim_location_dump;
CREATE EXTERNAL TABLE dim_location_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 location_id STRING,
 organisation_id STRING,
 group_id STRING,
 name STRING,
 address STRING,
 location_type STRING,
 shape_type STRING,
 radius STRING,
 shape_wkt STRING,
 deleted STRING,
 colour_on_map STRING,
 opacity_on_map STRING,
 temporary STRING,
 external_reference STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_Location'
tblproperties ("skip.header.line.count"="1");


DROP TABLE IF EXISTS dim_library_event_dump;
CREATE EXTERNAL TABLE dim_library_event_dump(
 durable_id STRING,
 ingested_date_utc STRING,
 subscription_id STRING,
 lineage_code STRING,
 description STRING,
 event_type_id STRING,
 event_type STRING,
 display_units STRING,
 format_type STRING,
 value_name STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location 'dbfs:/mnt/mvsdatalaket332integtco/sshadmin/external-pumps/dump/ExternalPump_LibraryEvent'
tblproperties ("skip.header.line.count"="1");
