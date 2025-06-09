package com.epam.tcodata.mock.sql.dal.impl.pumps;

import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.*;
import com.epam.tcodata.sql.dal.service.impl.pumps.MixOffsetService;
import com.epam.tcodata.sql.dal.service.impl.pumps.ValidatedEventTachoOffsetService;
import com.epam.tcodata.sql.dal.service.pumps.*;
import org.junit.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.*;

public class MockPumpDaoFactoryTest {

    private static IDaoFactory daoFactory = null;

    private static MixOffsetService mixOffsetService = null;
    private static IEventHubOffsetService eventHubOffsetService = null;
    private static IHiveOffsetService hiveOffsetService = null;
    private static IAccountService accountService = null;
    private static IOrganisationGroupService organisationGroupService = null;
    private static IAccountTokensService accountTokensService = null;
    private static ISignalService signalService = null;
    private static ValidatedEventTachoOffsetService validatedEventTachoOffsetService = null;
    private static final String ANOTHER = "another";
    private static final String UPDATED_NAME = "updated name";


    @BeforeClass
    public static void setUp() throws Exception {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        daoFactory = new MockPumpsDaoFactory(defaultMockSecretStorage);

        mixOffsetService = service(daoFactory, MixOffset.class);
        eventHubOffsetService = service(daoFactory, EventHubOffset.class);
        hiveOffsetService = service(daoFactory, HiveOffset.class);
        accountService = service(daoFactory, Account.class);
        organisationGroupService = service(daoFactory, OrganisationGroup.class);
        accountTokensService = service(daoFactory, AccountTokens.class);
        signalService = service(daoFactory, Signal.class);
        validatedEventTachoOffsetService = service(daoFactory, ValidatedEventTachoOffset.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        mixOffsetService.close();
        eventHubOffsetService.close();
        hiveOffsetService.close();
        accountService.close();
        organisationGroupService.close();
        accountTokensService.close();
        signalService.close();
        validatedEventTachoOffsetService.close();
        daoFactory.close();
    }

    @Before
    public void init() throws Exception {
        mixOffsetService.deleteAll();
        eventHubOffsetService.deleteAll();
        hiveOffsetService.deleteAll();
        accountService.deleteAll();
        organisationGroupService.deleteAll();
        accountTokensService.deleteAll();
        signalService.deleteAll();
        validatedEventTachoOffsetService.deleteAll();
    }

    @After
    public void reset() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void daoFactoryWithProperSecretStorageTest() {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        IDaoFactory testFactory2 = new MockPumpsDaoFactory(defaultMockSecretStorage);
        assertNotNull(testFactory2);
    }

    @Test
    public void MixOffsetServiceCRUD_Test() {
        MixOffsetService service = mixOffsetService;

        MixOffset entity = new MixOffset();
        entity.setOrganisationGroupId(100);
        entity.setEntityType(1);
        entity.setEntitySuperType(2);
        entity.setLastSyncDateUtc(Instant.now());
        entity.setLastProcessedTime(Instant.now());
        entity.setLastSyncResultCode(400);
        entity.setLastSyncElementCount(500);
        entity.setLastSyncDuration(Time.valueOf(LocalTime.now()));
        entity.setTotalElementsCount(1000);
        entity.setLastErrorMessage("last error message");
        long id = service.insert(entity);

        Optional<MixOffset> read1 = service.read(id);
        assertTrue(read1.isPresent());
        MixOffset inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setLastErrorMessage(ANOTHER);
        assertFalse(entity.equals(inserted));

        inserted.setLastErrorMessage(UPDATED_NAME);
        service.update(inserted);
        Optional<MixOffset> read2 = service.read(id);
        MixOffset updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<MixOffset> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<MixOffset> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void EventHubOffsetServiceCRUD_Test() {
        IEventHubOffsetService service = eventHubOffsetService;

        EventHubOffset entity = new EventHubOffset();
        entity.setPartitionId("00");
        entity.setEntityType(1);
        entity.setEntitySuperType(2);
        entity.setSeqNo(10000);
        entity.setLastSyncDateUtc(Instant.now());
        entity.setLastSyncElementCount(500);
        entity.setTotalElementsCount(1000);
        long id = service.insert(entity);

        Optional<EventHubOffset> read1 = service.read(id);
        assertTrue(read1.isPresent());
        EventHubOffset inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setPartitionId("99");
        assertFalse(entity.equals(inserted));

        inserted.setPartitionId("100");
        service.update(inserted);
        Optional<EventHubOffset> read2 = service.read(id);
        EventHubOffset updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<EventHubOffset> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<EventHubOffset> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void HiveOffsetServiceCRUD_Test() {
        IHiveOffsetService service = hiveOffsetService;

        HiveOffset entity = new HiveOffset();
        entity.setEntityType(1);
        entity.setPersistedDateUtc(Timestamp.from(Instant.now()));
        entity.setPreparedDateUtc(Timestamp.from(Instant.now()));
        entity.setValidatedDateUtc(Timestamp.from(Instant.now()));
        long id = service.insert(entity);

        Optional<HiveOffset> read1 = service.read(id);
        assertTrue(read1.isPresent());
        HiveOffset inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setEntityType(2);
        assertFalse(entity.equals(inserted));

        inserted.setEntityType(3);
        service.update(inserted);
        Optional<HiveOffset> read2 = service.read(id);
        HiveOffset updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<HiveOffset> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<HiveOffset> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void AccountServiceCRUD_Test() {
        IAccountService service = accountService;

        Account entity = new Account();
        entity.setAccountName("account name");
        entity.setAccountKeyVaultName("account key vault name");
        entity.setActive(true);
        long id = service.insert(entity);

        Optional<Account> read1 = service.read(id);
        assertTrue(read1.isPresent());
        Account inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setAccountName(ANOTHER);
        assertFalse(entity.equals(inserted));

        inserted.setAccountName(UPDATED_NAME);
        service.update(inserted);
        Optional<Account> read2 = service.read(id);
        Account updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<Account> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<Account> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void OrganisationGroupServiceCRUD_Test() {
        IOrganisationGroupService service = organisationGroupService;

        OrganisationGroup entity = new OrganisationGroup();
        entity.setAccountId(1000);
        entity.setGroupId(1);
        entity.setType("type");
        entity.setDisplayTimeZone("display time zone");
        entity.setName("name");
        entity.setActive(true);
        long id = service.insert(entity);

        Optional<OrganisationGroup> read1 = service.read(id);
        assertTrue(read1.isPresent());
        OrganisationGroup inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setName(ANOTHER);
        assertFalse(entity.equals(inserted));

        inserted.setName(UPDATED_NAME);
        service.update(inserted);
        Optional<OrganisationGroup> read2 = service.read(id);
        OrganisationGroup updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<OrganisationGroup> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<OrganisationGroup> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void AccountTokensServiceCRUD_Test() {
        IAccountTokensService service = accountTokensService;

        AccountTokens entity = new AccountTokens();
        entity.setAccountId(1000);
        entity.setAccessToken("access token");
        entity.setRefreshToken("refresh token");
        entity.setLastSyncDateUtc(Instant.now());
        entity.setExpirationDateUtc(Instant.now());
        long id = service.insert(entity);

        Optional<AccountTokens> read1 = service.read(id);
        assertTrue(read1.isPresent());
        AccountTokens inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setAccessToken(ANOTHER);
        assertFalse(entity.equals(inserted));

        inserted.setAccessToken(UPDATED_NAME);
        service.update(inserted);
        Optional<AccountTokens> read2 = service.read(id);
        AccountTokens updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<AccountTokens> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<AccountTokens> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void SignalServiceCRUD_Test() {
        ISignalService service = signalService;

        Signal entity = new Signal();
        entity.setApplicationType(1000);
        entity.setApplicationSuperType(2000);
        entity.setEntityType(1);
        entity.setEntitySuperType(10);
        entity.setTimestamp(Timestamp.from(Instant.now()));
        entity.setSignalType(500);
        entity.setMessage("message");
        long id = service.insert(entity);

        Optional<Signal> read1 = service.read(id);
        assertTrue(read1.isPresent());
        Signal inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setMessage(ANOTHER);
        assertFalse(entity.equals(inserted));

        inserted.setMessage(UPDATED_NAME);
        service.update(inserted);
        Optional<Signal> read2 = service.read(id);
        Signal updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<Signal> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<Signal> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void TachoMixOffsetServiceCRUD_Test() {
        ValidatedEventTachoOffsetService service = validatedEventTachoOffsetService;

        ValidatedEventTachoOffset entity = new ValidatedEventTachoOffset();
        entity.setSyncDateUtc(Timestamp.from(Instant.now()));
        entity.setFromPersistedDateUtc(Timestamp.from(Instant.now()));
        entity.setToPersistedDateUtc(Timestamp.from(Instant.now()));
        entity.setElementCount(100);
        entity.setSyncDuration(Time.valueOf("00:00:00"));
        long id = service.insert(entity);

        Optional<ValidatedEventTachoOffset> read1 = service.read(id);
        assertTrue(read1.isPresent());
        ValidatedEventTachoOffset inserted = read1.get();
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setSyncDuration(Time.valueOf("00:00:01"));
        assertFalse(entity.equals(inserted));

        inserted.setSyncDuration(Time.valueOf("00:00:02"));
        service.update(inserted);

        Optional<ValidatedEventTachoOffset> read2 = service.read(id);
        ValidatedEventTachoOffset updated = read2.get();
        assertNotSame(inserted, updated);
        assertEquals(inserted, updated);

        List<ValidatedEventTachoOffset> list = service.readAll();
        assertEquals(1, list.size());

        service.delete(id);
        Optional<ValidatedEventTachoOffset> read3 = service.read(id);
        assertFalse(read3.isPresent());
    }

    @Test
    public void AllServicesReadAllTest() {
        List<MixOffset> mixOffsets = mixOffsetService.readAll();
        List<EventHubOffset> eventHubOffsets = eventHubOffsetService.readAll();
        List<HiveOffset> hiveOffsets = hiveOffsetService.readAll();
        List<Account> accounts = accountService.readAll();
        List<OrganisationGroup> organisationGroups = organisationGroupService.readAll();
        List<AccountTokens> accountTokens = accountTokensService.readAll();
        List<Signal> signals = signalService.readAll();
        List<ValidatedEventTachoOffset> validatedEventTachoOffsets = validatedEventTachoOffsetService.readAll();

        assertNotNull(mixOffsets);
        assertNotNull(eventHubOffsets);
        assertNotNull(hiveOffsets);
        assertNotNull(accounts);
        assertNotNull(organisationGroups);
        assertNotNull(accountTokens);
        assertNotNull(signals);
        assertNotNull(validatedEventTachoOffsets);
    }
}
