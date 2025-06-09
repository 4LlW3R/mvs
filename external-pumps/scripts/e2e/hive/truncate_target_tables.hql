USE raw; 

TRUNCATE TABLE fact_trip; 
TRUNCATE TABLE fact_sub_trip; 
TRUNCATE TABLE fact_event; 
TRUNCATE TABLE fact_gps_pos; 

TRUNCATE TABLE dim_driver;
TRUNCATE TABLE dim_vehicle;
TRUNCATE TABLE dim_location;
TRUNCATE TABLE dim_event_desc;
  