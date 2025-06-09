package com.epam.tcodata.mock.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.source.dimension.impl.OrganisationSubGroupMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

import java.net.InetAddress;

public class MockOrganisationSubGroupMixSource extends OrganisationSubGroupMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return InetAddress.getLoopbackAddress().getHostName() + ":" + RestMockUtil.PORT;
    }
}
