package assign251_2;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

public class MemAppenderMBean extends StandardMBean {
    private final MemAppender memAppender;

    public MemAppenderMBean(MemAppender memAppender) {
        super(MemAppenderMBean.class, true);
        this.memAppender = memAppender;
    }





    public static void registerMBean(MemAppender memAppender) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("assign251_2:type=MemAppender");
        mbs.registerMBean(new MemAppenderMBean(memAppender), name);
    }
}
