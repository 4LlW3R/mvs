package com.epam.tcodata.external.pump.source;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.models.mix.Entity;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface IMixSource<T extends Entity> extends Serializable {

    void requestDataAndFillDto(List<AbstractDto<T>> dto, String host, Instant currentTime) throws IOException;

    /**
     * Method finds duplicates in list.
     *
     * @param entities list of entities.
     * @return set of duplicates
     */
    default Set<T> findDuplicates(List<T> entities) {
        final Set<T> duplicates = new HashSet<>();
        final Set<T> temp = new HashSet<>();

        for (T entity : entities) {
            if (!temp.add(entity)) {
                duplicates.add(entity);
            }
        }
        return duplicates;
    }
}
