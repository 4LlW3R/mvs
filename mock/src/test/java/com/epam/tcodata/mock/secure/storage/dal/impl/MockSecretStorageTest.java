package com.epam.tcodata.mock.secure.storage.dal.impl;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class MockSecretStorageTest {


    ISecretIdentity userId = Secret.Sql.MDM.user;
    ISecretIdentity passwordId = Secret.Sql.MDM.password;
    final static String USER = "value-Sql-MDM-user";
    final static String PASSWORD = "value-Sql-MDM-password";

    MockSecretStorage secretStorage;

    @Before
    public void setUp() {
        Properties properties = ResourceUtils.readProperties("mock-secret-storage.properties");
        this.secretStorage = new MockSecretStorage();
        this.secretStorage.init(properties);
    }

    @After
    public void tearDown() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void retrieveSecretTest() {
        String actualUser = this.secretStorage.retrieveSecret(this.userId);
        String actualPassword = this.secretStorage.retrieveSecret(this.passwordId);

        assertEquals(USER, actualUser);
        assertEquals(PASSWORD, actualPassword);
    }

    @Test(expected = SecretStorageException.class)
    public void retrieveNonExistingSecretTest() {
        this.secretStorage.retrieveSecret(Secret.Hive.RAW.key);
    }

    @Test
    public void collectPossibleIdentitiesTest() {
        Set<ISecretIdentity> actual = this.secretStorage.collectPossibleIdentities();
        Set<ISecretIdentity> expected = Secret.collectAllIdentifiers();
        assertEquals(expected, actual);
    }

    @Test
    public void retrieveStoredSecretsTest() {
        Map<String, String> secretsMap = this.secretStorage.retrieveStoredSecrets();
        assertEquals(USER, secretsMap.get(this.userId.buildSecretFullName()));
        assertEquals(PASSWORD, secretsMap.get(this.passwordId.buildSecretFullName()));
    }

    @Test
    public void retrieveAllNamesTest() {
        Set<String> expected = Arrays.asList(
                        "dummy",
                        "Secret-Sql-PUMPS-user",
                        "Secret-Sql-PUMPS-password",
                        "Secret-Sql-MDM-user",
                        "Secret-Sql-MDM-password",
                        "Secret-Sql-SPEEDLAYER-user",
                        "Secret-Sql-SPEEDLAYER-password",
                        "Secret-Mix-COMMON-clientId",
                        "Secret-Mix-COMMON-clientSecret",
                        "Secret-Mix-ACCOUNT-name-alexander-kochurin",
                        "Secret-Mix-ACCOUNT-name-vadim-volkov",
                        "Secret-Mix-ACCOUNT-name-nikita-poberezkin",
                        "Secret-Hive-PREPARED-connect",
                        "Secret-Hive-PREPARED-key")
                .stream().collect(Collectors.toSet());
        Set<String> actual = this.secretStorage.retrieveAllNames();
        assertEquals(expected, actual);
    }

    @Test
    public void storeAndRetrieveSecretByNameTest() {
        String secret = "one small secret (test)";
        this.secretStorage.storeSecret(this.userId, secret);
        String actual = this.secretStorage.retrieveSecret(this.userId);
        assertEquals(secret, actual);
    }
}