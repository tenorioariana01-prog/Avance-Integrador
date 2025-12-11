package ViveSalud.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
