USE @datalake.output.database@;

DROP TABLE fact_event_validated_golden_text_tmp;
CREATE EXTERNAL TABLE fact_event_validated_golden_text_tmp(                           
   subscription_id int,                                    
   external_id bigint,                                     
   ingested_time timestamp,                                
   persisted_time timestamp,                               
   vehicle_id int,                                         
   lineage_code int,                                       
   driver_id int,                                          
   driver_orig_id int,                                     
   event_id int,                                           
   event_type int,                                         
   start_seq int,                                          
   end_seq int,                                            
   start timestamp,                                        
   end_ timestamp,                                         
   recorded timestamp,                                     
   start_odo double,                                       
   end_odo double,                                         
   odometer double,                                        
   start_gps_id bigint,                                    
   end_gps_id bigint,                                      
   total_time int,                                         
   total_occurs int,                                       
   value double,                                           
   litres double,                                          
   f3prm int,                                              
   f3value double,
   year int, 
   week_number int
)                                         
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location '/vr-input-files/fact_event_validated'
tblproperties ("skip.header.line.count"="1");


DROP TABLE fact_event_validated_golden;
CREATE TABLE fact_event_validated_golden(                           
  `subscription_id` int,
  `external_id` bigint,
  `ingested_time` timestamp,
  `persisted_time` timestamp,
  `vehicle_id` int,
  `lineage_code` int,
  `driver_id` int,
  `driver_orig_id` int,
  `event_id` int,
  `event_type` int,
  `start_seq` int,
  `end_seq` int,
  `start` timestamp,
  `end_` timestamp,
  `recorded` timestamp,
  `start_odo` double,
  `end_odo` double,
  `odometer` double,
  `start_gps_id` bigint,
  `end_gps_id` bigint,
  `total_time` int,
  `total_occurs` int,
  `value` double,
  `litres` double,
  `f3prm` int,
  `f3value` double,
  `validation_code` int,
  `problem_vehicle` int)
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

INSERT OVERWRITE TABLE fact_event_validated_golden SELECT * FROM fact_event_validated_golden_text_tmp;


