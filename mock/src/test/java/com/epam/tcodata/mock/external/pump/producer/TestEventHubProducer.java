//package com.epam.tcodata.external.pump.producer;
//
//import com.epam.tcodata.eventhub.dal.configuration.EventHubConfigManager;
//import com.epam.tcodata.external.pump.converter.IConverter;
//import com.epam.tcodata.models.EntityType;
//import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
//import org.apache.avro.specific.SpecificRecord;
//
//public class TestEventHubProducer<T extends SpecificRecord> extends EventHubProducer<T> {
//
//    private com.epam.tcodata.external.pump.producer.repository.TestEventHubProducer sinkRepository;
//
//
//    public TestEventHubProducer(EntityType entityType, IConverter converter) {
//        super(entityType, converter);
//
//        ConnectionStringBuilder connectionStringBuilder = EventHubConfigManager
//                .createTestConnectionStringBuilder("", getEntityType().getEventHubType());
//
//        this.sinkRepository = new com.epam.tcodata.external.pump.producer.repository.TestEventHubProducer
//                (getEntityType().getAvroClass(), connectionStringBuilder);
//    }
//
//    @Override
//    public com.epam.tcodata.eventhub.dal.producer.IEventHubProducer<T> createSinkRepository() {
//        return this.sinkRepository;
//    }
//}
