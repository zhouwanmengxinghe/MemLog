package assign251_2;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.log4j.spi.LoggingEvent;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StressTest {
    private static final Logger logger = Logger.getLogger(StressTest.class);
    private static final int LOG_COUNT = 10000;

    @ParameterizedTest
    @ValueSource(ints = {100, 1000, 10000})
    void stressTestMemAppenderWithDifferentMaxSizes(int maxSize) {
        testMemAppender(maxSize, ArrayList.class);
        testMemAppender(maxSize, LinkedList.class);
    }

    private void testMemAppender(int maxSize, Class<?> listType) {
        MemAppender memAppender = createMemAppender(listType);
        memAppender.setMaxSize(maxSize);
        logger.addAppender(memAppender);

        long startTime = System.nanoTime();
        for (int i = 0; i < maxSize + 1000; i++) {
            logger.info("Stress test message " + i);
        }
        long endTime = System.nanoTime();

        System.out.println("List type: " + listType.getSimpleName() + ", MaxSize: " + maxSize + ", Current logs size: " + memAppender.getCurrentLogs().size());
        System.out.println("Discarded logs count: " + memAppender.getDiscardedLogCount() + ", Time taken: " + (endTime - startTime) + " ns");

        assertTrue(memAppender.getDiscardedLogCount() > 0 || memAppender.getCurrentLogs().size() <= maxSize,
                "Discarded logs count should be greater than 0 or current logs size should not exceed maxSize after stress test");
        logger.removeAppender(memAppender);
    }

    private MemAppender createMemAppender(Class<?> listType) {
        MemAppender memAppender = MemAppender.getInstance();
        try {
            memAppender.initLogs((List<LoggingEvent>) listType.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize MemAppender", e);
        }
        return memAppender;
    }

    @Test
    void compareAppenderPerformance() {
        measurePerformance(createMemAppender(ArrayList.class), "ArrayList MemAppender");
        measurePerformance(createMemAppender(LinkedList.class), "LinkedList MemAppender");
        measurePerformance(new ConsoleAppender(new PatternLayout()), "ConsoleAppender");

        try {
            measurePerformance(new FileAppender(new PatternLayout(), "test.log", true), "FileAppender");
        } catch (IOException e) {
            System.err.println("Failed to create FileAppender: " + e.getMessage());
        }
    }

    private void measurePerformance(Appender appender, String appenderType) {
        logger.addAppender(appender);
        long startTime = System.nanoTime();
        for (int i = 0; i < LOG_COUNT; i++) {
            logger.info("Performance test message " + i);
        }
        long endTime = System.nanoTime();
        System.out.println("Appender type: " + appenderType + " took " + (endTime - startTime) + " ns");
        logger.removeAppender(appender);
    }
    @Test
    void compareLayoutPerformance() {
        MemAppender memAppender = MemAppender.getInstance();
        Layout velocityLayout = new PatternLayout("[$p] $m");
        Layout patternLayout = new PatternLayout("%p %m");

        memAppender.setLayout(velocityLayout);
        measureLayoutPerformance(memAppender, "VelocityLayout");

        memAppender.setLayout(patternLayout);
        measureLayoutPerformance(memAppender, "PatternLayout");
    }

    private void measureLayoutPerformance(MemAppender appender, String layoutType) {
        logger.addAppender(appender);
        long startTime = System.nanoTime();
        for (int i = 0; i < LOG_COUNT; i++) {
            logger.info("Layout performance test message " + i);
        }
        long endTime = System.nanoTime();
        System.out.println("Layout type: " + layoutType + " took " + (endTime - startTime) + " ns");
        logger.removeAppender(appender);
    }
}