package com.epam.tcodata.secure.storage.dal;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SecretTest {

    private static final String SECRET_HIVE_RAW_CONNECT = "Secret-Hive-RAW-connect";
    private static final String SECRET_EVENTHUB_RAW_ACCESS_KEY = "Secret-EventHub-Raw-accessKey";
    private static final String SECRET_MIX_COMMON_CLIENT_ID = "Secret-Mix-COMMON-clientId";

    @Test
    public void simpleIdentifiersTest() {
        assertEquals("Secret-Sql-PUMPS-user", Secret.Sql.PUMPS.user.buildSecretFullName());
        assertEquals("Secret-Sql-MDM-password", Secret.Sql.MDM.password.buildSecretFullName());
        assertEquals(SECRET_HIVE_RAW_CONNECT, Secret.Hive.RAW.connect.buildSecretFullName());
        assertEquals(SECRET_EVENTHUB_RAW_ACCESS_KEY, Secret.EventHub.Raw.accessKey.buildSecretFullName());
        assertEquals(SECRET_MIX_COMMON_CLIENT_ID, Secret.Mix.COMMON.clientId.buildSecretFullName());
        assertEquals("Secret-Mix-COMMON-clientSecret", Secret.Mix.COMMON.clientSecret.buildSecretFullName());
        assertEquals("Secret-Mix-ACCOUNT-name", Secret.Mix.ACCOUNT.name.buildSecretFullName());
        assertEquals("Secret-Redis-COMMON-accessKey", Secret.Redis.COMMON.accessKey.buildSecretFullName());
    }

    @Test
    public void identifiersSwitchedBySectionNameTest() {
        assertEquals(SECRET_HIVE_RAW_CONNECT, Secret.Hive.connect.usingSection("RAW").buildSecretFullName());
        assertEquals("Secret-Sql-MDM-user", Secret.Sql.user.usingSection("MDM").buildSecretFullName());
        assertEquals(SECRET_EVENTHUB_RAW_ACCESS_KEY, Secret.EventHub.accessKey.usingSection("Raw").buildSecretFullName());
        assertEquals(SECRET_MIX_COMMON_CLIENT_ID, Secret.Mix.clientId.usingSection("COMMON").buildSecretFullName());

        for (Secret.Sql.DB database : Secret.Sql.DB.values()) {
            String dbname = database.name();
            assertEquals("Secret-Sql-" + dbname + "-user", Secret.Sql.user.usingSection(dbname).buildSecretFullName());
            assertEquals("Secret-Sql-" + dbname + "-password", Secret.Sql.password.usingSection(dbname).buildSecretFullName());        }

        for (Secret.EventHub.NameSpace nameSpace : Secret.EventHub.NameSpace.values()) {
            String names = nameSpace.name();
            assertEquals("Secret-EventHub-" + names + "-accessKey", Secret.EventHub.accessKey.usingSection(names).buildSecretFullName());
        }

        for (String accountName : Arrays.asList("account1", "account2")) {
            assertEquals("Secret-Mix-ACCOUNT-name-" + accountName, Secret.Mix.name.usingSection(accountName).buildSecretFullName());
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidEnumCodeShouldThrowExceptionTest() {
        Secret.Hive.connect.usingSection("NOT_EXISING_ENUM_CODE");
    }

    @Test
    public void collectAllIdentifiersTest() {
        Set<ISecretIdentity> secretIdentitySet = Secret.collectAllIdentifiers();
        List<String> actual = secretIdentitySet.stream().map(ISecretIdentity::buildSecretFullName).collect(Collectors.toList());
        List<String> expected = Arrays.asList(
                "Secret-Sql-PUMPS-user",
                "Secret-Sql-PUMPS-password",
                "Secret-Sql-MDM-user",
                "Secret-Sql-MDM-password",
                "Secret-Sql-SPEEDLAYER-user",
                "Secret-Sql-SPEEDLAYER-password",
                SECRET_EVENTHUB_RAW_ACCESS_KEY,
                "Secret-EventHub-Overtaking-accessKey",
                "Secret-EventHub-RoadCondition-accessKey",
                "Secret-EventHub-ConfirmedOvertaking-accessKey",
                SECRET_HIVE_RAW_CONNECT,
                "Secret-Hive-RAW-key",
                "Secret-Hive-PREPARED-connect",
                "Secret-Hive-PREPARED-key",
                SECRET_MIX_COMMON_CLIENT_ID,
                "Secret-Mix-COMMON-clientSecret",
                "Secret-Mix-ACCOUNT-name-*",
                "Secret-Redis-COMMON-accessKey",
                "Secret-StorageAccount-MAIN-connectionString",
                "Secret-StorageAccount-LOGS-connectionString"
        );

        assertEquals(expected, actual);
    }

    @Test
    public void selectStaticIdentityTest() {
        ISecretIdentity expected = Secret.Sql.PUMPS.password;
        ISecretIdentity actual = Secret.selectByName("Secret-Sql-PUMPS-password");

        assertEquals(expected, actual);
    }

    @Test
    public void selectByNameDynamicTest() {
        ISecretIdentity expected = Secret.Mix.name.usingSection("James-Bond");
        ISecretIdentity actual = Secret.Mix.name.selectByName("Secret-Mix-ACCOUNT-name-James-Bond");

        assertNotNull(actual);
        assertEquals(expected.buildSecretFullName(), actual.buildSecretFullName());
    }

    @Test
    public void selectByNameTest() {
        Set<ISecretIdentity> iSecretIdentities = Secret.collectAllIdentifiers();
        Set<String> expected = iSecretIdentities.stream()
                .map(i -> i.buildSecretFullName().replace("*", "James_Bond"))
                .collect(Collectors.toSet());

        Set<String> actual = expected.stream()
                .map(n -> Secret.selectByName(n))
                .filter(Objects::nonNull)
                .map(i -> i.buildSecretFullName())
                .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    public void dumbSqlParametersMatchedSectionParametersTest() {
        Set<String> expected = Arrays.stream(Secret.Sql.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual1 = Arrays.stream(Secret.Sql.PUMPS.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual2 = Arrays.stream(Secret.Sql.MDM.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual3 = Arrays.stream(Secret.Sql.SPEEDLAYER.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertEquals(3, Secret.Sql.DB.values().length);
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertEquals(expected, actual3);
    }

    @Test
    public void dumbEventHubParametersMatchedSectionParametersTest() {
        Set<String> expected = Arrays.stream(Secret.EventHub.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual1 = Arrays.stream(Secret.EventHub.Raw.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual2 = Arrays.stream(Secret.EventHub.Overtaking.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual3 = Arrays.stream(Secret.EventHub.RoadCondition.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual4 = Arrays.stream(Secret.EventHub.ConfirmedOvertaking.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertEquals(4, Secret.EventHub.NameSpace.values().length);
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertEquals(expected, actual3);
        assertEquals(expected, actual4);
    }

    @Test
    public void dumbHiveParametersMatchedSectionParametersTest() {
        Set<String> expected = Arrays.stream(Secret.Hive.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual1 = Arrays.stream(Secret.Hive.RAW.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual2 = Arrays.stream(Secret.Hive.PREPARED.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertEquals(2, Secret.Hive.DB.values().length);
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }

    @Test
    public void dumbMixParametersMatchedSectionParametersTest() {
        Set<String> expected = Arrays.stream(Secret.Mix.values())
                .filter(e -> e != Secret.Mix.name)
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual1 = Arrays.stream(Secret.Mix.COMMON.values())
                .map(Enum::name)
                .collect(Collectors.toSet());


        assertEquals(1, Secret.Mix.Section.values().length);
        assertEquals(expected, actual1);
    }

    @Test
    public void dumbRedisParametersMatchedSectionParametersTest() {
        Set<String> expected = Arrays.stream(Secret.Redis.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        Set<String> actual1 = Arrays.stream(Secret.Redis.COMMON.values())
                .map(Enum::name)
                .collect(Collectors.toSet());


        assertEquals(1, Secret.Redis.Section.values().length);
        assertEquals(expected, actual1);
    }

    @Test
    public void accountNameTest() {
        ISecretIdentity accountName = Secret.Mix.name.usingSection("James_Bond");
        String expected = "Secret-Mix-ACCOUNT-name-James_Bond";
        assertEquals(expected, accountName.buildSecretFullName());
    }
}