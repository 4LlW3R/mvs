# Introduction
Code repository for TCO-DATA MVS project. 

# Code repository structure
* /devops - code for cluster automation 
    * /devops/certificates - generated certificates for interaction between Data Lake stores and HDIsnight clusters/Data Factory
    * /devops/dsc_configs - dsc for on-premises VMs 
    * /devops/parameters - files with parameters for runbooks 
    * /devops/runbooks - files with runbooks 
    * /devops/templates - files with templates for runbooks 
    * /devops/scripts - files with action scripts for HDInsight clusters
* /src/ - legacy .net data pump service (Azure Service Fabric)
* /analytics/* - data science algorithms 
* /azure - event hub trigger for RCDB
* /datalake/java - java Data Platform internal data pump
* /deployment - java Data Platform deployment module 
* /libs - java Data Platform shared libraries, data contracts, code style settings
* /redis-manager - java Data Platform Spark job to fill Redis caches
* /spark-data-pump - java Data Platform external data pump
* /specs - specifications (e.g. MiX-T legacy SOAP API wsdl specs)
* /speed-layer - java Data Platform speed layer
* /tools - Azure wrapper scripts and Data Quality automated tests
    * /tools/dq-testing - Data Quality automated tests
* /webui - web UI enabled additional applications
    * /webui/RCDB - RCDB .net application
    * /webui/OTDB - OTDB .net application

 
# Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.	Installation process
2.	Software dependencies
3.	Latest releases
4.	API references

# Build and Test
Command to build project:
``` 
mvn --settings settings.xml -DskipTests=false -Penv-dev clean install 
```
