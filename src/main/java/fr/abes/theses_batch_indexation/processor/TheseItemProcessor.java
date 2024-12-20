package fr.abes.theses_batch_indexation.processor;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.DmdSec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;

import com.google.gson.Gson;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.these.TheseMappee;
import fr.abes.theses_batch_indexation.model.tef.Mets;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TheseItemProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener {

    private final XMLJsonMarshalling marshall;
    List<Set> oaiSets;

    public TheseItemProcessor(XMLJsonMarshalling marshall) {
        this.marshall = marshall;
    }

    @Override
    public TheseModel process(TheseModel item) throws Exception {

        log.debug("debut de traitement de " + item.getNnt());
        Mets mets = marshall.chargerMets(new ByteArrayInputStream(item.getDoc().getBytes()));
        String json = new Gson().toJson(new TheseMappee(mets, oaiSets, item.getIdDoc()));
        item.setJsonThese(json);
        Optional<DmdSec> starGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst();
        if (starGestion.isPresent()) {
            item.setCodeEtab(starGestion.get().getMdWrap().getXmlData().getStarGestion().getCodeEtab());
        }

        return item;
    }

    @Override
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
