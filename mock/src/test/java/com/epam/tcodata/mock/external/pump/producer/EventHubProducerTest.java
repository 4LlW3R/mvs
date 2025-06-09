//package com.epam.tcodata.external.pump.producer;
//
//import com.epam.tcodata.mock.external.pump.driver.TestFactoryAbilities;
//import com.epam.tcodata.external.pump.dto.Dto;
//import com.epam.tcodata.external.pump.factory.IFactory;
//import com.epam.tcodata.mock.external.pump.factory.impl.TestPositionFactory;
//import com.epam.tcodata.models.avro.fact.AvroPosition;
//import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
//import com.epam.tcodata.models.mix.fact.Position;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//
//
//public class EventHubProducerTest {
//
//    private static IFactory<Position, EnrichedPosition, AvroPosition> factory;
//    private static IEventHubProducer<AvroPosition> eventHubProducer;
//
//    @BeforeClass
//    public static void setup() throws Exception {
//        factory = new TestPositionFactory();
//        factory.setInitParameters(new HashMap<TestFactoryAbilities, String>() {{
//        }});
//        eventHubProducer = factory.createEventHubProducer();
//    }
//
//    @AfterClass
//    public static void cleanup() {
//    }
//
//    @Test
//    public void testProducer() throws Exception {
//        Dto<Position> dto = new Dto<>(100L, "");
//        Position position1 = new EnrichedPosition();
//        Position position2 = new EnrichedPosition();
//
//        dto.setEntityList(Arrays.asList(position1, position2));
//
//        List<Dto> dtos = Arrays.asList(dto);
//        eventHubProducer.handleBatches(dtos);
//    }
//}
