package nl.openweb.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.openweb.monitor.Config.Page;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tdclighthouse.commons.mail.util.MailClient;

public class Core implements ApplicationListener<ContextClosedEvent>, InitializingBean {

    @Autowired
    private Config config;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private ThreadPoolTaskExecutor exceutor;

    private ExecutorService scheduler = Executors.newSingleThreadExecutor();
    private List<Monitor> monitors = new ArrayList<Monitor>();
    private volatile boolean running = true;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        running = false;
        scheduler.shutdownNow();

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Page> pages = config.getPages();
        for (Page page : pages) {
            monitors.add(new Monitor(config, page, mailClient));
        }

        scheduler.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    while (running) {
                        for (int i = 0; i < monitors.size(); i++) {
                            exceutor.execute(monitors.get(i));
                            Thread.sleep(config.getFrequency() / monitors.size());
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        });

    }

}
