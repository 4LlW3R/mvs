package com.epam.tcodata.mock.external.pump.util.misc;

import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.domain.pumps.OrganisationGroup;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import com.epam.tcodata.sql.dal.service.pumps.IMixOffsetService;
import com.epam.tcodata.sql.dal.service.pumps.IOrganisationGroupService;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class TestDaoUtil {
    private TestDaoUtil() {}

    /**
     * Populate the database with initial data.
     * @summary Summary for this method
     * @param daoFactory factory that manages DB.
     * @throws IOException
     */
    private static final String ORGANISATION_GROUP = "OrganisationGroup";
    private static final String PAKISTAN_STANDARD_TIME = "Pakistan Standard Time";
    private static final String TIME_00_02_00 = "00:02:00";

    /**
     * This method populates database.
     * @param daoFactory
     * @throws IOException
     */
    public static void populateDatabase(IDaoFactory daoFactory) {
        IAccountService accountService = IDaoFactory.service(daoFactory, Account.class);
        Account account1 = new Account(-1, "vadim_volkov@epam.com", "vadim_volkov", true);
        Account account2 = new Account(-1, "Nikita_Poberezkin@epam.com", "nikita-poberezkin", true);
        accountService.insert(Arrays.asList(account1, account2));

        IOrganisationGroupService organisationGroupService = IDaoFactory.service(daoFactory, OrganisationGroup.class);
        OrganisationGroup group1 = new OrganisationGroup(
                -1,
                account1.getId(),
                -8655792887104031000L,
                ORGANISATION_GROUP,
                PAKISTAN_STANDARD_TIME,
                "BTS - Weatherford Kazakhstan (Tcon)",
                true);
        OrganisationGroup group2 = new OrganisationGroup(
                -1,
                account1.getId(),
                -8463266206350883000L,
                ORGANISATION_GROUP,
                PAKISTAN_STANDARD_TIME,
                "BTS - Sicim Kazakhstan (Tcon)",
                true);
        OrganisationGroup group3 = new OrganisationGroup(
                -1,
                account2.getId(),
                -8463266206350883000L,
                ORGANISATION_GROUP,
                PAKISTAN_STANDARD_TIME,
                "BTS - Weatherford Kazakhstan (Tcon)",
                true);
        OrganisationGroup group4 = new OrganisationGroup(
                -1,
                account2.getId(),
                -8300599461629977000L,
                ORGANISATION_GROUP,
                PAKISTAN_STANDARD_TIME,
                "BTS - Sicim Kazakhstan (Tcon)",
                true);
        organisationGroupService.insert(Arrays.asList(group1, group2));
        organisationGroupService.insert(Arrays.asList(group3, group4));

        IAccountTokensService accountTokensService = IDaoFactory.service(daoFactory, AccountTokens.class);
        AccountTokens token1 = new AccountTokens(
                -1,
                account1.getId(),
                "#access_token_1",
                "#refresh_token_1",
                Instant.now(),
                Instant.now().plus(10, ChronoUnit.DAYS));
        AccountTokens token2 = new AccountTokens(
                -1,
                account2.getId(),
                "#access_token_2",
                "#refresh_token_2",
                Instant.now(),
                Instant.now().plus(10, ChronoUnit.DAYS));
        accountTokensService.insert(Arrays.asList(token1, token2));

        IMixOffsetService<MixOffset> mixOffsetService = IDaoFactory.service(daoFactory, MixOffset.class);
        MixOffset mixOffset1 = new MixOffset(
                -1,
                group1.getId(),
                EntityType.POSITION.getCode(),
                EntitySuperType.FACT.getCode(),
                Instant.parse("2019-01-01T00:00:00Z"),
                Instant.parse("2019-01-01T00:00:00Z"), // from
                200,
                0L,
                Time.valueOf(TIME_00_02_00),
                0L,
                null);
        MixOffset mixOffset2 = new MixOffset(
                -1,
                group2.getId(),
                EntityType.POSITION.getCode(),
                EntitySuperType.FACT.getCode(),
                Instant.parse("2019-01-02T00:00:00Z"),
                Instant.parse("2019-01-02T00:00:00Z"), // from
                200,
                0L,
                Time.valueOf(TIME_00_02_00),
                0L,
                null);
        MixOffset mixOffset3 = new MixOffset(
                -1,
                group3.getId(),
                EntityType.POSITION.getCode(),
                EntitySuperType.FACT.getCode(),
                Instant.parse("2019-01-03T00:00:00Z"),
                Instant.parse("2019-01-03T00:00:00Z"), // from
                200,
                0L,
                Time.valueOf(TIME_00_02_00),
                0L,
                null);
        MixOffset mixOffset4 = new MixOffset(
                -1,
                group4.getId(),
                EntityType.POSITION.getCode(),
                EntitySuperType.FACT.getCode(),
                Instant.parse("2019-01-04T00:00:00Z"),
                Instant.parse("2019-01-04T00:00:00Z"), // from
                200,
                0L,
                Time.valueOf(TIME_00_02_00),
                0L,
                null);
        mixOffsetService.insert(Arrays.asList(mixOffset1, mixOffset2, mixOffset3, mixOffset4));
    }


    /**
     * Clear the whole database.
     *
     * @param daoFactory factory that controls database.
     */
    public static void clearDatabase(IDaoFactory daoFactory) {
        IAccountService accountService = IDaoFactory.service(daoFactory, Account.class);
        accountService.deleteAll();

        IOrganisationGroupService organisationGroupService = IDaoFactory.service(daoFactory, OrganisationGroup.class);
        organisationGroupService.deleteAll();

        IAccountTokensService accountTokensService = IDaoFactory.service(daoFactory, AccountTokens.class);
        accountTokensService.deleteAll();

        IMixOffsetService<MixOffset> mixOffsetService = IDaoFactory.service(daoFactory, MixOffset.class);
        mixOffsetService.deleteAll();
    }
}
