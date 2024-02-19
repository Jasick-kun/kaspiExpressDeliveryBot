package kz.zhasulan.kaspiexpressdeliverybot;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzSubmitJobs {
    private static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * ? * * *";

    @Bean(name = "orderCheck")
    public JobDetailFactoryBean jobMemberStats() {
        return QuartzConfig.createJobDetail(MemberStatsJob.class, "Order check job");
    }

    @Bean(name = "orderCheckTrigger")
    public SimpleTriggerFactoryBean triggerMemberStats(@Qualifier("orderCheck") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, 180000, "Order check Trigger");
    }
}

