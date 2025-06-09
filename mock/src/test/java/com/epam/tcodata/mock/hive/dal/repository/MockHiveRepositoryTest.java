package com.epam.tcodata.mock.hive.dal.repository;

import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import org.apache.spark.sql.Row;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MockHiveRepositoryTest {
    @Test
    public void mergeTest() {
        String partitionColumn = RawOrganisationGroup.Fields.GROUP_ID;
        String ingestedDateUtc = RawEntity.Fields.INGESTED_DATE_UTC;

        MockHiveRepository<RawOrganisationGroup> repository = new MockHiveRepository<>(RawAreaEntityType.ORGANISATION_GROUP, null);
        

        RawOrganisationGroup group1 = new RawOrganisationGroup();
        group1.setGroupId(100L);
        group1.setIngestedDateUtc(Timestamp.valueOf("2019-11-27 00:00:00"));
        RawOrganisationGroup group2 = new RawOrganisationGroup();
        group2.setGroupId(200L);
        group2.setIngestedDateUtc(Timestamp.valueOf("2019-11-26 00:00:00"));
        RawOrganisationGroup group3 = new RawOrganisationGroup();
        group3.setGroupId(200L);
        group3.setIngestedDateUtc(Timestamp.valueOf("2019-11-28 00:00:00"));
        RawOrganisationGroup group4 = new RawOrganisationGroup();
        group4.setGroupId(300L);
        group4.setIngestedDateUtc(Timestamp.valueOf("2019-11-29 00:00:00"));

        Row row1 = HiveCommon.entityToRow(group1);
        Row row2 = HiveCommon.entityToRow(group2);
        Row row3 = HiveCommon.entityToRow(group3);
        Row row4 = HiveCommon.entityToRow(group4);

        List<Row> list = new ArrayList<>();
        list.add(row1);
        list.add(row2);

        List<Row> union = new ArrayList<>();
        union.add(row3);
        union.add(row4);

        union.addAll(list);

        Map<Long, List<Row>> map = union.stream()
                .collect(Collectors.groupingBy(post -> post.getAs(partitionColumn)));
        List<Row> collect = map.entrySet().stream().map(e -> sortListByField(e.getValue(), ingestedDateUtc)).collect(Collectors.toList());

        List<Row> expected = Arrays.asList(row1, row3, row4);
        assertEquals(expected, collect);
    }

    private Row sortListByField(List<Row> list, String field) {

        Comparator<Row> comparator = Comparator.comparing(row -> row.getAs(field));
        ArrayList<Row> res = new ArrayList<>();
        res.addAll(list);
        res.sort(comparator.reversed());
        return res.get(0);
    }
}