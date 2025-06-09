USE @datalake.input.database@;


DROP TABLE fact_event_text_tmp;
CREATE EXTERNAL TABLE fact_event_text_tmp(                           
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
   f3value double)                                         
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location '/vr-input-files/fact_event'
tblproperties ("skip.header.line.count"="1");


DROP TABLE @datalake.input.table.factEvent@;
CREATE TABLE @datalake.input.table.factEvent@(                           
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
   f3value double)                                         
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

INSERT OVERWRITE TABLE @datalake.input.table.factEvent@ SELECT * FROM fact_event_text_tmp;


DROP TABLE fact_gps_pos_text_tmp;
CREATE EXTERNAL TABLE fact_gps_pos_text_tmp(                           
   `subscription_id` int,                               
   `external_id` bigint,                                
   `ingested_time` timestamp,                           
   `persisted_time` timestamp,                          
   `vehicle_id` int,                                    
   `lineage_code` int,                                  
   `driver_id` int,                                     
   `driver_orig_id` int,                                
   `event_time` timestamp,                              
   `latitude` double,                                   
   `longitude` double,                                  
   `altitude` int,                                      
   `velocity` int,                                      
   `block_seq` int,                                     
   `heading` int,                                       
   `satellites` int,                                    
   `hdop` double,                                       
   `age_of_read` int,                                   
   `distance_since_read` int,                           
   `is_avl` boolean,                                    
   `odometer` double,                                   
   `coord_valid` boolean,                               
   `event_year` int,                                    
   `event_month` int,                                   
   `event_day` int,                                     
   `event_hour` int,                                    
   `event_minute` int,                                  
   `event_second` bigint,                               
   `event_msecond` bigint,                              
   `geohash1` string,                                   
   `geohash2` string,                                   
   `geohash3` string,                                   
   `geohash4` string,                                   
   `geohash5` string,                                   
   `geohash6` string,                                   
   `geohash7` string,                                   
   `geohash8` string,                                   
   `geohash9` string,                                   
   `geohash10` string,                                  
   `geohash11` string,                                  
   `geohash12` string)                                  
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location '/vr-input-files/fact_gps_pos/'
tblproperties ("skip.header.line.count"="1");


DROP TABLE @datalake.input.table.factGPSPos@;
CREATE TABLE @datalake.input.table.factGPSPos@(                           
   `subscription_id` int,                               
   `external_id` bigint,                                
   `ingested_time` timestamp,                           
   `persisted_time` timestamp,                          
   `vehicle_id` int,                                    
   `lineage_code` int,                                  
   `driver_id` int,                                     
   `driver_orig_id` int,                                
   `event_time` timestamp,                              
   `latitude` double,                                   
   `longitude` double,                                  
   `altitude` int,                                      
   `velocity` int,                                      
   `block_seq` int,                                     
   `heading` int,                                       
   `satellites` int,                                    
   `hdop` double,                                       
   `age_of_read` int,                                   
   `distance_since_read` int,                           
   `is_avl` boolean,                                    
   `odometer` double,                                   
   `coord_valid` boolean,                               
   `event_year` int,                                    
   `event_month` int,                                   
   `event_day` int,                                     
   `event_hour` int,                                    
   `event_minute` int,                                  
   `event_second` bigint,                               
   `event_msecond` bigint,                              
   `geohash1` string,                                   
   `geohash2` string,                                   
   `geohash3` string,                                   
   `geohash4` string,                                   
   `geohash5` string,                                   
   `geohash6` string,                                   
   `geohash7` string,                                   
   `geohash8` string,                                   
   `geohash9` string,                                   
   `geohash10` string,                                  
   `geohash11` string,                                  
   `geohash12` string)                                  
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

INSERT OVERWRITE TABLE @datalake.input.table.factGPSPos@ SELECT * FROM fact_gps_pos_text_tmp;



DROP TABLE dim_event_desc_text_tmp;                           
CREATE EXTERNAL TABLE dim_event_desc_text_tmp(                           
   `subscription_id` int,                                 
   `external_id` bigint,                                  
   `ingested_time` timestamp,                             
   `persisted_time` timestamp,                            
   `version_id` string,                                   
   `description` string,                                  
   `eventtype` int,                                       
   `in_use` boolean,                                      
   `start_odo` boolean,                                   
   `start_position` boolean,                              
   `end_odo` boolean,                                     
   `end_position` boolean,                                
   `record_f3_count` boolean,                             
   `use_warning_msg` boolean,                             
   `active_position` boolean,                             
   `record_time` int,                                     
   `alarm_time` int,                                      
   `relay_time` int,                                      
   `relay2_time` int,                                     
   `critical_time` int,                                   
   `active_time` int,                                     
   `active_end_time` int,                                 
   `track_time` int,                                      
   `track_interval` int,                                  
   `alarm_state` int,                                     
   `relay_state` int,                                     
   `relay2_state` int,                                    
   `critical_id` string,                                  
   `warning_message` string,                              
   `summary_type` int,                                    
   `summary_id` int,                                      
   `priority` int,                                        
   `event_save_id` int,                                   
   `updated` timestamp,                                   
   `notes` string)                                      
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
STORED AS TEXTFILE
location '/vr-input-files/dim_event_desc/';


DROP TABLE @datalake.input.table.dimEventDesc@;                           
CREATE TABLE @datalake.input.table.dimEventDesc@(                           
   `subscription_id` int,                                 
   `external_id` bigint,                                  
   `ingested_time` timestamp,                             
   `persisted_time` timestamp,                            
   `version_id` string,                                   
   `description` string,                                  
   `eventtype` int,                                       
   `in_use` boolean,                                      
   `start_odo` boolean,                                   
   `start_position` boolean,                              
   `end_odo` boolean,                                     
   `end_position` boolean,                                
   `record_f3_count` boolean,                             
   `use_warning_msg` boolean,                             
   `active_position` boolean,                             
   `record_time` int,                                     
   `alarm_time` int,                                      
   `relay_time` int,                                      
   `relay2_time` int,                                     
   `critical_time` int,                                   
   `active_time` int,                                     
   `active_end_time` int,                                 
   `track_time` int,                                      
   `track_interval` int,                                  
   `alarm_state` int,                                     
   `relay_state` int,                                     
   `relay2_state` int,                                    
   `critical_id` string,                                  
   `warning_message` string,                              
   `summary_type` int,                                    
   `summary_id` int,                                      
   `priority` int,                                        
   `event_save_id` int,                                   
   `updated` timestamp,                                   
   `notes` string)                                      
STORED AS ORC TBLPROPERTIES ("auto.purge"="true");

INSERT OVERWRITE TABLE @datalake.input.table.dimEventDesc@ SELECT * FROM dim_event_desc_text_tmp;


   
