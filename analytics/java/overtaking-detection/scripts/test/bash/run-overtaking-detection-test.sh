#!/usr/bin/env bash

num_executors=3
executor_cores=3
driver_cores=3

#test env
jar_ver_suffix="1.8.0-RC"
jar_path="../../.."
spark_ver=$(ls /usr/hdp | grep -o '[0-9].*')
local_jar_path="/usr/hdp/${spark_ver}/spark2/jars"

#business-like params
driver_name="OvertakingDetectionDriver"
factory_class_name="com.epam.tcodata.analytics.overtaking.detection.factory.impl.OvertakingDetectionFactory"
app_name="Analytics_OvertakingDetection"
batchIntervalSeconds=210

/usr/hdp/${spark_ver}/spark2/bin/spark-submit \
--class com.epam.tcodata.analytics.overtaking.detection.${driver_name} \
--name ${app_name} \
--master yarn \
--deploy-mode cluster \
`# SPARK CONFIGURATION` \
--files ${jar_path}/log4j.properties \
--num-executors ${num_executors} \
--executor-cores ${executor_cores} \
--executor-memory 8G \
--driver-memory 8G \
--total-executor-cores $((${num_executors}*${executor_cores} + 1)) \
--conf spark.driver.cores=${driver_cores} \
--conf "spark.driver.extraJavaOptions=-Dlog.path=/var/log/spark2/${app_name} -Dlog4j.configuration=file:./log4j.properties " \
--conf "spark.executor.extraJavaOptions=-Dlog.path=/var/log/spark2/${app_name} -Dlog4j.configuration=file:./log4j.properties " \
--conf spark.task.maxFailures=2 `# Increase max task failures before failing job (Default: 4)` \
--conf spark.logConf=true `# Log Spark Configuration in driver log for troubleshooting` \
`# YARN CONFIGURATION` \
--conf spark.yarn.submit.waitAppCompletion=false \
--conf spark.yarn.driver.memoryOverhead=512 `# [Optional] Set if --driver-memory < 5GB` \
--conf spark.yarn.executor.memoryOverhead=1024 `# [Optional] Set if --executor-memory < 10GB` \
--conf spark.yarn.maxAppAttempts=2 `# Increase max application master attempts (needs to be <= yarn.resourcemanager.am.max-attempts in YARN, which defaults to 2) (Default: yarn.resourcemanager.am.max-attempts)` \
--conf spark.yarn.am.attemptFailuresValidityInterval=1h `# Attempt counter considers only the last hour (Default: (none))` \
--conf spark.yarn.max.executor.failures=2 `# Increase max executor failures (Default: max(numExecutors * 2, 3))` \
--conf spark.yarn.executor.failuresValidityInterval=1h `# Executor failure counter considers only the last hour` \
${jar_path}/overtaking-detection-${jar_ver_suffix}.jar \
`# application-arguments` \
--appName ${app_name} \
--factoryClassName ${factory_class_name} \
--mode stream \
--batchIntervalSeconds ${batchIntervalSeconds}
