#!/usr/bin/env bash

num_executors=2
executor_cores=1
driver_cores=2

#test env
jar_ver_suffix="1.8.0-RC"
jar_path="../../../.."
spark_ver=$(ls /usr/hdp | grep -o '[0-9].*')
local_jar_path="/usr/hdp/${spark_ver}/spark2/jars"

#business-like params
driver_name="ExternalPumpStreamDriver"
app_name="ExternalPump_Trip"
batchIntervalSeconds=60
factoryClassName="com.epam.tcodata.external.pump.factory.impl.ExternalTripFactory"
dumpRdd="true"
additionalLogging="true"

/usr/hdp/${spark_ver}/spark2/bin/spark-submit \
--class com.epam.tcodata.external.pump.driver.${driver_name} \
--name ${app_name} \
--master yarn \
--deploy-mode cluster \
`# SPARK CONFIGURATION` \
--files ${jar_path}/log4j.properties \
--num-executors ${num_executors} \
--executor-cores ${executor_cores} \
--executor-memory 2G \
--driver-memory 2G \
--total-executor-cores $((${num_executors}*${executor_cores} + 1)) \
--conf spark.yarn.appMasterEnv.VAULT_BASE_URL=$VAULT_BASE_URL \
--conf spark.yarn.appMasterEnv.CLIENT_ID=$CLIENT_ID \
--conf spark.yarn.appMasterEnv.CLIENT_SECRET=$CLIENT_SECRET \
--conf spark.yarn.appMasterEnv.ENCRYPTION_KEY_ID=$ENCRYPTION_KEY_ID \
--conf spark.driver.cores=${driver_cores} \
--conf "spark.driver.extraJavaOptions=-Dlog.path=/var/log/spark2/${app_name} -Dlog4j.configuration=file:./log4j.properties " \
--conf "spark.executor.extraJavaOptions=-Dlog.path=/var/log/spark2/${app_name} -Dlog4j.configuration=file:./log4j.properties " \
--conf spark.task.maxFailures=8 `# Increase max task failures before failing job (Default: 4)` \
--conf spark.logConf=true `# Log Spark Configuration in driver log for troubleshooting` \
`# YARN CONFIGURATION` \
--conf spark.yarn.submit.waitAppCompletion=false \
--conf spark.driver.memoryOverhead=512 `# [Optional] Set if --driver-memory < 5GB` \
--conf spark.executor.memoryOverhead=1024 `# [Optional] Set if --executor-memory < 10GB` \
--conf spark.yarn.maxAppAttempts=4 `# Increase max application master attempts (derived from yarn.resourcemanager.am.max-attempts in YARN, which defaults to 2)` \
--conf spark.yarn.am.attemptFailuresValidityInterval=1h `# Attempt counter considers only the last hour (Default: (none))` \
--conf spark.yarn.max.executor.failures=$((8 * ${num_executors})) `# Increase max executor failures (Default: max(numExecutors * 2, 3))` \
--conf spark.yarn.executor.failuresValidityInterval=1h `# Executor failure counter considers only the last hour` \
`# application-arguments` \
${jar_path}/external-pumps-${jar_ver_suffix}.jar \
--appName ${app_name} \
--batchIntervalSeconds ${batchIntervalSeconds} \
--factoryClassName ${factoryClassName} \
--dumpRdd ${dumpRdd} \
--additionalLogging ${additionalLogging}
