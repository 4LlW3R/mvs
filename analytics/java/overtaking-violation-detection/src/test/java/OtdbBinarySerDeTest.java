import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventWithViolationsAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingPathSegmentAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.PassingVehicleIdx;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroConfirmedOvertakingViolation;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroOvertakingParticipant;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroOvertakingPathSegment;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.SourceTypeCode;
import com.epam.tcodata.storage.events.DomainCode;
import com.epam.tcodata.storage.events.EventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

/**
 * Test container for unit tests covering binary interoperability with OTDB external system.
 * <p/>
 * 	Method name format within the test class: UnitOfWork_StateUnderTest_ExpectedBehavior
 * 	e.g., AddUser_ValidUserDetails_UserCanBeLoggedIn
 */
public class OtdbBinarySerDeTest {
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final Logger LOGGER = LoggerFactory.getLogger(OtdbBinarySerDeTest.class);

    /**
     * Capture reference binary sample as base64 string for DataLake->OTDB flow (OvertakingWithViolations message).
     */
    @Test
    public void serializeValidOvertakingWithViolationsInstanceExpectedByteArray() {
        //arrange
        final String expectedSerializedBufAsBase64String = "KGFuYWx5dGljcy5vdmVydGFraW5nBARIMENDNEVCQkUtNzJDMy00MUIyLUI4RTktRDAzQjJDODFBNjY4BgJIMDlDN0IzRjItQkVFRi00QTA1LThFMEEtQUNDNUJDNjM1M0RFSDdGQ0U1RTI5LTk3MkMtNDE4OC1BQjM3LUVCMTc3MDA3QTI1NkhDNUJEMzRCMS1BODYxLTQ3NzctQkI0NS03QkIyQUNGMUQ5RjNIREVFREE2MzgtMUI4My00QUU0LThCREMtMTQ1NDAzRjFCQUQw6Z86M79LUkBIIflamWZLQAAAAEDxHEdAAAAAYIKzSkCArYu551gAAABA/RtHQAAAAKD0sUpAkOaSuedYAAAAwLgcR0AAAAAAVLNKQOCKjLnnWAAAAKAOHEdAAAAAABqySkDww5O551iwlI6551ixXrQXkxxHQAzpCOPoskpABAIwAjEABgoIBAA=";

        OvertakingEventWithViolationsAvro inputSample = OvertakingEventWithViolationsAvro.newBuilder()
                .setDomain(DomainCode.DOMAIN_OVERTAKING.getDomain())
                .setEntityType(EventType.OVERTAKING_WITH_VIOLATIONS.getEventTypeId())
                .setSchemaVersion(2)
                .setId("0CC4EBBE-72C3-41B2-B8E9-D03B2C81A668")
                .setPolicyVersion(1)
                .setVehicleDurableIdA("09C7B3F2-BEEF-4A05-8E0A-ACC5BC6353DE")
                .setVehicleDurableIdB("7FCE5E29-972C-4188-AB37-EB177007A256")
                .setDriverDurableIdA("C5BD34B1-A861-4777-BB45-7BB2ACF1D9F3")
                .setDriverDurableIdB("DEEDA638-1B83-4AE4-8BDC-145403F1BAD0")
                .setVelocityA(73.183544928789743D)
                .setVelocityB(54.801555034299383D)
                .setTime(new DateTime(1525712799000L, DateTimeZone.UTC))
                .setLatitude(46.223238909789309D)
                .setLongitude(53.397732142782928D)
                .setTrajectoryA(
                        OvertakingPathSegmentAvro.newBuilder()
                                .setStartLatitude(46.226112365722656D)
                                .setStartLongitude(53.402416229248047D)
                                .setStartTime(new DateTime(1525712776000L, DateTimeZone.UTC))
                                .setEndLatitude(46.218666076660156D)
                                .setEndLongitude(53.390277862548828D)
                                .setEndTime(new DateTime(1525712837000L, DateTimeZone.UTC))
                                .build())
                .setTrajectoryB(
                        OvertakingPathSegmentAvro.newBuilder()
                                .setStartLatitude(46.224388122558594D)
                                .setStartLongitude(53.4010009765625D)
                                .setStartTime(new DateTime(1525712782000L, DateTimeZone.UTC))
                                .setEndLatitude(46.219196319580078D)
                                .setEndLongitude(53.39141845703125D)
                                .setEndTime(new DateTime(1525712843000L, DateTimeZone.UTC))
                                .build())
                .setPolicyAreaIDs(Arrays.asList("0", "1"))
                .setViolationIDs(Arrays.asList(5, 4, 2))
                .setAOvertookB(PassingVehicleIdx.NOT_DETECTED)
                .build();

        //act
        byte[] byteArray = AvroSerDeUtil.serialize(inputSample);
        byte[] base64EncodedData = Base64.getEncoder().encode(byteArray);
        String actualBase64String = new String(base64EncodedData, UTF8);

        //assert
        LOGGER.info("Encoded base64 string for OvertakingEventWithViolationsAvro = [{}]", actualBase64String);
        assertEquals(expectedSerializedBufAsBase64String, actualBase64String);
    }

