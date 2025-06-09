package com.epam.tcodata.sql.dal.service.pumps;

import com.epam.tcodata.sql.dal.domain.pumps.OrganisationGroup;
import com.epam.tcodata.sql.dal.service.IReadWriteService;

import java.util.List;

public interface IOrganisationGroupService extends IReadWriteService<OrganisationGroup> {

    List<OrganisationGroup> getActiveOrganisationGroupsForActiveAccounts();
}
