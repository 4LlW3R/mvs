package com.epam.tcodata.mock.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.source.dimension.impl.AssetMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

import java.net.InetAddress;

public class MockAssetMixSource extends AssetMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return InetAddress.getLoopbackAddress().getHostName() + ":" + RestMockUtil.PORT;
    }
}
