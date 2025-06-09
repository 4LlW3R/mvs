#!/usr/bin/env bash

#Please start script without any args to see help manual

jar_ver_suffix="1.8.0-RC"
jar_path="."
spark_ver=$(ls /usr/hdp | grep -o '[0-9].*')
local_jar_path="/usr/hdp/${spark_ver}/spark2/jars/"

java -cp ${jar_path}/application-manager-${jar_ver_suffix}.jar\
com.epam.tcodata.application.manager.ApplicationManager \
$@
