package com.epam.tcodata.external.pump.util;

import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.external.pump.exception.WrongFieldEnrichmentException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class KeyManagerUtilTest {

    private Map<EntityType, List<SearchingResult>> entitySearchingResultMap = new HashMap<>();
    private List<SearchingResult> searchingResultList = new ArrayList<>();

    @Before
    public void initialize() {
        entitySearchingResultMap.clear();
        searchingResultList.clear();
    }

    @Test
    public void enrichEntityFieldsTest() throws IllegalAccessException {
        UUID driverSurrogateKey = UUID.randomUUID();
        UUID vehicleSurrogateKey = UUID.randomUUID();
        EnrichedPosition enrichedPosition = new EnrichedPosition();

        searchingResultList.add(new SearchingResult("driverDurableKey", driverSurrogateKey));
        entitySearchingResultMap.put(EntityType.POSITION, searchingResultList);
        KeyManagerUtil.enrichEntity(enrichedPosition, entitySearchingResultMap);

        searchingResultList.clear();
        searchingResultList.add(new SearchingResult("vehicleDurableKey", vehicleSurrogateKey));
        KeyManagerUtil.enrichEntity(enrichedPosition, entitySearchingResultMap);

        assertEquals(driverSurrogateKey.toString(), enrichedPosition.getDriverDurableKey());
        assertEquals(vehicleSurrogateKey.toString(), enrichedPosition.getVehicleDurableKey());
    }

    @Test
    public void enrichEntityWithParentFieldsTest() throws IllegalAccessException {
        UUID driverSurrogateKey = UUID.randomUUID();
        UUID vehicleSurrogateKey = UUID.randomUUID();
        UUID formattedAddressSurrogateKey = UUID.randomUUID();
        EnrichedPosition enrichedPosition = new EnrichedPosition();

        searchingResultList.add(new SearchingResult("driverDurableKey", driverSurrogateKey));
        entitySearchingResultMap.put(EntityType.POSITION, searchingResultList);
        KeyManagerUtil.enrichEntity(enrichedPosition, entitySearchingResultMap);

        searchingResultList.clear();
        searchingResultList.add(new SearchingResult("vehicleDurableKey", vehicleSurrogateKey));
        entitySearchingResultMap.put(EntityType.POSITION, searchingResultList);
        KeyManagerUtil.enrichEntity(enrichedPosition, entitySearchingResultMap);

        searchingResultList.clear();
        searchingResultList.add(new SearchingResult("formattedAddress", formattedAddressSurrogateKey));
        entitySearchingResultMap.put(EntityType.POSITION, searchingResultList);
        KeyManagerUtil.enrichEntity(enrichedPosition, entitySearchingResultMap);


        assertEquals(driverSurrogateKey.toString(), enrichedPosition.getDriverDurableKey());
        assertEquals(vehicleSurrogateKey.toString(), enrichedPosition.getVehicleDurableKey());
        assertEquals(formattedAddressSurrogateKey.toString(), enrichedPosition.getFormattedAddress());
    }

    @Test(expected = WrongFieldEnrichmentException.class)
    public void enrichEntityUnknowsFieldsTest() throws IllegalAccessException {
        UUID unknownFieldSurrogateId = UUID.randomUUID();

        searchingResultList.add(new SearchingResult("unknownField", unknownFieldSurrogateId));

        entitySearchingResultMap.put(EntityType.POSITION, searchingResultList);

        EnrichedPosition enrichedPosition = new EnrichedPosition();

        KeyManagerUtil.enrichEntity(enrichedPosition, entitySearchingResultMap);
    }
}
