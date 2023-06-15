package fr.abes.theses_batch_indexation.processor;

import java.io.ByteArrayInputStream;
import java.util.List;

import fr.abes.theses_batch_indexation.dto.personne.PersonneMapee;
import fr.abes.theses_batch_indexation.dto.these.TheseThematiquesMappee;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
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
public class ThematiqueItemProcessor implements ItemProcessor<TheseModel, TheseModel> {

    private final XMLJsonMarshalling marshall;

    public ThematiqueItemProcessor(XMLJsonMarshalling marshall) {
        this.marshall = marshall;
    }

    @Override
    public TheseModel process(TheseModel item) throws Exception {

        log.info("debut de traitement de " + item.getNnt());
        Mets mets = marshall.chargerMets(new ByteArrayInputStream(item.getDoc().getBytes()));
        String json = new Gson().toJson(new TheseThematiquesMappee(mets));
        item.setJsonThematiques(json);
        return item;
    }


}