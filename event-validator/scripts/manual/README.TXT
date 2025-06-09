Testing Validation Rules Process

1.	Open the actual document with the validation rules. The latest version is located here. 
2.	Pull the golden sets and scripts from the following folder in Git ()
3.	Make corresponding changes in the golden set (files in the /home/sshadmin/validation_rules/input_files folder) – either add new records or adjust existing to cover all validation rules changes, the columns are marked in green in the golden data set files, setting the following fields, saving in the csv format
	a.	In the dim_event_desc file; 
		i.	descriptions (F field) 
		ii.	IDs for events (B field) reflecting numbers of rules in the document
	b.	In the fact_event_golden_set file; 
		“external_id” (T field) – reflecting numbers of rules in the document, the field will be used to identify results in the output data set 
		“event_id” (I field) – the column is used as a key to join with the dim_event_desc file (B field), reflecting numbers of rules in the document
		"value” field (W field) – reflecting a speed in time of event
		“start_gps_id” field (T field) – the column is used as a key to join with the fact_gps_pos_golden_set file (“external_id” field), reflecting numbers of rules in the document
	c.	In the fact_gps_pos_set file set ;
		“external_id” (B field) – the column is used as an external key to join with the fact_gps_pos_golden_set file 
		“velocity” field (M field) – reflecting a speed in time of gps position 
4.	Copy the adjusted CSV golden set files under the /home/sshadmin/validation_rules/input_files folder on the cluster
5.	Run the e2e.sh bash script in the /home/sshadmin/validation_rules folder to run test from the beginning to the end and update the output tables, the script will do the following actions 
		Refresh the golden set files in the HDFS 
		Re-generate external input hive tables (in the tcodata_e2e database) with the data from the golden set files
		Run the Spark job (Validation rule application) to generate output tables (test_db_e2e.fact_event_validated)
6.	Open Hive session by running the beeline.sh script in the /home/sshadmin/validation_rules folder 
7.	Check results in the output hive table (test_db_e2e.fact_event_validated) by running the following hive query, values in the validation_code and problem_vehicle fields should match with expected results in the document
	    select subscription_id, external_id, validation_code, problem_vehicle from fact_event_validated where subscription_id = 1452;


