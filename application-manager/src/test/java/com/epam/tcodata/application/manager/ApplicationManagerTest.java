package com.epam.tcodata.application.manager;

import com.epam.tcodata.models.SignalType;
import com.epam.tcodata.models.ApplicationType;
import junit.framework.TestCase;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ApplicationManagerTest {

    private static final String APPLICATIONS_STR_CONST = "--applications" ;
    private static final String OVERTAKING_VIOLATION_DETECTION_STR_CONST = "OVERTAKING_VIOLATION_DETECTION";
    private static final String SIGNAL_STR_CONST = "--signal";
    private static final String DASH_DASH_MESSAGE_STR_CONST = "--message";
    private static final String MESSAGE_STR_CONST = "message";

    @Test
    public void shouldReturnApplicationTypes() {
        Set<ApplicationType> expected = new HashSet<>();
        expected.add(ApplicationType.EXTERNAL_PUMP);
        expected.add(ApplicationType.INTERNAL_PUMP);
        expected.add(ApplicationType.OVERTAKING_DETECTION);
        expected.add(ApplicationType.OVERTAKING_VIOLATION_DETECTION);
        expected.add(ApplicationType.ROAD_CONDITION_VIOLATION_STREAM_DATALAKE);
        expected.add(ApplicationType.OVERTAKING_VIOLATION_STREAM_DATALAKE);
        String applications = "EXTERNAL_PUMP,INTERNAL_PUMP,OVERTAKING_DETECTION,OVERTAKING_VIOLATION_DETECTION,"
                + "ROAD_CONDITION_VIOLATION_STREAM_DATALAKE,OVERTAKING_VIOLATION_STREAM_DATALAKE";
        Set<ApplicationType> actual = ApplicationManager.getApplicationTypes(applications);
        TestCase.assertTrue(expected.containsAll(actual));
    }

    @Test
    public void shouldReturnSignalType() {
        SignalType expected = SignalType.STOP;
        String signal = "STOP";
        SignalType actual = ApplicationManager.getSignalType(signal);
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void shouldParseInputArgs() {
        String[] args = new String[8];
        args[0] = APPLICATIONS_STR_CONST;
        args[1] = OVERTAKING_VIOLATION_DETECTION_STR_CONST;
        args[2] = "--entities";
        args[3] = "POSITION,LOCATION";
        args[4] = SIGNAL_STR_CONST;
        args[5] = "STOP";
        args[6] = DASH_DASH_MESSAGE_STR_CONST;
        args[7] = MESSAGE_STR_CONST;
        CommandLine commandLine = ApplicationManager.parseInputArgs(args);
        TestCase.assertEquals(commandLine.getOptions().length, 4);
    }

    @Test
    public void shouldParseInputArgsWithoutPumpsAndWithoutEntitiesAndWithoutMessage() {
        String[] args = new String[4];
        args[0] = APPLICATIONS_STR_CONST;
        args[1] = OVERTAKING_VIOLATION_DETECTION_STR_CONST;
        args[2] = SIGNAL_STR_CONST;
        args[3] = "STOP";
        CommandLine commandLine = ApplicationManager.parseInputArgs(args);
        TestCase.assertEquals(commandLine.getOptions().length, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotParseInputArgsWithoutRequiredArgs() {
        String[] args = new String[2];
        args[0] = APPLICATIONS_STR_CONST;
        args[1] = OVERTAKING_VIOLATION_DETECTION_STR_CONST;
        CommandLine commandLine = ApplicationManager.parseInputArgs(args);
        TestCase.assertEquals(commandLine.getOptions().length, 1);
    }

    @Test
    public void shouldValidateArgs() {
        String[] args = new String[8];
        args[0] = APPLICATIONS_STR_CONST;
        args[1] = "EXTERNAL_PUMP,OVERTAKING_VIOLATION_DETECTION";
        args[2] = "--entities";
        args[3] = "POSITION,EVENT";
        args[4] = SIGNAL_STR_CONST;
        args[5] = "STOP";
        args[6] = DASH_DASH_MESSAGE_STR_CONST;
        args[7] = MESSAGE_STR_CONST;

        CommandLine commandLine = ApplicationManager.parseInputArgs(args);
        ApplicationManager.validateArgs(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotValidateArgsWithPumpsAndWithoutEntities() {
        String[] args = new String[6];
        args[0] = APPLICATIONS_STR_CONST;
        args[1] = "EXTERNAL_PUMP,OVERTAKING_VIOLATION_DETECTION";
        args[2] = SIGNAL_STR_CONST;
        args[3] = "STOP";
        args[4] = DASH_DASH_MESSAGE_STR_CONST;
        args[5] = MESSAGE_STR_CONST;

        CommandLine commandLine = ApplicationManager.parseInputArgs(args);
        ApplicationManager.validateArgs(commandLine);
    }

    @Test
    public void shouldValidateArgsWithoutPumpsAndWithoutEntities() {
        String[] args = new String[4];
        args[0] = APPLICATIONS_STR_CONST;
        args[1] = OVERTAKING_VIOLATION_DETECTION_STR_CONST;
        args[2] = SIGNAL_STR_CONST;
        args[3] = "STOP";

        CommandLine commandLine = ApplicationManager.parseInputArgs(args);
        ApplicationManager.validateArgs(commandLine);
    }
}
