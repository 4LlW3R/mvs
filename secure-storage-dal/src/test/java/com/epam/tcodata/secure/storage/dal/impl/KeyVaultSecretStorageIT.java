package com.epam.tcodata.secure.storage.dal.impl;

import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.Secret;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * For proper working of these tests the following system variables must be set:
 * CLIENT_ID - with proper client id for key vault on development environment
 * CLIENT_SECRET - with proper secret value for the same key vault.
 * Variables can be set both for user or for system.
 */
public class KeyVaultSecretStorageIT {

    ISecretIdentity userId = Secret.Sql.MDM.user;
    ISecretIdentity passwordId = Secret.Sql.MDM.password;
    final static String USER = "test-user";
    final static String PASSWORD = "test-password";

    final static String  SYSTEM_VAR_VAULT_BASE_URL = "VAULT_BASE_URL";
    final static String  SYSTEM_VAR_CLIENT_ID = "CLIENT_ID";
    final static String  SYSTEM_VAR_CLIENT_SECRET = "CLIENT_SECRET";
    final static String  SYSTEM_VAR_ENCRYPTION_KEY_ID = "ENCRYPTION_KEY_ID";

    KeyVaultSecretStorage secretStorage;

    @Before
    public void setUp() {
        Properties properties = new Properties();

        properties.setProperty(KeyVaultSecretStorage.VAULT_BASE_URL, System.getenv(SYSTEM_VAR_VAULT_BASE_URL));
        properties.setProperty(KeyVaultSecretStorage.CLIENT_ID,  System.getenv(SYSTEM_VAR_CLIENT_ID));
        properties.setProperty(KeyVaultSecretStorage.CLIENT_SECRET, System.getenv(SYSTEM_VAR_CLIENT_SECRET));
        properties.setProperty(KeyVaultSecretStorage.ENCRYPTION_KEY_ID, System.getenv(SYSTEM_VAR_ENCRYPTION_KEY_ID));

        this.secretStorage = new KeyVaultSecretStorage();
        this.secretStorage.init(properties);

        this.secretStorage.storeSecret(userId, USER);
        this.secretStorage.storeSecret(passwordId, PASSWORD);
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

    @Test
    public void collectPossibleIdentitiesTest() {
        Set<ISecretIdentity> actual = this.secretStorage.collectPossibleIdentities();
        Set<ISecretIdentity> expected = Secret.collectAllIdentifiers();
        assertEquals(expected, actual);
    }

    @Test
    @Ignore("Test takes a long time")
    public void retrieveStoredSecretsTest() {
        Map<String, String> secretsMap = this.secretStorage.retrieveStoredSecrets();
        assertEquals(USER, secretsMap.get(this.userId.buildSecretFullName()));
        assertEquals(PASSWORD, secretsMap.get(this.passwordId.buildSecretFullName()));
    }

    @Test
    public void retrieveAllNamesTest() {
        Set<String> names = this.secretStorage.retrieveAllNames();
        assertTrue(names.contains(this.userId.buildSecretFullName()));
        assertTrue(names.contains(this.passwordId.buildSecretFullName()));
    }
    
    @Test
    public void storeAndRetrieveSecretByNameTest() {
        String secret = "one small secret (test)";
        this.secretStorage.storeSecret(this.userId, secret);
        String actual = this.secretStorage.retrieveSecret(this.userId);
        assertEquals(secret, actual);
    }

    @Test
    public void storeMix() {
        this.secretStorage.storeSecret(Secret.Mix.COMMON.clientId, "epam.development");
        this.secretStorage.storeSecret(Secret.Mix.COMMON.clientSecret, "8NQff2f7Ku7pRaBu");
    }

    @Test
    public void storeRedis() {
        this.secretStorage.storeSecret(Secret.Redis.COMMON.accessKey, "LU9Kf1t1LCDucXqdFMILR6+vyp2ujG4rYFAGgEK7BMY=");
//NOSONAR        this.secretStorage.storeSecret(Secret.Redis.COMMON.ACCESS_KEY, "eInKLCvzSSeMy/EtGceDq5CByQI9etrU9mcgKdarRhPTkyyLqTUd+CQq0JWp70xJYHeE8oKdmUc9irBGY/DMhY6lptXwtts6XfDdj5mOf1sILm5BvniTrShjfW5kSezYNskJ0NgkM1YluNxxr/J3H6mTIXJx3tK8Pd0TE7Yn+ealA4lhXsj60Oixtv9GTamxefzq7TQMbKkghhKSYh0jSDu9Y1+pK9kKtwNzQyiZOmkXttwfBzZRYGjUjM3sJDWyQgr8YA6RhsdreEyJjG9YGj1VOXs/SRFcK+KUvyrAeK7sMzOSUR1pyTpLwgnG9Diz0+ingMO4/PgSIXaIavTsalM6TEv7t5umne0yFaxI8Nq6hgOdAapEok3Cb6/apw7akqoVEVFpHR3jA+OwEbknxSL137f5sivhRy0Mlb08vGb0jeyNPIiM3Fh5GTAfGd5QLDdhHeehXG/NY/G0c2rHBylTBXRiUSt7ft2hDB63mXD84GAkvmK10FvgMm1umuzusAwNyIU3Fcg+tnQKvjasp4Zdg99u26yP8j8ptaY7mBJxuS3sy51/nyY+4qNfI9lxzMVorl3Oh5uLPm3PaycjNQ4KyRqihHEbfmszhqwcv+PQt3rgiytVR/quOMTy+WK43FgMIQKGVYRW74nOvADQseML5+LupceHiBjXOrfu5HY=");
    }


    @Test
    public void storeSQL() {
        this.secretStorage.storeSecret(Secret.Sql.MDM.user, "dbadmin");
//NOSONAR        this.secretStorage.storeSecret(Secret.Sql.MDM.USER, "R4VSSyYFh+M7JJCLc4RlJf/lSIalDbM8GiL8WzgR9ntdAAA3CS5KMlbOMCeIj8tNnRp7Ps/G+3LZLlpRGPi4yMKp/o+xOjx5eRBDruVgCvnJmBJGHMnAakqjCyRSdWVw42Hrpq/RLTL+CqvBcpC+dOVyfbLLjKonC6HAHgPq+IY3BqqwLZjftTKB6eKiUPgib4pdLEM32rAy81XqR/7Rr921Rgwg46A7l9sbBY1JTxbbNg391z9dunXgDAUlQdJ0C2dilV7OrzqRmjoLzqIJX5YciQE/iYp0pNUOh014GS5QQKI8qQv7G6830iFQk7ERmB4b14zPYBgfCDzzSc3zj0Lg+Y5RAk1772PN6gKlIycjRgOSu7o8TWcJKTL754/iykkHvP9WDVPR5RPy/SLDrST8wKUuK/WA3telK0fWPadV2AVaziWhwZ/z2GX6C+INB0KImvJIBDSa8TXrbHjoKkeJWiep8I3t6LzONHEC0FOam8J4bSH+qO2Gt2llTdt9MLBmLyBRQjSdWTuHzb+YnPpbcpDkdAEQp538NETYkk54CPKpudTVTsjOPBxmWu4KMP9Fyn8H15GFeDqcYMxPenKOgyGIJ4Z0Fw9+o7TLefsGe/qtNcd6F+fNAcbm7qqHfDC3DTnUAQMqqL6uB9p9isoT0ObyAUMTkzXD6RCekhA=");
        this.secretStorage.storeSecret(Secret.Sql.MDM.password, "lYF3PP4#Ovq#CsU");
//NOSONAR        this.secretStorage.storeSecret(Secret.Sql.MDM.PASSWORD, "zuYtswJOk1CE4+SBe79gF9hVo6ISRPHzWaFGxlAgyJsLNi2vI6IwOAQFr8smq/QbKvY+pg4MR6uI0qDKH+mdzvEIwLM2/StIK6Hz/3dFFRs27PJhoTKtvFu45xmqyvmdtiQv05WvWZfpI7HFEehn2g3gLOYm5dF9/UMsrXffnxLyKT3rf1LXgzhHqrsNrj79hlhlWVxS790h5LXJbwvFeZ23TS6A6HSAToFMogGxgddc5Uf9Y9a6R8/9lUFqjUFqFrNxvKLusf0Pu9HjbjWX55VJO/mcJwTwI3j/2udcy97qYbtwER7K8+v0QPbyO6jthBORQncSIZ6+uyiSCKC1gRe+kOyx0SgEe1C3NWicgzpNGVG4anAJvMgyWuHP7qtxt24wi/s7M1A8SQPqszSXiars0bvdIx7wcIUbDyuGBViJyQ11WTmEpk0lupmJxoSSmcRhmmHxnqKgujrSe3DMF0ZRxM4ZHFC6iO/mDbtF6fM53Czw1kmX/5T5QqeDGOizZ4vo+u+91j/syTT9D2ZenXieSWHTVTxF1qgA3vR9+0zBJsH+wG98+dMk6fPHHt1r3d1ipvx9gpIFZEOv+YR7qHm6VMqYRYgPMd/pfmViurm26jZvsEvwp40i92eUrcFhIYyA1ZBfH65H2JPTaskgEF3mu9DoxcDTtDHbwgZTFSo=");


//NOSONAR        this.secretStorage.storeSecret(Secret.Sql.PUMPS.user, "sqladmin");
//NOSONAR        this.secretStorage.storeSecret(Secret.Sql.PUMPS.password, "TEaYcmY?8OKhpb7I");
//NOSONAR
//NOSONAR        this.secretStorage.storeSecret(Secret.Sql.SPEEDLAYER.user, "sqladmin");
//NOSONAR        this.secretStorage.storeSecret(Secret.Sql.SPEEDLAYER.password, "TEaYcmY?8OKhpb7I");
    }
}