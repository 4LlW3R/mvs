package com.epam.tcodata.mock.external.pump.util.misc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RestMockUtil {

    public static final int PORT = 1080;

    private RestMockUtil() {
    }

    /**
     * Read the stream and returns whole its contain as a string.
     *
     * @param is input stream.
     * @return string.
     * @throws IOException
     */
    public static String resourceAsString(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()))) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }
    }

    /**
     * Read the stream and returns whole its contain as a list.
     *
     * @param entityClass entity class.
     * @param is input stream.
     * @param <T> generic entity type.
     * @return list.
     * @throws IOException
     */
    public static <T> List<T> loadData(Class<T> entityClass, InputStream is) throws IOException {

        String line = resourceAsString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(
                        ArrayList.class,
                        entityClass);


        return mapper.readValue(line, type);
    }


    /**
     * Read the file and returns whole its contain as a string.
     *
     * @param entityClass entity class.
     * @param fileName file name.
     * @param <T> generic entity type.
     * @return list.
     * @throws IOException
     */
    public static <T> List<T> loadData(Class<T> entityClass, String fileName) throws IOException {

        return loadData(entityClass, Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
    }

    /**
     * Writes all elements into the file as separate lines.
     *
     * @param entityClass entity class.
     * @param data list of elements.
     * @param fileName file name.
     * @param <T> generic entity type.
     * @throws IOException
     */
    public static <T> void saveData(Class<T> entityClass, List<T> data, String fileName) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(
                        ArrayList.class,
                        entityClass);
        mapper.constructType(type);
        mapper.writeValue(new File(fileName), data);
    }
}
