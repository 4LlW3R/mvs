package com.epam.tcodata.external.pump.source;

import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.source.fact.AbstractFactMixSource;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AbstractFactMixSourceTest extends Mockito {

    private static final String GET_SINCE_TOKEN_HEADER = "GetSinceToken";
    private static final String TIME_PATTERN_FIELD_NAME = "timePattern";
    private static final String TIME_PATTERN = "yyyyMMddHHmmssnnn";
    private static final String ACCESS_TOKEN = "accessToken";
    private CloseableHttpClient httpClient;
    private HttpRequestBase httpRequest;
    private CloseableHttpResponse httpResponse;
    private AbstractFactMixSource abstractFactMixSource;

    @Before
    public void before() throws NoSuchFieldException {
        httpClient = mock(CloseableHttpClient.class);
        httpRequest = mock(HttpRequestBase.class);
        httpResponse = mock(CloseableHttpResponse.class);
        abstractFactMixSource = mock(AbstractFactMixSource.class, Mockito.CALLS_REAL_METHODS);

        FieldSetter.setField(abstractFactMixSource,
                AbstractFactMixSource.class.getDeclaredField(TIME_PATTERN_FIELD_NAME),
                TIME_PATTERN);
    }

    @Test
    public void testSuccessDataIngesting() throws IOException, NoSuchFieldException {
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new HttpVersion(1, 1), 200, "OK"));
        when(httpResponse.getEntity()).thenReturn(null);
        String nextSinceToken = "20190101010000000";
        when(httpResponse.getFirstHeader(GET_SINCE_TOKEN_HEADER)).thenReturn(new BasicHeader(GET_SINCE_TOKEN_HEADER, nextSinceToken));
        when(httpClient.execute(httpRequest)).thenReturn(httpResponse);

        Time duration = Time.valueOf(LocalTime.now());
        Instant sinceToken = Instant.now();
        FactDto expected = new FactDto<>(sinceToken, 1L, 1L, ACCESS_TOKEN, null, 0);
        expected.setEntityList(Collections.emptyList());
        expected.setLastSyncResultCode(200);
        expected.setLastSyncElementCount(0);
        expected.setLastSyncDuration(duration);
        expected.setTotalElementsCount(0);
        expected.setLastErrorMessage(null);
        expected.setNextSinceToken(ConverterUtil.stringToInstant(nextSinceToken, TIME_PATTERN));

        FactDto actual = new FactDto<>(sinceToken, 1L, 1L, ACCESS_TOKEN, null, 0);

        abstractFactMixSource.executeRequest(httpClient, httpRequest, actual);
        actual.setLastSyncDuration(duration);

        assertEquals(expected, actual);
    }

    @Test
    public void testErrorDataIngesting() throws IOException {
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new HttpVersion(1, 1), 500, "Internal Server Error"));
        when(httpResponse.getEntity()).thenReturn(null);
        when(httpClient.execute(httpRequest)).thenReturn(httpResponse);

        Time duration = Time.valueOf(LocalTime.now());
        Instant sinceToken = Instant.now();
        FactDto expected = new FactDto<>(sinceToken, 1L, 1L, ACCESS_TOKEN, null, 0);
        expected.setEntityList(Collections.emptyList());
        expected.setLastSyncResultCode(500);
        expected.setLastSyncElementCount(0);
        expected.setLastSyncDuration(duration);
        expected.setTotalElementsCount(0);
        expected.setLastErrorMessage("Internal Server Error");
        expected.setNextSinceToken(sinceToken);

        FactDto actual = new FactDto<>(sinceToken, 1L, 1L, ACCESS_TOKEN, null, 0);

        abstractFactMixSource.executeRequest(httpClient, httpRequest, actual);
        actual.setLastSyncDuration(duration);

        assertEquals(expected, actual);
    }

    @Test
    public void testTimeoutDataIngesting() throws IOException {
        when(httpClient.execute(httpRequest)).thenThrow(new SocketTimeoutException("Read timed out"));

        Time duration = Time.valueOf("00:00:00");
        Instant sinceToken = Instant.now();
        FactDto expected = new FactDto<>(sinceToken, 1L, 1L, ACCESS_TOKEN, null, 0);
        expected.setEntityList(Collections.emptyList());
        expected.setLastSyncResultCode(408);
        expected.setLastSyncElementCount(0);
        expected.setLastSyncDuration(duration);
        expected.setTotalElementsCount(0);
        expected.setLastErrorMessage("Read timed out");
        expected.setNextSinceToken(sinceToken);

        FactDto actual = new FactDto<>(sinceToken, 1L, 1L, ACCESS_TOKEN, null, 0);

        abstractFactMixSource.executeRequest(httpClient, httpRequest, actual);
        actual.setLastSyncDuration(duration);

        assertEquals(expected, actual);
    }

}
