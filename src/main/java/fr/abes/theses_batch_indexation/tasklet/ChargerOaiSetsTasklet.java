package fr.abes.theses_batch_indexation.tasklet;

import fr.abes.theses_batch_indexation.model.oaisets.OAIPMH;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class ChargerOaiSetsTasklet implements Tasklet, StepExecutionListener {

    List<Set> oaiSets;
    @Value("${oaiSets.path}")
    String oaiSetsFilePath;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();
        executionContext.put("oaiSets", oaiSets);
        return null;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("chargement du fichier de mapping OAI...");

        File f = new File(oaiSetsFilePath);
        final JAXBContext jc2 = JAXBContext.newInstance("fr.abes.theses_batch_indexation.model.oaisets");
        final Unmarshaller unmarshaller2 = jc2.createUnmarshaller();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
        OAIPMH oai = (OAIPMH) unmarshaller2.unmarshal(isr);
        this.oaiSets = oai.getListSets().getSet();
        return RepeatStatus.FINISHED;
    }
}
