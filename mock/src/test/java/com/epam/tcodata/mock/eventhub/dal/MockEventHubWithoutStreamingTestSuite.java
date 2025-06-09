package com.epam.tcodata.mock.eventhub.dal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        MockEventHubSendTest.class,
        MockEventHubUtilTest.class
})

public class MockEventHubWithoutStreamingTestSuite {
}
