//package com.epam.tcodata.external.pump.producer.repository;
//
//import com.epam.tcodata.eventhub.dal.producer.AbstractEventHubProducer;
//import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
//import com.microsoft.azure.eventhubs.EventData;
//import org.apache.avro.specific.SpecificRecord;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class TestEventHubProducer<TEntity extends SpecificRecord> extends AbstractEventHubProducer<TEntity> {
//
//    private ConcurrentHashMap<String, Queue<EventData>> storage;
//
//    public TestEventHubProducer(
//            Class<TEntity> entityClass,
//            ConnectionStringBuilder eventHubConnectionBuilder) {
//
//        super(entityClass, eventHubConnectionBuilder);
//
//        this.storage = new ConcurrentHashMap<>();
//    }
//
//    @Override
//    protected void send(EventData eventData, String partitionKey, String connectionString) throws Exception {
//
//        String topic = connectionString;
//        Queue<EventData> queue = this.storage.computeIfAbsent(topic, v -> new LinkedList<>());
//        queue.add(eventData);
//    }
//
//    @Override
//    protected void send(List<EventData> eventDataBatch, String partitionKey, String connectionString) throws Exception {
//
//        String topic = connectionString;
//        Queue<EventData> queue = this.storage.computeIfAbsent(topic, v -> new LinkedList<>());
//        queue.addAll(eventDataBatch);
//    }
//}
