package fr.abes.theses_batch_indexation.notification;

import fr.abes.theses_batch_indexation.database.TheseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("theseWriteListener")
public class ThesesItemWriteListener implements ItemWriteListener<TheseModel> {

    @Override
    public void beforeWrite(Chunk<? extends TheseModel> items) {
    }
    /**
     * Ecrire le nnt dans le fichier de log
     */
    @Override
    public void afterWrite(Chunk<? extends TheseModel> items) {
        if (!items.isEmpty()) {
            for (TheseModel theseModel : items.getItems()) {
                log.debug("Writer OK pour " + theseModel.getIdDoc());
            }
        }
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends TheseModel> items) {
        for (TheseModel theseModel : items.getItems()) {
            log.error("Writer KO  pour " + theseModel.getIdDoc());
            log.error(exception.toString());
        }
    }

}
