package com.epam.tcodata.mock.external.pump.util.misc;

import org.apache.commons.io.FilenameUtils;
import org.mockserver.mock.Expectation;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.server.initialize.ExpectationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MockExpectationInitializer implements ExpectationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockExpectationInitializer.class);
    public static final String BASE_STR = "base";
    private static final String GET_SINCE_TOKEN_HEADER = "GetSinceToken";
    private static File baseDir = null;

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(BASE_STR);
        if (resource != null) {
            baseDir = new File(resource.getFile());
        }
    }

    /**
     * Set another base for initializing.
     *
     * @param base base directory.
     * @throws FileNotFoundException
     */
    public static void setBase(String base) throws FileNotFoundException {
        if (base != null && new File(base).exists()) {
            baseDir = new File(base);
        } else {
            throw new FileNotFoundException(base);
        }
    }

    @Override
    public Expectation[] initializeExpectations() {
        List<Expectation> expectations = new ArrayList<>();
        File base = baseDir;
        if (base == null) {
            return new Expectation[0];
        }
        scanTree(base, base, (p, f) -> {
            try (FileInputStream fileInputStream = new FileInputStream(f)) {
                expectations.add(
                        new Expectation(HttpRequest.request(p))
                                .thenRespond(HttpResponse.response()
                                        .withBody(RestMockUtil.resourceAsString(fileInputStream))
                                        .withHeader(GET_SINCE_TOKEN_HEADER, FilenameUtils.removeExtension(f.getName()))
                                ));
                LOGGER.info("Expectation withPath({}) added", p);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });

        return expectations.toArray(new Expectation[0]);
    }

    private static void scanTree(File base, File current, BiConsumer<String, File> apply) {
        if (base == null || current == null || apply == null) {
            return;
        }
        File[] files = current.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanTree(base, file, apply);
                } else {
                    File parent = file.getParentFile();
                    if (parent != null) {
                        String relative = base.toURI().relativize(parent.toURI()).getPath();
                        apply.accept("/" + relative.substring(0, relative.length() - 1), file);
                    }
                }
            }
        }
    }

}
