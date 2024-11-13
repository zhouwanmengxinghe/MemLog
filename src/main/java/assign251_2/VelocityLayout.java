package assign251_2;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

public class VelocityLayout extends Layout {
    private String pattern;

    public VelocityLayout(String pattern) {
        this.pattern = pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String format(LoggingEvent event) {
        VelocityContext context = new VelocityContext();
        context.put("c", event.getLoggerName());
        context.put("d", event.getTimeStamp());
        context.put("m", event.getMessage());
        context.put("p", event.getLevel().toString());
        context.put("t", event.getThreadName());
        context.put("n", System.lineSeparator());

        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "VelocityLayout", pattern);
        return writer.toString();
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {
        // Initialization if needed
    }
}