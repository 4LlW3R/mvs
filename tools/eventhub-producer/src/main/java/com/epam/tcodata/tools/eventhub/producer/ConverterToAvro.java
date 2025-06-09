package com.epam.tcodata.tools.eventhub.producer;

import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.storage.avro.entities.events.overtaking.v2.OvertakingEventWithViolationsAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.v2.OvertakingPathSegmentAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.v2.PassingVehicleIdx;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Contains static methods for conversion from csv to avro.
 */
public class ConverterToAvro {
    private ConverterToAvro(){
        /***  Default implementation ***/
    }

    private static final DateTimeFormatter DWH_DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSS");

    static SpecificRecord positionCsvToAvro(CSVRecord csvRecord) {
        return AvroPosition.newBuilder()
                .setSubscriptionId(1L)
//                .setSubscriptionId(Long.parseLong(csvRecord.get("subscriptionid")))
                .setDurableId(UUID.randomUUID().toString())
//                .setDurableId(csvRecord.get("externalid"))
                .setIngestedDateUtc(DateTime.now())
//                .setIngestedDateUtc(DateTime.parse(csvRecord.get("ingesteddateutc"), DWH_DATETIME_FORMAT).withZoneRetainFields(DateTimeZone.UTC))
                .setVehicleDurableKey(csvRecord.get("ASSETID"))
                .setDriverDurableKey(csvRecord.get("DRIVERID"))
                .setTimestamp(DateTime.parse(csvRecord.get("TIMESTAMP"), DWH_DATETIME_FORMAT).withZoneRetainFields(DateTimeZone.UTC))
                .setLatitude(Double.parseDouble(csvRecord.get("LATITUDE")))
                .setLongitude(Double.parseDouble(csvRecord.get("LONGITUDE")))
                .setSpeedKilometresPerHour(Double.parseDouble(csvRecord.get("SPEEDKILOMETRESPERHOUR")))
                // just default values
                .setLineageCode(1)
                .setSchemaVersion(1)
                .setPositionId(Long.parseLong(csvRecord.get("POSITIONID")))
                .setAssetId(Long.parseLong(csvRecord.get("ASSETID")))
                .setDriverId(Long.parseLong(csvRecord.get("DRIVERID")))
                .setSpeedLimit(0.0)
//                .setSpeedLimit(Double.valueOf(csvRecord.get("SPEEDLIMIT")))
                .setAltitudeMetres(Integer.parseInt(csvRecord.get("ALTITUDEMETRES")))
                .setHeading(Integer.parseInt(csvRecord.get("HEADING")))
                .setNumberOfSatellites(Integer.valueOf(csvRecord.get("NUMBEROFSATELLITES")))
                .setHdop(Integer.parseInt(csvRecord.get("HDOP")))
                .setPdop(0)
//                .setPdop(Integer.parseInt(csvRecord.get("PDOP")))
                .setVdop(0)
//                .setVdop(Integer.parseInt(record.get("VDOP")))
                .setAgeOfReadingSeconds(Long.parseLong(record.get("AGEOFREADINGSECONDS")))
                .setDistanceSinceReadingKilometres(Integer.parseInt(record.get("DISTANCESINCEREADINGKILOMETRES")))
                .setIgnitionOn(Boolean.valueOf(record.get("IGNITIONON")))
                .setOdometerKilometres(Double.valueOf(record.get("ODOMETERKILOMETRES")))
                .setFormattedAddress(record.get("FORMATTEDADDRESS"))
                .setSource(record.get("SOURCE"))
                .setAvl(Boolean.valueOf(record.get("AVL")))
                .build();
    }

    /**
     * TO DO implement when needed.
     *
     * @param csvRecord CSVRecord
     * @return SpecificRecord
     */
    static SpecificRecord overtakingViolationCsvToAvro(CSVRecord csvRecord) {
        return OvertakingEventWithViolationsAvro.newBuilder()
                .setDomain(csvRecord.get("domain"))
                // just default values
                .setEntityType(1)
                .setSchemaVersion(1)
                .setId("1")
                .setPolicyVersion(1)
                .setVehicleDurableIdA("1")
                .setVehicleDurableIdB("1")
                .setDriverDurableIdA("1")
                .setDriverDurableIdB("1")
                .setVelocityA(1.0)
                .setVelocityB(1.0)
                .setTrajectoryA(OvertakingPathSegmentAvro.newBuilder()
                        .setEndLatitude(1.0)
                        .setEndLongitude(1.0)
                        .setEndTime(new DateTime())
                        .setStartLatitude(1.0)
                        .setStartLongitude(1.0)
                        .setStartTime(new DateTime())
                        .build())
                .setTrajectoryB(OvertakingPathSegmentAvro.newBuilder()
                        .setEndLatitude(1.0)
                        .setEndLongitude(1.0)
                        .setEndTime(new DateTime())
                        .setStartLatitude(1.0)
                        .setStartLongitude(1.0)
                        .setStartTime(new DateTime())
                        .build())
                .setTime(new DateTime())
                .setLatitude(1.0)
                .setLongitude(1.0)
                .setPolicyAreaIDs(new ArrayList<>())
                .setViolationIDs(new ArrayList<>())
                .setAOvertookB(PassingVehicleIdx.A_OVERTOOK_B)
                .build();
    }
}
