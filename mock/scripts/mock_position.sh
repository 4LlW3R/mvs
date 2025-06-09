#!/usr/bin/env bash

java -jar mock-1.8.0-RC.jar -currentMoment="2019-11-19T00:00:00Z" -entity=POSITION -interval=10 -launchFactor=2 -mockPort=1080  \
  -config=local-data/POSITION.json -restDir=local-data/rest/ -mdmBackup=local-data/db-backup/mdm -pumpBackup=local-data/db-backup/pump \
  -secureStorage=local-data/secret-storage.properties -dumpRddDirectory=temp/dump

