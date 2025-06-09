#!/usr/bin/env bash

hdfs dfs -rm -r -skipTrash /vr-input-files/fact_gps_pos
hdfs dfs -mkdir /vr-input-files/fact_gps_pos
hdfs dfs -copyFromLocal ../golden_sets/fact_gps_pos_golden_set.csv /vr-input-files/fact_gps_pos


hdfs dfs -rm -r -skipTrash /vr-input-files/dim_event_desc
hdfs dfs -mkdir /vr-input-files/dim_event_desc
hdfs dfs -copyFromLocal ../golden_sets/dim_event_desc_golden_set.csv /vr-input-files/dim_event_desc 


hdfs dfs -rm -r -skipTrash /vr-input-files/fact_event
hdfs dfs -mkdir /vr-input-files/fact_event
hdfs dfs -copyFromLocal ../golden_sets/fact_event_golden_set.csv /vr-input-files/fact_event

hdfs dfs -rm -r -skipTrash /vr-input-files/fact_event_validated
hdfs dfs -mkdir /vr-input-files/fact_event_validated
hdfs dfs -copyFromLocal ../golden_sets/fact_event_validated_golden_set.csv /vr-input-files/fact_event_validated


beeline -u 'jdbc:hive2://headnodehost:10001/;transportMode=http' -f ../hive_db/generate_vr_input_golden_set_table.hql
beeline -u 'jdbc:hive2://headnodehost:10001/;transportMode=http' -f ../hive_db/generate_vr_output_golden_set_table.hql


sh run-recorded-events-validator-qa.sh
