package com.epam.tcodata.mock.external.pump.source.fact.impl.mix;

import com.epam.tcodata.external.pump.source.fact.impl.PositionMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

@SuppressWarnings("CPD-START")
public class MixMockPositionMixSource extends PositionMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return host + ":" + RestMockUtil.PORT;
    }
}
