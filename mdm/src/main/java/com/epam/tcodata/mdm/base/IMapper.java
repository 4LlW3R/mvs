package com.epam.tcodata.mdm.base;

import java.util.Map;

@FunctionalInterface
public interface IMapper {
    Map<String, Object> map(Object object);
}
