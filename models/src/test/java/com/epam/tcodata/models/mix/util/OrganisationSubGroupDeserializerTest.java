package com.epam.tcodata.models.mix.util;

import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrganisationSubGroupDeserializerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationSubGroupDeserializerTest.class);
    private static final String SUBGROUPS = "\"SubGroups\": [],";
    private static final String TYPE_SITEGROUP = "\"Type\": \"SiteGroup\",";

    @Test
    public void deserializerTest() {
        String response = "{" +
                "\"GroupId\": -8655792887104030903," +
                "\"SubGroups\": [{" +
                "\"GroupId\": -8306432597325709434," +
                "\"SubGroups\": [{" +
                "\"GroupId\": 123," +
                SUBGROUPS +
               TYPE_SITEGROUP +
                "\"Name\": \"123\"" +
                "}]," +
                "\"Type\": \"DefaultSite\"," +
                "\"Name\": \"X - Unidentified Drivers\"" +
                "}," +
                "{" +
                "\"GroupId\": -7329612782321779053," +
                SUBGROUPS +
               TYPE_SITEGROUP +
                "\"Name\": \"Weatherford\"" +
                "}," +
                "{" +
                "\"GroupId\": -5335243818561250994," +
                SUBGROUPS +
               TYPE_SITEGROUP +
                "\"Name\": \"X - Decommissioned\"" +
                "}," +
                "{" +
                "\"GroupId\": -4562482005495805788," +
                SUBGROUPS +
               TYPE_SITEGROUP +
                "\"Name\": \"X - BTS\"" +
                "}" +
                "]," +
                "\"Type\": \"OrganisationGroup\"," +
                "\"Name\": \"BTS - Weatherford Kazakhstan (Tcon)\"" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        JavaType javaType = objectMapper.getTypeFactory().constructType(OrganisationSubGroup.class);

        List<OrganisationSubGroup> deserialized = null;
        try {
            deserialized = objectMapper.readValue(response, javaType);
            assertEquals(6, deserialized.size());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
