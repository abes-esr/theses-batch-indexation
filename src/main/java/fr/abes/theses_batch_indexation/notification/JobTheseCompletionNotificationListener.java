package fr.abes.theses_batch_indexation.notification;

import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JobTheseCompletionNotificationListener extends JobExecutionListenerSupport {

    private long start;

    private final ElasticConfig elasticConfig;

    @Autowired
    private Environment env;

    @Autowired
    public JobTheseCompletionNotificationListener(ElasticConfig elasticConfig) {
        this.elasticConfig = elasticConfig;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.debug("Load elastic client");
        try {
            ElasticClient.chargeClient(elasticConfig.getHostname(), elasticConfig.getPort(), elasticConfig.getScheme(), elasticConfig.getUserName(), elasticConfig.getPassword(), elasticConfig.getProtocol());
        } catch (Exception e) {
            log.error("pb lors du chargement du client ES : " + e.toString());
            throw new RuntimeException(e);
        }
        start = System.currentTimeMillis();
        Date date = new Date(start);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        log.info("Debut du job "+ env.getProperty("spring.batch.job.names") +" des theses à " + sdf.format(date));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long duration = System.currentTimeMillis() - start;

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("job indexation des theses termine");
        }

        log.info("duree du job = " + millisecondsToReadeable(duration));
    }

    private String millisecondsToReadeable(long millis) {

        long hour = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hour);
        long min = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(min);
        long sec = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.SECONDS.toMillis(sec);

        if (hour > 0) {
            return String.format("%02d h, %02d min, %02d sec, %03d millis", hour, min, sec, millis);
        } else {
            if (min > 0) {
                return String.format("%02d min, %02d sec, %03d millis", min, sec, millis);
            } else {
                return String.format("%02d sec, %03d millis", sec, millis);
            }
        }
    }
}
