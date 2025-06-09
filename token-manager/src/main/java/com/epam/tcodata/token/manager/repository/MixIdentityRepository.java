package com.epam.tcodata.token.manager.repository;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.token.manager.domain.AccountCredentials;
import com.epam.tcodata.token.manager.domain.MixResponse;
import com.epam.tcodata.token.manager.exception.AccountTokensException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.io.CharStreams;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CPD-START")
public class MixIdentityRepository implements IMixIdentityRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MixIdentityRepository.class);

    private static final int CONNECT_TIMEOUT = 60000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;

    private static final String ENDPOINT = "https://identity.uk.mixtelematics.com/core/connect/token";
    private static final String SCOPE = "offline_access MiX.Integrate";

    private String mixClientId;
    private String mixClientSecret;

    /**
     * Repository class, that provides methods for creating or updating tokens in Mix Identity service.
     *
     * @param secretStorage service storage instance
     */
    public MixIdentityRepository(ISecretStorage secretStorage) {
        this.mixClientId = secretStorage.retrieveSecret(Secret.Mix.COMMON.clientId);
        this.mixClientSecret = secretStorage.retrieveSecret(Secret.Mix.COMMON.clientSecret);
    }

    @Override
    public String getMixIdentityTokenEndpoint() {
        return ENDPOINT;
    }

    @Override
    public String getMixScopes() {
        return SCOPE;
    }

    /**
     * Method creates new tokens using acoount credentials.
     *
     * @param accountCredentials account credentials
     * @return account tokens
     */
    @Override
    public AccountTokens createAccountTokens(AccountCredentials accountCredentials, Long accountId) {
        HttpPost post = new HttpPost(getMixIdentityTokenEndpoint());
        CloseableHttpResponse response = null;
        String responseContent = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "password"));
            params.add(new BasicNameValuePair("scope", getMixScopes()));
            params.add(new BasicNameValuePair("username", accountCredentials.getUsername()));
            params.add(new BasicNameValuePair("password", accountCredentials.getPwd()));
            params.add(new BasicNameValuePair("client_id", this.mixClientId));
            params.add(new BasicNameValuePair("client_secret", this.mixClientSecret));
            params.add(new BasicNameValuePair("override", "true"));
            post.setEntity(new UrlEncodedFormEntity(params));

            // setting timeouts to avoid lags when MIX doesn't respond (default timeout = infinite)
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .build();
            post.setConfig(config);

            response = client.execute(post);
            responseContent = inputStreamToString(response.getEntity().getContent());

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    MixResponse mixResponse = parseSuccessResponse(responseContent);
                    return new AccountTokens(
                            0,
                            accountId,
                            mixResponse.getAccessToken(),
                            mixResponse.getRefreshToken(),
                            Instant.now(),
                            Instant.now().plus(mixResponse.getExpiresIn(), ChronoUnit.SECONDS));
                default:
                    throw new AccountTokensException("Code " + response.getStatusLine().getStatusCode() + " received"
                            + "\nStatus message: " + response.getStatusLine().getReasonPhrase()
                            + "\nContent: " + responseContent);
            }
        } catch (IOException e) {
            LOGGER.error("createAccountTokens({}, {})", accountCredentials, accountId);
            LOGGER.error("HTTP request:  {}", requestDescription(post));
            LOGGER.error("HTTP response: {}", responceDescription(response, responseContent));
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException(e);
        } finally {
            post.releaseConnection();
        }
    }

    /**
     * Method updates tokens using existed refresh token.
     *
     * @param oldAccountTokens old account credentials
     * @return account tokens
     */
    @Override
    public AccountTokens updateAccountTokens(AccountTokens oldAccountTokens) {
        HttpPost post = new HttpPost(getMixIdentityTokenEndpoint());
        CloseableHttpResponse response = null;
        String responseContent = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            params.add(new BasicNameValuePair("access_token", oldAccountTokens.getAccessToken()));
            params.add(new BasicNameValuePair("refresh_token", oldAccountTokens.getRefreshToken()));
            params.add(new BasicNameValuePair("client_id", this.mixClientId));
            params.add(new BasicNameValuePair("client_secret", this.mixClientSecret));
            post.setEntity(new UrlEncodedFormEntity(params));

            // setting timeouts to avoid lags when MIX doesn't respond (default timeout = infinite)
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .build();
            post.setConfig(config);

            response = client.execute(post);
            responseContent = inputStreamToString(response.getEntity().getContent());

            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    MixResponse mixResponse = parseSuccessResponse(responseContent);
                    return new AccountTokens(
                            oldAccountTokens.getId(),
                            oldAccountTokens.getAccountId(),
                            mixResponse.getAccessToken(),
                            mixResponse.getRefreshToken(),
                            Instant.now(),
                            Instant.now().plus(mixResponse.getExpiresIn(), ChronoUnit.SECONDS));
                default:
                    throw new AccountTokensException("Code " + response.getStatusLine().getStatusCode() + " received"
                            + "\nStatus message: " + response.getStatusLine().getReasonPhrase()
                            + "\nContent: " + responseContent
                            + "\nTrying to create new tokens...");
            }
        } catch (IOException e) {
            LOGGER.error("updateAccountTokens failed");
//            LOGGER.debug("updateAccountTokens({})", oldAccountTokens);
//            LOGGER.debug("HTTP request:  {}", requestDescription(post));
//            LOGGER.debug("HTTP response: {}", responceDescription(response, responseContent));
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException(e);
        } finally {
            post.releaseConnection();
        }
    }

    private String requestDescription(HttpPost request) {
        if (request == null) {
            return null;
        }
        return String.format("%s, content: %s",
                request.toString(),
                request.getEntity());
    }

    private String responceDescription(CloseableHttpResponse response, String responceContent) {
        if (response == null) {
            return null;
        }
        return String.format("status code: %s, status message: %s, content: %s",
                response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase(),
                responceContent);
    }


    private String inputStreamToString(InputStream content) {
        try (Reader reader = new InputStreamReader(content, Charset.defaultCharset())) {
            return CharStreams.toString(reader);
        } catch (IOException e) {
            String msg = "Exception when parse response content from Mix.";
            LOGGER.error(msg, e);
            throw new IllegalArgumentException(msg, e);
        }
    }

    private MixResponse parseSuccessResponse(String content) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            return objectMapper.readValue(content, new TypeReference<MixResponse>() {
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException("Exception when map String content from Mix to MixResponse object.", e);
        }
    }
}
