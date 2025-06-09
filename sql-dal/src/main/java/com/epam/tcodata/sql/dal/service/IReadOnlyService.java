package com.epam.tcodata.sql.dal.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IReadOnlyService<T> extends IService {

    List<T> readFiltered(Map<String, Object> fields);

    Optional<T> read(Long id);

    default  List<T> readAll() { return readAll(null); }
    List<T> readAll(Long parentId);
}
