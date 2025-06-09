@ECHO OFF
SET CLIENT_ID=<GUID with service principal in AAD>
SET CLIENT_SECRET=<PASSWORD FOR service principal in AAD>
SET VAULT_BASE_URL=https://VAULT-NAME.vault.azure.net
SET ENCRYPTION_KEY_ID=https://VAULT-NAME.vault.azure.net/keys/devkey/ENCRIPTION_KEY_GUID

SET TARGET=..\..\..\..\..\target
@rem 'validate' - validate checksums only
@rem java -cp %TARGET%\sql-dal-1.8.0-RC-shaded.jar com.epam.tcodata.sql.dal.Migration SPEEDLAYER validate

@rem 'status' - get list of not applied yet change sets
@rem java -cp %TARGET%\sql-dal-1.8.0-RC-shaded.jar com.epam.tcodata.sql.dal.Migration SPEEDLAYER status

@rem 'updateSQL' - show SQL to be run
java -cp %TARGET%\sql-dal-1.8.0-RC-shaded.jar com.epam.tcodata.sql.dal.Migration SPEEDLAYER updateSQL

@rem 'update' - run real update
@rem java -cp %TARGET%\sql-dal-1.8.0-RC-shaded.jar com.epam.tcodata.sql.dal.Migration SPEEDLAYER update
