package com.epam.tcodata.sql.dal.impl.pumps;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.AbstractDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.*;
import com.epam.tcodata.sql.dal.service.impl.pumps.*;

public class PumpsDaoFactory extends AbstractDaoFactory {

    /**
     * Main pubic constructor.
     */
    public PumpsDaoFactory(ISecretStorage secretStorage) {
        super(secretStorage);

        register(MixOffset.class, new MixOffsetService(this));
        register(EventHubOffset.class, new EventHubOffsetService(this));
        register(HiveOffset.class, new HiveOffsetService(this));
        register(Account.class, new AccountService(this));
        register(OrganisationGroup.class, new OrganisationGroupService(this));
        register(AccountTokens.class, new AccountTokensService(this));
        register(Signal.class, new SignalService(this));
        register(ValidatedEventTachoOffset.class, new ValidatedEventTachoOffsetService(this));
    }
}
