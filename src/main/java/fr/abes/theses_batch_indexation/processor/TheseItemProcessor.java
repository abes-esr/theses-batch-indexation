package fr.abes.theses_batch_indexation.processor;

import java.io.ByteArrayInputStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import fr.abes.theses_batch_indexation.dto.TheseDTO;
import fr.abes.theses_batch_indexation.dto.TheseMappee;
import fr.abes.theses_batch_indexation.model.jaxb.Mets;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
@Slf4j
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
