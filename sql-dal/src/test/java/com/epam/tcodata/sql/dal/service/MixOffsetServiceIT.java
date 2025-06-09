package com.epam.tcodata.sql.dal.service;

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

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MixOffsetServiceIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(MixOffsetServiceIT.class);

    private static IDaoFactory daoFactory = null;
    private static IMixOffsetService<MixOffset> mixOffsetService = null;

    @BeforeClass
    public static void setUp() {
        ISecretStorage secretStorage = mock(ISecretStorage.class);
        when(secretStorage.retrieveSecret(Secret.Sql.PUMPS.user)).thenReturn("user");
        when(secretStorage.retrieveSecret(Secret.Sql.PUMPS.password)).thenReturn("password");
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
        LOGGER.info("Initializing connection");
    }

    @After
    public void reset() {
        /***  Default implementation ***/
    }

    @Test
    public void connectionTest() {
        boolean opened = mixOffsetService.checkConnection();
        assertTrue(opened);
        LOGGER.info("Connection is open");
    }

    @Test
    public void readMixOffsetMapTest() {
        Set<Long> orgIdSet = new HashSet<>();
        orgIdSet.add(1L);
        orgIdSet.add(2L);

        Map<Long, Optional<MixOffset>> offsetMapOpt =
                mixOffsetService.readMixOffsetMap(orgIdSet, EntityType.TRIP);

        offsetMapOpt.values()
                .forEach(opt -> assertTrue(opt.isPresent()));
    }
}
