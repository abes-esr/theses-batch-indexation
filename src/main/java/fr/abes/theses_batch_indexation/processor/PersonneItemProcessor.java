package fr.abes.theses_batch_indexation.processor;

import com.google.gson.Gson;
import fr.abes.theses_batch_indexation.dto.personne.PersonneMapee;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.model.jaxb.Mets;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
public class PersonneItemProcessor implements ItemProcessor<TheseModel, TheseModel> {

    private final XMLJsonMarshalling marshall;

    public PersonneItemProcessor(XMLJsonMarshalling marshall) {
        this.marshall = marshall;
    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {
        log.info("debut de traitement de " + theseModel.getNnt());
        Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModel.getDoc().getBytes()));
        String json = new Gson().toJson(new PersonneMapee(mets));
        theseModel.setJson(json);
        return theseModel;
    }
}
