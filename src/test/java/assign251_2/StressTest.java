package assign251_2;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StressTest {
    private static final Logger logger = Logger.getLogger(StressTest.class);

    @Test
    void stressTestMemAppenderWithHighLoad() {
        MemAppender memAppender = MemAppender.getInstance();
        memAppender.setMaxSize(1000); // 设置较大的容量
        logger.addAppender(memAppender);

        for (int i = 0; i < 10000; i++) {
            logger.info("Stress test message " + i);
        }

        System.out.println("Current logs size: " + memAppender.getCurrentLogs().size());
        System.out.println("Discarded logs count: " + memAppender.getDiscardedLogCount());

        assertTrue(memAppender.getDiscardedLogCount() > 0, "Discarded logs count should be greater than 0 after stress test");
    }
}