package com.epam.tcodata.external.pump.source;

import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.source.dimension.AbstractDimensionMixSource;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AbstractDimensionMixSourceTest extends Mockito {

    private CloseableHttpClient httpClient;
    private HttpRequestBase httpRequest;
    private CloseableHttpResponse httpResponse;
    private AbstractDimensionMixSource abstractDimensionMixSource;
    private static final String ACCESS_TOKEN = "accessToken";

    @Before
    public void before() {
        httpClient = mock(CloseableHttpClient.class);
        httpRequest = mock(HttpRequestBase.class);
        httpResponse = mock(CloseableHttpResponse.class);
        abstractDimensionMixSource = mock(AbstractDimensionMixSource.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testSuccessDataIngesting() throws IOException {
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new HttpVersion(1, 1), 200, "OK"));
        when(httpResponse.getEntity()).thenReturn(null);
        when(httpClient.execute(httpRequest)).thenReturn(httpResponse);

        Time duration = Time.valueOf(LocalTime.now());
        DimensionDto expected = new DimensionDto<>(1L, ACCESS_TOKEN);
        expected.setEntityList(Collections.emptyList());
        expected.setLastSyncResultCode(200);
        expected.setLastSyncElementCount(0);
        expected.setLastSyncDuration(duration);
        expected.setTotalElementsCount(0);
        expected.setLastErrorMessage(null);

        DimensionDto actual = new DimensionDto<>(1L, ACCESS_TOKEN);

        abstractDimensionMixSource.executeRequest(httpClient, httpRequest, actual);
        actual.setLastSyncDuration(duration);

        assertEquals(expected, actual);
    }

    @Test
    public void testErrorDataIngesting() throws IOException {
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new HttpVersion(1, 1), 500, "Internal Server Error"));
        when(httpResponse.getEntity()).thenReturn(null);
        when(httpClient.execute(httpRequest)).thenReturn(httpResponse);

        Time duration = Time.valueOf(LocalTime.now());
        DimensionDto expected = new DimensionDto<>(1L, ACCESS_TOKEN);
        expected.setEntityList(Collections.emptyList());
        expected.setLastSyncResultCode(500);
        expected.setLastSyncElementCount(0);
        expected.setLastSyncDuration(duration);
        expected.setTotalElementsCount(0);
        expected.setLastErrorMessage("Internal Server Error");

        DimensionDto actual = new DimensionDto<>(1L, ACCESS_TOKEN);

        abstractDimensionMixSource.executeRequest(httpClient, httpRequest, actual);
        actual.setLastSyncDuration(duration);

        assertEquals(expected, actual);
    }

    @Test
    public void testTimeoutDataIngesting() throws IOException {
        when(httpClient.execute(httpRequest)).thenThrow(new SocketTimeoutException("Read timed out"));

        Time duration = Time.valueOf("00:00:00");
        DimensionDto expected = new DimensionDto<>(1L, ACCESS_TOKEN);
        expected.setEntityList(Collections.emptyList());
        expected.setLastSyncResultCode(408);
        expected.setLastSyncElementCount(0);
        expected.setLastSyncDuration(duration);
        expected.setTotalElementsCount(0);
        expected.setLastErrorMessage("Read timed out");

        DimensionDto actual = new DimensionDto<>(1L, ACCESS_TOKEN);

        abstractDimensionMixSource.executeRequest(httpClient, httpRequest, actual);
        actual.setLastSyncDuration(duration);

        assertEquals(expected, actual);
    }

}
