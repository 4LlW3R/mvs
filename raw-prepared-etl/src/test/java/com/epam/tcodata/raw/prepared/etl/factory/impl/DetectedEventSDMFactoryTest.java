package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.dimension.PreparedGroup;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;
import com.epam.tcodata.models.datalake.prepared.statics.GroupType;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationSubGroup;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import scala.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DetectedEventSDMFactoryTest implements Serializable {

    private static final long serialVersionUID = 1429332681838919250L;

    private static SparkSession sparkSession;

    @BeforeClass
    public static void setUpClass() {
        sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName("app-name")
                        .setMaster("local"))
                .getOrCreate();
    }


    @Test
    public void rddRawDetectedEventConverterTest() {
        JavaRDD<RawDetectedEvent> rawEventRDD = JavaSparkContext.fromSparkContext(sparkSession.sparkContext()).parallelize(rawDataWithRawDetectedEvent());

        DetectedEventSDMFactory factory = new DetectedEventSDMFactory();
        ReferenceSupplier referenceSupplier = createTestReferenceSupplier();
        ISingleDomainModelConverter<RawDetectedEvent, PreparedEvent> converter = factory.createConverter(referenceSupplier);
        JavaRDD<PreparedEvent> preparedEventRDD = converter.convertRDD(rawEventRDD);
        JavaRDD<PreparedEvent> filteredPreparedEventRDD = preparedEventRDD.filter(prep -> prep.getViolationID() != null);

        long rawCount = rawEventRDD.count();
        long prepCount = preparedEventRDD.count();
        long filteredCount = filteredPreparedEventRDD.count();
        assertEquals(2L, rawCount);
        assertEquals(3L, prepCount);
        assertEquals(2L, filteredCount);
    }

    @Test
    public void rddRawOrganisationSubGroupConverterTest() {
        JavaRDD<RawOrganisationSubGroup> rawEventRDD = JavaSparkContext.fromSparkContext(sparkSession.sparkContext()).parallelize(rawDataWithOrgGroup());

        OrganisationGroupSDMFactory factory = new OrganisationGroupSDMFactory();
        ReferenceSupplier referenceSupplier = createTestReferenceSupplier();
        ISingleDomainModelConverter<RawOrganisationSubGroup, PreparedGroup> converter = factory.createConverter(referenceSupplier);
        JavaRDD<PreparedGroup> preparedEventRDD = converter.convertRDD(rawEventRDD);

        // Collect results after transformation.
        List<PreparedGroup> actual = preparedEventRDD.collect();

        // In this case the values should be different and can be collected as set.
        Set<String> stringCodes = actual.stream().map(group -> group.getGroupTypeCode()).collect(Collectors.toSet());

        // Checks.
        for (GroupType.Values value: GroupType.Values.values()) {
            assertEquals(
                    "Result set should contains " + value.getStringCode(),
                    true,
                    stringCodes.contains(value.getStringCode()));
        }
    }

    private ReferenceSupplier createTestReferenceSupplier() {
        ReferenceSupplier referenceSupplier = Mockito.mock(ReferenceSupplier.class);
        return referenceSupplier;
    }

    private List<RawOrganisationSubGroup> rawDataWithOrgGroup() {
        List<RawOrganisationSubGroup> organisationGroupList = new ArrayList<>();

        for (GroupType.Values value: GroupType.Values.values()) {
            organisationGroupList.add(createRawGroupWithType(value));
        }

        return organisationGroupList;
    }

    private RawOrganisationSubGroup createRawGroupWithType(GroupType.Values value) {
        RawOrganisationSubGroup group = new RawOrganisationSubGroup();
        group.setType(value.getStringCode());
        return group;
    }

    private List<RawDetectedEvent> rawDataWithRawDetectedEvent() {
        RawDetectedEvent event1 = new RawDetectedEvent();
        Integer[] violationIDs = new Integer[2];
        violationIDs[0] = 1;
        violationIDs[1] = 2;
        event1.setViolationIDs(violationIDs);

        RawDetectedEvent event2 = new RawDetectedEvent();
        event2.setRoadConditionId("id");

        List<RawDetectedEvent> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);

        return eventList;
    }
}
