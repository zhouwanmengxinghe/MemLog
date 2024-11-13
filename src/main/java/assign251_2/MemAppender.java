package assign251_2;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton {
    private static MemAppender instance;
    private List<LoggingEvent> logs;
    private Layout layout; // 依赖项
    private int maxSize = 100; // 默认最大尺寸
    private long discardedLogCount = 0;

    private MemAppender() {
        logs = new ArrayList<>();
    }

    public static synchronized MemAppender getInstance() {
        if (instance == null) {
            instance = new MemAppender();
        }
        return instance;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getDiscardedLogCount() {
        return discardedLogCount;
    }

    public List<LoggingEvent> getCurrentLogs() {
        return Collections.unmodifiableList(logs);
    }

    @Override
    protected void append(LoggingEvent event) {
        if (logs.size() >= maxSize) {
            logs.remove(0);
            discardedLogCount++;
        }
        logs.add(event);
    }

    public List<String> getEventStrings() {
        if (this.layout == null) {
            throw new NullPointerException("Layout must be set before calling getEventStrings.");
        }
        List<String> formattedLogs = new ArrayList<>();
        for (LoggingEvent event : logs) {
            formattedLogs.add(this.layout.format(event));
        }
        return Collections.unmodifiableList(formattedLogs);
    }

    public void printLogs() {
        if (this.layout == null) {
            throw new IllegalStateException("Layout must be set before calling printLogs.");
        }

        for (LoggingEvent event : logs) {
            System.out.println(this.layout.format(event));
        }
        logs.clear();
    }

    @Override
    public void close() {
        logs.clear();
    }
    public void clear() {
        logs.clear();
        discardedLogCount = 0;
    }
    @Override
    public boolean requiresLayout() {
        return true;
    }
}