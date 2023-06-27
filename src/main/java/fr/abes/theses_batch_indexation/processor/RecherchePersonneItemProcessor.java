package fr.abes.theses_batch_indexation.processor;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneMapee;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneMappe;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.Mets;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.List;

@Slf4j
@Component
public class RecherchePersonneItemProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener {

    private final XMLJsonMarshalling marshall;

    List<Set> oaiSets;

    public RecherchePersonneItemProcessor(XMLJsonMarshalling marshall) {
        this.marshall = marshall;
    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {
        log.info("debut de traitement de " + theseModel.getId());
        Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModel.getDoc().getBytes()));
        RecherchePersonneMappe recherchePersonneMappe = new RecherchePersonneMappe(mets,theseModel.getId(), oaiSets);
        theseModel.setPersonnes(recherchePersonneMappe.getPersonnes());
        return theseModel;
    }

    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution
                .getJobExecution()
                .getExecutionContext();

        this.oaiSets = (List<Set>) executionContext.get("oaiSets");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
