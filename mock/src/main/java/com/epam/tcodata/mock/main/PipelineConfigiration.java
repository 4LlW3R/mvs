package com.epam.tcodata.mock.main;

class PipelineConfigiration {

    public PipelineConfigiration() {
        /***  Default implementation ***/
    }

    private String sqlMdmKeyMapping;
    private String sqlPumpMixOffset;
    private String sqlPumpEventHubOffset;
    private String sqlPumpSignal;
    private String sqlSpeedLayerSpeedLayerPosition;
    private String sqlSpeedLayerSpeedLayerEvent;

    private String hiveRawPosition;
    private String hiveRawEvent;
    private String hiveRawTrip;
    private String hiveRawSubtrip;
    private String hiveRawDriver;
    private String hiveRawAsset;
    private String hiveRawLibraryEvent;
    private String hiveRawLocation;
    private String hiveRawOrganisationGroup;
    private String hiveRawOrganisationSubGroup;

    private String hivePreparedPosition;
    private String hivePreparedEvent;
    private String hivePreparedTrip;
    private String hivePreparedSubtrip;
    private String hivePreparedDriver;
    private String hivePrepareVehicle;
    private String hivePreparedLocation;
    private String hivePreparedGroup;

    String getSqlMdmKeyMapping() {
        return sqlMdmKeyMapping;
    }

    String getSqlPumpMixOffset() {
        return sqlPumpMixOffset;
    }

    String getSqlPumpEventHubOffset() {
        return sqlPumpEventHubOffset;
    }

    String getSqlPumpSignal() {
        return sqlPumpSignal;
    }

    String getSqlSpeedLayerSpeedLayerPosition() {
        return sqlSpeedLayerSpeedLayerPosition;
    }

    String getSqlSpeedLayerSpeedLayerEvent() {
        return sqlSpeedLayerSpeedLayerEvent;
    }

    String getHiveRawPosition() {
        return hiveRawPosition;
    }

    String getHiveRawEvent() {
        return hiveRawEvent;
    }

    String getHiveRawTrip() {
        return hiveRawTrip;
    }

    String getHiveRawSubtrip() {
        return hiveRawSubtrip;
    }

    String getHiveRawDriver() {
        return hiveRawDriver;
    }

    String getHiveRawAsset() {
        return hiveRawAsset;
    }

    String getHiveRawLibraryEvent() {
        return hiveRawLibraryEvent;
    }

    String getHiveRawLocation() {
        return hiveRawLocation;
    }

    String getHiveRawOrganisationGroup() {
        return hiveRawOrganisationGroup;
    }

    String getHiveRawOrganisationSubGroup() {
        return hiveRawOrganisationSubGroup;
    }

    String getHivePreparedPosition() {
        return hivePreparedPosition;
    }

    String getHivePreparedEvent() {
        return hivePreparedEvent;
    }

    String getHivePreparedTrip() {
        return hivePreparedTrip;
    }

    String getHivePreparedSubtrip() {
        return hivePreparedSubtrip;
    }

    String getHivePreparedDriver() {
        return hivePreparedDriver;
    }

    String getHivePrepareVehicle() {
        return hivePrepareVehicle;
    }

    String getHivePreparedLocation() {
        return hivePreparedLocation;
    }

    String getHivePreparedGroup() {
        return hivePreparedGroup;
    }
}
