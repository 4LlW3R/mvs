package com.epam.tcodata.external.pump.util;

import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.external.pump.exception.WrongFieldEnrichmentException;
import com.epam.tcodata.models.EntityType;

import java.lang.reflect.Field;
import java.util.*;

public class KeyManagerUtil {

    private KeyManagerUtil() {
    }

    /**
     * Enriches Facts with surrogate keys using reflection. Checks both class fields and superclass fields.
     *
     * @param entity             entity to enrich.
     * @param searchingResultMap surrogate key and name of the field to enrich.
     * @throws IllegalAccessException key manager configuration in database is invalid (unlikely).
     */
    public static void enrichEntity(Object entity, Map<EntityType, List<SearchingResult>> searchingResultMap)
            throws IllegalAccessException {
        enrichEntity(Arrays.asList(entity), searchingResultMap);
    }

    /**
     * Enriches Facts with surrogate keys using reflection. Checks both class fields and superclass fields.
     *
     * @param entities             entities to enrich.
     * @param searchingResultMap surrogate key and name of the field to enrich.
     * @throws IllegalAccessException key manager configuration in database is invalid (unlikely).
     */
    public static void enrichEntity(List<Object> entities, Map<EntityType, List<SearchingResult>> searchingResultMap)
            throws IllegalAccessException {

        for (int i = 0; i < entities.size(); ++i) {
            for (Map.Entry<EntityType, List<SearchingResult>> entry: searchingResultMap.entrySet()) {
                EntityType entityType = entry.getKey();
                List<SearchingResult> searchingResults = searchingResultMap.get(entityType);
                SearchingResult searchingResult = searchingResults.get(i);

                String fieldName = searchingResult.getName();
                UUID surrogateKey = searchingResult.getUuid();

                Object entity = entities.get(i);
                Class<?> clazz = entity.getClass();
                Field declaredField;
                try {
                    declaredField = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    try {
                        declaredField = clazz.getSuperclass().getDeclaredField(fieldName);
                    } catch (NoSuchFieldException ex) {
                        throw new WrongFieldEnrichmentException("Field to enrich wasn't found neither in class, "
                                + "nor in superclass. Field name: " + fieldName + ". Entity: " + entity);
                    }
                }

                declaredField.setAccessible(true);
                declaredField.set(entity, String.valueOf(surrogateKey));
            }
        }
    }
}
