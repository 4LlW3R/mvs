package com.epam.tcodata.sql.dal.impl.mdm;

import com.epam.tcodata.sql.dal.IQuerySupplier;

public enum MdmQueries implements IQuerySupplier {

    NONE(""),

    KEY_MAPPING_SELECT(
            "SELECT * "
            + "FROM <table> "
            + "WHERE entity = :entity AND key_name = :key_name AND natural_key = :natural_key "
    ),

    KEYS_MAPPING_SELECT(
            "SELECT * "
                    + "FROM <table> "
                    + "WHERE entity = :entity AND key_name = :key_name AND natural_key in (<natural_key>)"
    );

    MdmQueries(String query) {
        this.query = query;
    }

    private String query;

    @Override
    public String query() {
        return this.query;
    }
}
