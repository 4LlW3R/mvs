package com.epam.tcodata.mock.token.manager.repository;

import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.token.manager.repository.MixIdentityRepository;

import java.net.InetAddress;

public class MockMixIdentityRepository extends MixIdentityRepository {

    public MockMixIdentityRepository(ISecretStorage secretStorage) {
        super(secretStorage);
    }

    @Override
    public String getMixIdentityTokenEndpoint() {
        return "http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + RestMockUtil.PORT + "/core/connect/token";
    }

    @Override
    public String getMixScopes() {
        return "offline_access MiX.Integrate";
    }
}
