package com.epam.tcodata.external.pump.converter;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import org.apache.avro.specific.SpecificRecord;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public interface IConverter<T extends Entity, S extends IEnrichable, U extends SpecificRecord> extends Serializable {

    S convertToEnriched(T entity, AbstractDto<T> dto, IKeyManager keyManager);

    /**
     * Convert raw entities to enriched.
     * @param entities raw entities.
     * @param dto
     * @param keyManager
     * @return enriched entities.
     */
    default List<S> convertListToEnriched(List<T> entities, AbstractDto<T> dto, IKeyManager keyManager) {
        return entities.stream()
                .map(entity -> convertToEnriched(entity, dto, keyManager))
                .collect(Collectors.toList());
    }

    U convertToAvro(S enrichedEntity);

    Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD);
}
