package com.epam.tcodata.sql.dal.service;

import com.epam.tcodata.sql.dal.IQuerySupplier;

public class CRUD {

    private IQuerySupplier read;
    private IQuerySupplier readAll;
    private IQuerySupplier readAllByParent;
    private IQuerySupplier insert;
    private IQuerySupplier update;
    private IQuerySupplier delete;
    private IQuerySupplier deleteAll;
    private IQuerySupplier filtered;

    public CRUD() {
        /***  Default implementation ***/
    }

    public CRUD setRead(IQuerySupplier read) {
        this.read = read;
        return this;
    }

    public CRUD setReadAll(IQuerySupplier readAll) {
        this.readAll = readAll;
        return this;
    }

    public CRUD setReadAllByParent(IQuerySupplier readAllByParent) {
        this.readAllByParent = readAllByParent;
        return this;
    }

    public CRUD setInsert(IQuerySupplier insert) {
        this.insert = insert;
        return this;
    }

    public CRUD setUpdate(IQuerySupplier update) {
        this.update = update;
        return this;
    }

    public CRUD setDelete(IQuerySupplier delete) {
        this.delete = delete;
        return this;
    }

    public CRUD setDeleteAll(IQuerySupplier deleteAll) {
        this.deleteAll = deleteAll;
        return this;
    }

    public CRUD setFiltered(IQuerySupplier filtered) {
        this.filtered = filtered;
        return this;
    }

    public IQuerySupplier getReadQuerySupplier() {
        return this.read;
    }

    public IQuerySupplier getReadAllQuerySupplier() {
        return this.readAll;
    }

    public IQuerySupplier getReadAllByParentQuerySupplier() {
        return this.readAllByParent;
    }

    public IQuerySupplier getInsertQuerySupplier() {
        return this.insert;
    }

    public IQuerySupplier getUpdateQuerySupplier() {
        return this.update;
    }

    public IQuerySupplier getDeleteQuerySupplier() {
        return this.delete;
    }

    public IQuerySupplier getDeleteAllQuerySupplier() {
        return this.deleteAll;
    }

    public IQuerySupplier getFilteredQuerySupplier() {
        return this.filtered;
    }
}
