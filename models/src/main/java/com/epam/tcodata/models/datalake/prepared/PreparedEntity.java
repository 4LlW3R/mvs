package com.epam.tcodata.models.datalake.prepared;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.AbstractDataLakeEntity;

@SuppressWarnings("Duplicates")
public class PreparedEntity extends AbstractDataLakeEntity {

    private static final long serialVersionUID = 6083731162820334202L;

    public static class Fields {
        public static final String DURABLE_ID = "durable_id";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DURABLE_ID)
    private String durableId;

    public PreparedEntity() {
        /***  Default implementation ***/
    }

    public String getDurableId() {
        return this.durableId;
    }

    public void setDurableId(String durableId) {
        this.durableId = durableId;
    }

}
