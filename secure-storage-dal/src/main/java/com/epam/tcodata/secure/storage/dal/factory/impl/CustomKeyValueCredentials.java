package com.epam.tcodata.secure.storage.dal.factory.impl;

import com.microsoft.aad.adal4j.AuthenticationCallback;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.keyvault.authentication.KeyVaultCredentials;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class CustomKeyValueCredentials extends KeyVaultCredentials {

    private final String clientId;
    private final String clientSecret;


    public CustomKeyValueCredentials(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String doAuthenticate(String authorization, String resource, String scope) {
        ExecutorService executorService = null;
        try {
            executorService = Executors.newSingleThreadExecutor();
            AuthenticationContext context = new AuthenticationContext(authorization, false, executorService);
            ClientCredential credential = new ClientCredential(clientId, clientSecret);
            AtomicReference<Throwable> exception = new AtomicReference<>();
            Future<AuthenticationResult> authenticationResultFuture = context.acquireToken(resource, credential, new AuthenticationCallback() {
                @Override
                public void onSuccess(AuthenticationResult result) {
                    // do nothing. result will be returned any way
                }

                @Override
                public void onFailure(Throwable exc) {
                    exception.set(exc);
                }
            });
            AuthenticationResult result = authenticationResultFuture.get();
            if (exception.get() != null) {
                throw exception.get();
            }
            return result.getAccessToken();

            // we have to catch Throwable only because of weird signature of method onFailure(Throwable exc)
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }
}
