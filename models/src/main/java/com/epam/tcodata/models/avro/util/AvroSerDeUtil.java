package com.epam.tcodata.models.avro.util;

import com.epam.tcodata.models.exception.InvalidAvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * Utility class to store Avro serialization/deserialization functions.
 */
public final class AvroSerDeUtil implements Serializable {

    private static final long serialVersionUID = 8905024134762476770L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AvroSerDeUtil.class);

    private AvroSerDeUtil() {
    }

    /**
     * Deserialize a single object based on given Avro schema.
     *
     * @param avroClass avro class.
     * @param bytes     array to deserialize from.
     * @param <T>       the type of object being deserialized.
     * @return deserialized object instance.
     * @throws IOException condition is unknown: not reflected in Avro docs.
     */
    public static <T extends SpecificRecordBase> T deserialize(Class<T> avroClass, byte[] bytes)
            throws IOException {
        Objects.requireNonNull(avroClass, "'class type' should be not null!");
        Objects.requireNonNull(bytes, "'bytes' should be not null!");

        T probe;
        try {
            probe = avroClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new InvalidAvroTypeException("Invalid avro type was given!", e);
        }
        Schema schema = probe.getSchema();

        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        DatumReader<T> reader = new SpecificDatumReader<>(schema);

        return reader.read(null, decoder);
    }

    /**
     * Serialize a single given object to byte array using binary avro encoder.
     *
     * @param objectInstance object to serialize
     * @param <T>            the type of object to serialize
     * @return array of bytes as object instance avro serialized representation
     * @throws IOException If an I/O error occurs
     */
    public static <T extends SpecificRecord> byte[] serialize(T objectInstance) {
        Objects.requireNonNull(objectInstance, "'objectInstance' to be serialized should be not null!");

        Schema schema = objectInstance.getSchema();
        DatumWriter<T> writer = new SpecificDatumWriter<>(schema);

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);

            writer.write(objectInstance, encoder);
            encoder.flush();

            return stream.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
