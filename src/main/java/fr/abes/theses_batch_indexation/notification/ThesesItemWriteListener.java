package fr.abes.theses_batch_indexation.notification;

import fr.abes.theses_batch_indexation.database.TheseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Qualifier("theseWriteListener")
public class ThesesItemWriteListener implements ItemWriteListener<TheseModel> {

    @Override
    public void beforeWrite(List<? extends TheseModel> items) {
    }
    /**
     * Ecrire le nnt dans le fichier de log
     */
    @Override
    public void afterWrite(List<? extends TheseModel> items) {
        if (!items.isEmpty()) {
            for (TheseModel theseModel : items) {
                log.debug("Writer OK pour " + theseModel.getIdDoc());
            }
        }
    }

    @Override
    public void onWriteError(Exception exception, List<? extends TheseModel> items) {
        for (TheseModel theseModel : items) {
            log.error("Writer KO  pour " + theseModel.getIdDoc());
            log.error(exception.toString());
        }
    }

}
