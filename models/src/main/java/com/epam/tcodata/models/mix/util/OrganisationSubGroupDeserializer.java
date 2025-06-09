package com.epam.tcodata.models.mix.util;

import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroupCycle;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OrganisationSubGroupDeserializer extends StdDeserializer<List<OrganisationSubGroup>> {

    private static final long serialVersionUID = 4206745511431776442L;

    private ObjectMapper objectMapper;

    public OrganisationSubGroupDeserializer() {
        this(null);
    }

    /**
     * Main constructor.
     */
    public OrganisationSubGroupDeserializer(Class<?> vc) {
        super(vc);
        this.objectMapper = new ObjectMapper()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<OrganisationSubGroup> deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        OrganisationSubGroupCycle organisationSubGroupCycle =
                this.objectMapper.readValue(jsonparser, OrganisationSubGroupCycle.class);

        return handle(organisationSubGroupCycle);
    }

    private List<OrganisationSubGroup> handle(OrganisationSubGroupCycle top) {
        List<OrganisationSubGroup> organisationSubGroups = new ArrayList<>();
        Queue<Pair<Long, OrganisationSubGroupCycle>> queue = new LinkedList<>();
        queue.add(Pair.of(null, top));
        while (!queue.isEmpty()) {
            Pair<Long, OrganisationSubGroupCycle> elem = queue.poll();
            organisationSubGroups.add(handleCycle(elem));
            elem.getValue().getOrganisationSubGroupCycleList().forEach(child ->
                    queue.add(Pair.of(elem.getValue().getGroupId(), child)));
        }
        return organisationSubGroups;
    }

    private OrganisationSubGroup handleCycle(Pair<Long, OrganisationSubGroupCycle> cycle) {
        return new OrganisationSubGroup.OrganisationSubGroupBuilder()
                .setGroupId(cycle.getValue().getGroupId())
                .setParentSubGroupId(cycle.getKey())
                .setName(cycle.getValue().getName())
                .setType(cycle.getValue().getType())
                .build();
    }
}
