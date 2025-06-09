package com.epam.tcodata.sql.dal.service;

import com.epam.tcodata.sql.dal.IDaoFactory;

public interface IService extends AutoCloseable {

    IDaoFactory factory();

    boolean checkConnection();

    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();
}
