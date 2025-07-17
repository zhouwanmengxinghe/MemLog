package assign251_2;

import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemAppenderTest {
    private MemAppender memAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        memAppender = MemAppender.getInstance();
        memAppender.clear();
        logger = Logger.getLogger(MemAppenderTest.class);
        logger.addAppender(memAppender);
    }

    @Test
    void testSingletonInstance() {
        MemAppender anotherInstance = MemAppender.getInstance();
        assertSame(memAppender, anotherInstance, "MemAppender should follow singleton pattern");
    }

    @Test
    void testLogStorageAndRetrieval() {
        logger.info("First log message");
        logger.info("Second log message");

        List<LoggingEvent> logs = memAppender.getCurrentLogs();
        assertEquals(2, logs.size(), "The number of stored logs should be 2");
        assertEquals("First log message", logs.get(0).getMessage().toString());
        assertEquals("Second log message", logs.get(1).getMessage().toString());
    }

    @Test
    void testMaxSizeLimitAndDiscardCount() {
        memAppender.setMaxSize(2); // Set a small max size for testing
        for (int i = 1; i <= 10; i++) {
            logger.info("Log message " + i);
        }

        List<LoggingEvent> logs = memAppender.getCurrentLogs();
        assertEquals(2, logs.size(), "The number of stored logs should be limited to maxSize (2)");
        assertTrue(memAppender.getDiscardedLogCount() >= 8, "At least 8 logs should have been discarded");
    }


    @Test
    void testPrintLogs() {
        VelocityLayout layout = new VelocityLayout("[$p] $m");
        memAppender.setLayout(layout);
        logger.info("First log message");
        logger.info("Second log message");

        memAppender.printLogs();

        assertTrue(memAppender.getCurrentLogs().isEmpty(), "Logs should be cleared after printLogs is called");
    }

    @Test
    void testDependencyInjectionOfLayout() {
        Layout layout = new VelocityLayout("[$p] $m");
        memAppender.setLayout(layout);


        assertSame(layout, memAppender.getLayout(), "Layout should be an instance of VelocityLayout");

        logger.info("Test message with layout");

        List<String> logStrings = memAppender.getEventStrings();
        assertEquals(1, logStrings.size());
        assertTrue(logStrings.get(0).contains("[INFO] Test message with layout"), "Log string should be formatted according to the layout");
    }

    @Test
    void testGetEventStringsWithoutLayout() {
        memAppender.setLayout(null); // Ensure layout is removed
        logger.info("Test message without layout");

        assertThrows(NullPointerException.class, () -> memAppender.getEventStrings(),
                "Calling getEventStrings without setting a layout should throw an exception");
    }

    @Test
    void testGetEventStringsWithLayout() {
        VelocityLayout layout = new VelocityLayout("[$p] $m");
        memAppender.setLayout(layout);

        logger.info("Test message with layout");

        List<String> logStrings = memAppender.getEventStrings();
        assertEquals(1, logStrings.size());
        assertEquals("[INFO] Test message with layout", logStrings.get(0));
    }
}