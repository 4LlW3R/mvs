package com.epam.tcodata.mock.external.pump.source.dimension.impl.mix;

import com.epam.tcodata.external.pump.source.dimension.impl.OrganisationSubGroupMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

public class MixMockOrganisationSubGroupMixSource extends OrganisationSubGroupMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return host + ":" + RestMockUtil.PORT;
    }
}
