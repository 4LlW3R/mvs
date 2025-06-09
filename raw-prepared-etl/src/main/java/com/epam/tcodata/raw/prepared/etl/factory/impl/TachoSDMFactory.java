package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedTacho;
import com.epam.tcodata.models.datalake.raw.fact.RawTacho;
import com.epam.tcodata.models.mix.fact.TachoInterval;
import com.epam.tcodata.models.mix.fact.TachoParameterDefinition;
import com.epam.tcodata.models.mix.fact.TachoParameterValue;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.api.java.JavaRDD;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TachoSDMFactory extends AbstractSDMFactory<RawTacho, PreparedTacho> {

    private static final long serialVersionUID = 6158883511519565639L;

    private static final String LINE_NAME = "F2";
    private ObjectMapper objectMapper;

    public TachoSDMFactory() {
        super(RawTacho.class, PreparedTacho.class);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ISingleDomainModelConverter<RawTacho, PreparedTacho> createConverter(ReferenceSupplier referenceSupplier) {
        return new ISingleDomainModelConverter<RawTacho, PreparedTacho>() {

            private static final long serialVersionUID = 3153962079518247134L;

            @Override
            public PreparedTacho convert(RawTacho raw) {
                String organizationDurableId = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());
                String assetDurableId = referenceSupplier.getAssetDurableId(raw.getAssetId());

                PreparedTacho res = new PreparedTacho();
                res.setDurableId(raw.getDurableId());
                res.setOrganizationDurableKey(organizationDurableId);
                res.setExternalId(null); //according to SDM document
                res.setPersistedDateUtc(raw.getPersistedDateUtc());
                res.setAssetId(raw.getAssetId());
                res.setDurableAssetId(assetDurableId);
                res.setLineName(LINE_NAME); //according to SDM document
                res.setStartDateTime(raw.getStartDateTime());
                res.setEndDateTime(raw.getEndDateTime());
                res.setYear(raw.getYear());
                res.setWeekNumber(raw.getWeekNumber());

                return res;
            }

            @Override
            public JavaRDD<PreparedTacho> convertRDD(JavaRDD<RawTacho> rawRDD) {
                return rawRDD.flatMap(raw -> {
                            List<TachoParameterDefinition> tachoParameterDefinitions =
                                    objectMapper.readValue(
                                            raw.getTachoParameterDefinitions(),
                                            new TypeReference<List<TachoParameterDefinition>>() {
                                            });

                            Optional<Integer> optionalTachoParameterDefinitionKey = tachoParameterDefinitions.stream()
                                    .filter(parameterDefinition -> parameterDefinition.getLineName().equals(LINE_NAME))
                                    .map(TachoParameterDefinition::getKey)
                                    .findFirst();

                            if (optionalTachoParameterDefinitionKey.isPresent()) {
                                Integer tachoParameterDefinitionKey = optionalTachoParameterDefinitionKey.get();

                                List<TachoInterval> tachoIntervals = objectMapper.readValue(
                                        raw.getTachoIntervals(),
                                        new TypeReference<List<TachoInterval>>() {
                                        });

                                return tachoIntervals.stream()
                                        .map(interval -> {
                                            PreparedTacho preparedTacho = convert(raw);
                                            preparedTacho.setValueDateTime(interval.getIntervalDateTime());

                                            Integer value = interval.getData().stream()
                                                    .filter(parameterValue ->
                                                            parameterValue.getKey().equals(tachoParameterDefinitionKey))
                                                    .map(TachoParameterValue::getValue)
                                                    .findFirst()
                                                    .get();
                                            preparedTacho.setValue(value);
                                            return preparedTacho;
                                        }).iterator();
                            }

                            return Collections.emptyIterator();
                        }
                );
            }
        };
    }
}
