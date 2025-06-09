package com.epam.tcodata.mock.external.pump.source.dimension.impl.mix;

import com.epam.tcodata.external.pump.source.dimension.impl.LibraryEventMixSource;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;

public class MixMockLibraryEventMixSource extends LibraryEventMixSource {

    @Override
    protected String endPointScheme() {
        return "http";
    }

    @Override
    protected String endPointHost(String host) {
        return host + ":" + RestMockUtil.PORT;
    }
}
