package com.epam.tcodata.models.datalake;

import java.io.Serializable;

public interface IDataLakeEntity extends Serializable {

    Object[] orderedValues();
}
