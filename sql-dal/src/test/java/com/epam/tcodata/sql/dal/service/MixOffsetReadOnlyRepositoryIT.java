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
import java.util.List;
import java.util.Optional;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MixOffsetReadOnlyRepositoryIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(MixOffsetReadOnlyRepositoryIT.class);

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
        LOGGER.info("Size: {}", mixOffsetService.readAll().size());
        PumpDbUtils.populateMixOffsetTable(daoFactory);
    }

    @After
    public void reset() {
        /***  Default implementation ***/
    }

    @Test
    public void connectionTest() {
        boolean opened = mixOffsetService.checkConnection();
        assertTrue(opened);
    }

    @Test
    public void mixOffsetReadTest() {
        long id = mixOffsetService.insert(createMixOffset());

        Optional<MixOffset> mixOffset = mixOffsetService.read(id);
        assertTrue(mixOffset.isPresent() && mixOffset.get().getId() == id);
    }

    @Test
    public void mixOffsetReadAllTest() {
        List<MixOffset> mixOffsetList = mixOffsetService.readAll();
        assertNotNull(mixOffsetList);
    }

    private static MixOffset createMixOffset() {
        return new MixOffset(
                1,
                3,
                EntityType.POSITION.getCode(),
                EntitySuperType.FACT.getCode(),
                Instant.parse("1900-01-01T00:30:00Z"),
                Instant.parse("1900-01-01T00:30:00Z"),
                DataPumpResultCode.GOT_ALL_DATA.getCode(),
                2,
                Time.valueOf("00:02:00"),
                3,
                "exception1"
        );
    }
}
