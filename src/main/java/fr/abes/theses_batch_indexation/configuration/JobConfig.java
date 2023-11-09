package fr.abes.theses_batch_indexation.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("job")
@EnableBatchProcessing
@Getter
@Setter
public class JobConfig {

    private int chunk;

    private int throttle;

    private int whereLimite;


}
