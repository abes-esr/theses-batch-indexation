package fr.abes.theses_batch_indexation.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${index.name}")
    private String nomIndex;

    @Value("${index.name.theses}")
    private String thesesIndex;

    @Value("${index.name.personnes}")
    private String personnesIndex;

    @Value("${index.name.thematiques}")
    private String thematiquesIndex;

    @Value("${index.name.recherche_personnes}")
    private String recherche_personnesIndex;

    @Value("${table.name}")
    private String nomTable;

}
