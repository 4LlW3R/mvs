package com.epam.tcodata.secure.storage.dal;

/**
 * This interface is for switching between sections inside subsystems in Secret.
 * The interface must be implemented by all internal (inside Secret) enums that matched to sections.
 */
interface ISecretDivision {

    /**
     * For the given name of section (in case of Sql it is database name f.e.) should look the
     * corresponded identifier with the same name as current.
     *
     * @param sectionName desired section name
     * @return identifier
     */
    ISecretIdentity usingSection(String sectionName);

    /**
     * Selects identity (static or dynamic) matched to the given full name.
     *
     * @param fullName a full name representation
     * @return identity
     */
    ISecretIdentity selectByName(String fullName);
}
