package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.domain.pumps.OrganisationGroup;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import com.epam.tcodata.sql.dal.service.pumps.IOrganisationGroupService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganisationGroupService extends AbstractReadWriteService<OrganisationGroup> implements IOrganisationGroupService {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public OrganisationGroupService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "OrganisationGroup",
                new CRUD(),
                OrganisationGroup.class);
    }

    @Override
    public List<OrganisationGroup> getActiveOrganisationGroupsForActiveAccounts() {
        IAccountService accountService = IDaoFactory.service(factory(), Account.class);

        Map<String, Object> activeAccountFilter = new HashMap<>();
        activeAccountFilter.put(Account.Fields.IS_ACTIVE, true);
        List<Account> activeAccounts = accountService.readFiltered(activeAccountFilter);
        List<OrganisationGroup> activeOrgGroupsInActiveAccounts = new ArrayList<>();
        for (Account account : activeAccounts) {
            long activeAccountId = account.getId();

            Map<String, Object> activeOrgGroupByAccountFilter = new HashMap<>();
            activeOrgGroupByAccountFilter.put(OrganisationGroup.Fields.IS_ACTIVE, true);
            activeOrgGroupByAccountFilter.put(OrganisationGroup.Fields.ACCOUNT_ID, activeAccountId);
            activeOrgGroupsInActiveAccounts.addAll(this.readFiltered(activeOrgGroupByAccountFilter));
        }
        return activeOrgGroupsInActiveAccounts;
    }
}
