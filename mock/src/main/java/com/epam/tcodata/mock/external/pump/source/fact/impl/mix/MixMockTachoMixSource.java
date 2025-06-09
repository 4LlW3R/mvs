package com.epam.tcodata.mock.external.pump.source.fact.impl.mix;

import com.epam.tcodata.external.pump.source.fact.impl.TachoMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

public class MixMockTachoMixSource extends TachoMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return host + ":" + RestMockUtil.PORT;
    }
}
