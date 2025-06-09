#!/usr/bin/env bash

num_executors=3
executor_cores=3
driver_cores=2

#test env
jar_ver_suffix="1.8.0-RC"
jar_path="../../.."
spark_ver=$(ls /usr/hdp | grep -o '[0-9].*')
local_jar_path="/usr/hdp/${spark_ver}/spark2/jars"

#business-like params
app_name="RedisManager_Asset"
batchIntervalSeconds=600

/usr/hdp/${spark_ver}/spark2/bin/spark-submit \
--class com.epam.tcodata.redis.manager.RedisAssetStreamDriver \
--name ${app_name} \
--master yarn \
--deploy-mode cluster \
`# SPARK CONFIGURATION` \
--num-executors ${num_executors} \
--executor-cores ${executor_cores} \
--executor-memory 4G \
--driver-memory 4G \
--total-executor-cores $((${num_executors}*${executor_cores} + 1)) \
--conf spark.driver.cores=${driver_cores} \
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
${jar_path}/redis-manager-${jar_ver_suffix}.jar \
--appName ${app_name} \
--batchIntervalSeconds ${batchIntervalSeconds}
