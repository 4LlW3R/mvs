package com.epam.tcodata.mock.external.pump.source.fact.impl;

import com.epam.tcodata.external.pump.source.fact.impl.PositionMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

import java.net.InetAddress;

public class MockPositionMixSource extends PositionMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return InetAddress.getLoopbackAddress().getHostName() + ":" + RestMockUtil.PORT;
    }
}
