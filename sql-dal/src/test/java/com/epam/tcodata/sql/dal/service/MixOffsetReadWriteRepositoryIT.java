package com.epam.tcodata.sql.dal.service;

import com.epam.tcodata.models.DataPumpResultCode;
import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import com.epam.tcodata.sql.dal.service.pumps.IMixOffsetService;
import com.epam.tcodata.sql.dal.util.PumpDbUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MixOffsetReadWriteRepositoryIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(MixOffsetReadOnlyRepositoryIT.class);

    private static final String RANDOM_DATE = "1900-01-01T00:30:00Z";

    private static IDaoFactory daoFactory = null;
    private static IMixOffsetService<MixOffset> mixOffsetService = null;

    @BeforeClass
    public static void setUp() {
        ISecretStorage secretStorage = mock(ISecretStorage.class);
        when(secretStorage.retrieveSecret(Secret.Sql.MDM.user)).thenReturn("user");
        when(secretStorage.retrieveSecret(Secret.Sql.MDM.password)).thenReturn("password");
        daoFactory = new PumpsDaoFactory(secretStorage);
        mixOffsetService = service(daoFactory, MixOffset.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        mixOffsetService.close();
        daoFactory.close();
    }

    @Before
    public void init() {
        PumpDbUtils.clearTables(daoFactory);
        PumpDbUtils.populateMixOffsetTable(daoFactory);
    }

    @After
    public void reset() {
        /***  Default implementation ***/
    }

    @Test
    public void connectionTest() {
        boolean opened = mixOffsetService.checkConnection();
        assertEquals(true, opened);
    }

    @Test
    public void insertMixOffsetListTest() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("OrganisationGroupId", 3);
        filter.put("EntityType", 7);

        List<MixOffset> mixOffsetList = createMixOffsetList();
        mixOffsetService.insert(mixOffsetList);

        List<MixOffset> mixOffsetList1 = mixOffsetService.readFiltered(filter);
        LOGGER.info("List: {}", mixOffsetList1);
    }

    @Test
    public void updateMixOffsetListTest() {
        List<MixOffset> mixOffsetList = mixOffsetService.readAll();

        mixOffsetList.get(0).setLastErrorMessage("New Error");
        mixOffsetService.update(mixOffsetList);

        mixOffsetList = mixOffsetService.readAll();

        assertEquals("New Error", mixOffsetList.get(0).getLastErrorMessage());
    }

    private List<MixOffset> createMixOffsetList() {
        List<MixOffset> mixOffsetList = new ArrayList<>();
        MixOffset mixOffset1 =
                new MixOffset(
                        7,
                        1,
                        EntityType.SUBTRIP.getCode(),
                        EntitySuperType.FACT.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf("00:02:00"),
                        3,
                        "exception1"
                );
        MixOffset mixOffset2 =
                new MixOffset(
                        8,
                        3,
                        EntityType.ASSET.getCode(),
                        EntitySuperType.DIMENSION.getCode(),
                        Instant.parse(RANDOM_DATE),
                        Instant.parse(RANDOM_DATE),
                        DataPumpResultCode.GOT_ALL_DATA.getCode(),
                        2,
                        Time.valueOf("00:02:00"),
                        3,
                        "exception2"
                );
        mixOffsetList.add(mixOffset1);
        mixOffsetList.add(mixOffset2);
        return mixOffsetList;
    }
}
