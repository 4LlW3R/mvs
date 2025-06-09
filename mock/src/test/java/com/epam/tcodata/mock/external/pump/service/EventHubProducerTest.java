//package com.epam.tcodata.rest.pump.service;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//public class EventHubProducerTest {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubProducerTest.class);
//
//    private static Properties mixIntegrateProperties;
//
//    @BeforeClass
//    public static void setUp() {
//        mixIntegrateProperties = loadProperties("mix-integration.properties");
//    }
//
//    @Test
//    public void convertToAvroEventTest() {
//    }
//
//    @Test
//    public void convertToAvroTripTest(){
//    }
//
//    private static Properties loadProperties(String propertiesFileName) {
//        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFileName)) {
//            Properties properties = new Properties();
//            properties.load(inputStream);
//            return properties;
//        } catch (IOException e) {
//            String msg = "Error load " + propertiesFileName + ". Please check it.";
//            LOGGER.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//    }
//}