    /**
     * Ensure that DataLake can deserialize dotNet-serialized message.
     * OTDB -> DataLake data flow (ConfirmedOvertakingEventWithViolations)
     * @throws java.io.IOException
     */
    @Test
    public void deserializeDotNetSerializedConfirmedOvertakingEventWithViolationsExpectedJavaInstance() throws java.io.IOException {
        //arrange
        final String inputSerializedBufAsBase64String = "CG90ZGICBEgyMzRGRUU0Ny00MUUxLTQ3MzUtQjQzNC04MUM0MzE1NUU3NTQCAkhDNUJEMzRCMS1BODYxLTQ3NzctQkI0NS03QkIyQUNGMUQ5RjNIREVFREE2MzgtMUI4My00QUU0LThCREMtMTQ1NDAzRjFCQUQwNtqJXWYJR0BIMENDNEVCQkUtNzJDMy00MUIyLUI4RTktRDAzQjJDODFBNjY4SDYxRTdFNzk2LUVBRkEtNDQ1MC05NjJDLThFNDhBMDkyRDREM3AEfZPAIkRABwAAoMkaR0D6//8fg7BKQPCmhev+WAMAAKDqGUdA////P2OwSkCA4Izr/lgFAACAHBtHQPn//192sEpAoKOB6/5YAwAAoG4aR0D///8/d7BKQODMiOv+WPCjhuv+WPBKBzapGkdAxzjpfX6wSkACAjAABgYIDABMQ29uZmlybWVkIHdpdGggdmlvbGF0aW9ucyBieSBKb2huIERvZS4+RHJpdmVyIHNlZW1zIHRvIGJlIGludG94aWNhdGVkLg==";
/*
extract from
TCO-MVS-OTDB\tests\Common\Examples.cs

				new com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.v2.ConfirmedOvertakingEventWithViolations
				{
					domain = "otdb",
					entityType = 1,
					schemaVersion = 2,
					id = "234FEE47-41E1-4735-B434-81C43155E754",
					policyVersion = 1,
					sourceType = com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.v2.SourceTypeCode.DETECTED,
					passingParticipant = new com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.v2.OvertakingParticipant
					{
						durableVehicleID = "C5BD34B1-A861-4777-BB45-7BB2ACF1D9F3",
						durableDriverID = "DEEDA638-1B83-4AE4-8BDC-145403F1BAD0",
						interpolatedOvertakingVelocity = 46.0734364436252D
					},
					passedParticipant = new com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.v2.OvertakingParticipant
					{
						durableVehicleID = "0CC4EBBE-72C3-41B2-B8E9-D03B2C81A668",
						durableDriverID = "61E7E796-EAFA-4450-962C-8E48A092D4D3",
						interpolatedOvertakingVelocity = 40.2715019569813D,
					},
					passingTrajectory = new com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.v2.OvertakingPathSegment
					{
						startLatitude = 46.2092781066895D,
						startLongitude = 53.3790016174316D,
						startTime = "2018-06-13T01:09:23.0000000Z".FromIso8601UtcString().ToPosixMilliseconds(),
						endLatitude = 46.2024726867676D,
						endLongitude = 53.3780288696289D,
						endTime = "2018-06-13T01:10:24.0000000Z".FromIso8601UtcString().ToPosixMilliseconds()
					},
					passedTrajectory = new com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.v2.OvertakingPathSegment
					{
						startLatitude = 46.2118072509766D,
						startLongitude = 53.3786125183105D,
						startTime = "2018-06-13T01:08:50.0000000Z".FromIso8601UtcString().ToPosixMilliseconds(),
						endLatitude = 46.2065010070801D,
						endLongitude = 53.3786392211914D,
						endTime = "2018-06-13T01:09:50.0000000Z".FromIso8601UtcString().ToPosixMilliseconds()
					},
					time = "2018-06-13T01:09:31.0000000Z".FromIso8601UtcString().ToPosixMilliseconds(),
					latitude = 46.2082889114007D,
					longitude = 53.3788602246073D,
					policyAreaIDs = new[] { "0" },
					violationIDs = new[] { 3, 4, 6 },
					unclassifiedViolationDescription = "Driver seems to be intoxicated.",
					validationComment = "Confirmed with violations by John Doe."
				};
*/
        AvroConfirmedOvertakingViolation expectedSample = AvroConfirmedOvertakingViolation.newBuilder()
                .setDomain("otdb")
                .setEntityType(1)
                .setSchemaVersion(2)
                .setId("234FEE47-41E1-4735-B434-81C43155E754")
                .setPolicyVersion(1)
                .setSourceType(SourceTypeCode.DETECTED)
                .setPassingParticipant(
                        AvroOvertakingParticipant.newBuilder()
                                .setVehicleDurableID("C5BD34B1-A861-4777-BB45-7BB2ACF1D9F3")
                                .setDriverDurableID("DEEDA638-1B83-4AE4-8BDC-145403F1BAD0")
                                .setInterpolatedOvertakingVelocity(46.0734364436252D)
                                .build())
                .setPassedParticipant(
                        AvroOvertakingParticipant.newBuilder()
                                .setVehicleDurableID("0CC4EBBE-72C3-41B2-B8E9-D03B2C81A668")
                                .setDriverDurableID("61E7E796-EAFA-4450-962C-8E48A092D4D3")
                                .setInterpolatedOvertakingVelocity(40.2715019569813D)
                                .build())
                .setPassingTrajectory(
                        AvroOvertakingPathSegment.newBuilder()
                                .setStartLatitude(46.2092781066895D)
                                .setStartLongitude(53.3790016174316D)
                                .setStartTime(getUtcTime("2018-06-13T01:09:23"))
                                .setEndLatitude(46.2024726867676D)
                                .setEndLongitude(53.3780288696289D)
                                .setEndTime(getUtcTime("2018-06-13T01:10:24"))
                                .build())
                .setPassedTrajectory(
                        AvroOvertakingPathSegment.newBuilder()
                                .setStartLatitude(46.2118072509766D)
                                .setStartLongitude(53.3786125183105D)
                                .setStartTime(getUtcTime("2018-06-13T01:08:50"))
                                .setEndLatitude(46.2065010070801D)
                                .setEndLongitude(53.3786392211914D)
                                .setEndTime(getUtcTime("2018-06-13T01:09:50"))
                                .build())
                .setTime(getUtcTime("2018-06-13T01:09:31"))
                .setLatitude(46.2082889114007D)
                .setLongitude(53.3788602246073D)
                .setPolicyAreaIds(Arrays.asList("0"))
                .setViolationIDs(Arrays.asList(3, 4, 6))
                .setUnclassifiedViolationDescription("Driver seems to be intoxicated.")
                .setValidationComment("Confirmed with violations by John Doe.")
                .build();

        //act
        byte[] inputByteArray = Base64.getDecoder().decode(inputSerializedBufAsBase64String);
        AvroConfirmedOvertakingViolation actualSample = AvroSerDeUtil.deserialize(AvroConfirmedOvertakingViolation.class, inputByteArray);

        //assert
        assertEquals(expectedSample, actualSample);
    }

    private static DateTime getUtcTime(String dt) {
        DateTime date = DateTime.parse(dt);
        LocalDateTime ldt = new LocalDateTime(date);
        return ldt.toDateTime(DateTimeZone.UTC);
    }

}
