#!/usr/bin/env bash

resource_group="mvs-dl-qa-t80kz-20180808"
azure_function_app="mvs-qa-tco1"
zip_path="./"
zip_ver_suffix="1.8.0-RC"

az functionapp deployment source config-zip -g ${resource_group} -n ${azure_function_app} --src ${zip_path}/rcdb-ingestion-csharp-${zip_ver_suffix}-zip.zip