package fr.abes.theses_batch_indexation.processor;

import com.google.gson.Gson;
import fr.abes.theses_batch_indexation.dto.personne.PersonneDTO;
import fr.abes.theses_batch_indexation.dto.personne.PersonneMapee;
import fr.abes.theses_batch_indexation.model.jaxb.Mets;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;

@Slf4j
public class PersonneItemProcessor implements ItemProcessor<PersonneDTO, PersonneDTO> {

    @Autowired
    private XMLJsonMarshalling marshall;

    @Override
    public PersonneDTO process(PersonneDTO personneDTO) throws Exception {
        log.info("debut de traitement de " + personneDTO.getNnt());
        Mets mets = marshall.chargerMets(new ByteArrayInputStream(personneDTO.getDoc().getBytes()));
        String json = new Gson().toJson(new PersonneMapee(mets));
        personneDTO.setJson(json);
        return personneDTO;
    }
}
