package assign251_2;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VelocityLayoutTest {
    private VelocityLayout layout;
    private Logger logger;

    @BeforeEach
    void setUp() {
        layout = new VelocityLayout("[$p] $c $d: $m");
        logger = Logger.getLogger(VelocityLayoutTest.class);
    }

    @Test
    void testFormatLogMessage() {
        LoggingEvent event = new LoggingEvent("test.FQCN", logger, Level.INFO, "Test log message", null);
        String formattedMessage = layout.format(event);
        assertTrue(formattedMessage.contains("[INFO]"), "Formatted message should contain priority");
        assertTrue(formattedMessage.contains("Test log message"), "Formatted message should contain log message");
    }

    @Test
    void testSetPattern() {
        layout.setPattern("$m - [$p]");
        LoggingEvent event = new LoggingEvent("test.FQCN", logger, Level.WARN, "Warning message", null);

        String formattedMessage = layout.format(event);
        assertEquals("Warning message - [WARN]", formattedMessage, "Pattern should be applied correctly");
    }

    @Test
    void testNullMessage() {
        LoggingEvent event = new LoggingEvent("test.FQCN", logger, Level.ERROR, null, null);
        String formattedMessage = layout.format(event);
        assertNotNull(formattedMessage, "Formatted message should not be null even if log message is null");
    }
}