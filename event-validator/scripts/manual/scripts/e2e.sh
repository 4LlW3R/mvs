#!/usr/bin/env bash

hdfs dfs -rmr -skipTrash /vr-input-files/fact_gps_pos
hdfs dfs -mkdir /vr-input-files/fact_gps_pos
hdfs dfs -copyFromLocal ./input_files/fact_gps_pos_golden_set.csv /vr-input-files/fact_gps_pos


hdfs dfs -rmr -skipTrash /vr-input-files/dim_event_desc
hdfs dfs -mkdir /vr-input-files/dim_event_desc
hdfs dfs -copyFromLocal ./input_files/dim_event_desc.csv /vr-input-files/dim_event_desc 


hdfs dfs -rmr -skipTrash /vr-input-files/fact_event
hdfs dfs -mkdir /vr-input-files/fact_event
hdfs dfs -copyFromLocal ./input_files/fact_event_golden_set.csv /vr-input-files/fact_event

beeline -u 'jdbc:hive2://headnodehost:10001/;transportMode=http' -f generate_input_tables.hql

sh run-recorded-events-validator-qa.sh
