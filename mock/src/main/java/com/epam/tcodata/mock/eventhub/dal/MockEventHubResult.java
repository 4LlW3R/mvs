package com.epam.tcodata.mock.eventhub.dal;

import com.microsoft.azure.eventhubs.EventData;

import java.util.List;

public class MockEventHubResult {
    private String partitionKey;
    long from;
    long until;
    private List<EventData> list;

    /**
     * Public constructor.
     *
     * @param partitionKey partition key
     * @param from sequence number from
     * @param until sequence number until
     * @param list list of event data
     */
    public MockEventHubResult(String partitionKey, long from, long until, List<EventData> list) {
        this.partitionKey = partitionKey;
        this.from = from;
        this.until = until;
        this.list = list;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public long getFrom() {
        return from;
    }

    public long getUntil() {
        return until;
    }

    public List<EventData> getList() {
        return list;
    }
}
