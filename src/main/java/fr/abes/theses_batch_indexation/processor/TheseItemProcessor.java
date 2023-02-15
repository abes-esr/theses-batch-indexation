package fr.abes.theses_batch_indexation.processor;

import java.io.ByteArrayInputStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import fr.abes.theses_batch_indexation.dto.these.TheseDTO;
import fr.abes.theses_batch_indexation.dto.these.TheseMappee;
import fr.abes.theses_batch_indexation.model.jaxb.Mets;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TheseItemProcessor implements ItemProcessor<TheseDTO, TheseDTO> {

    @Autowired
    private XMLJsonMarshalling marshall;

    @Override
    public TheseDTO process(TheseDTO item) throws Exception {

        log.info("debut de traitement de " + item.getNnt());
        Mets mets = marshall.chargerMets(new ByteArrayInputStream(item.getDoc().getBytes()));
        String json = new Gson().toJson(new TheseMappee(mets));
        item.setJson(json);
        return item;
    }
}