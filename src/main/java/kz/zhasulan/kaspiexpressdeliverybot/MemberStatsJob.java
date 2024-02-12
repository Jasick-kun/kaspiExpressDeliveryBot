package kz.zhasulan.kaspiexpressdeliverybot;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Component
@DisallowConcurrentExecution
public class MemberStatsJob implements Job {

   @Autowired
   private OrderCheckService orderCheckService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Job ** {} ** starting @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        try {
            orderCheckService.check();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        log.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}