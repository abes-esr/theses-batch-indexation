package fr.abes.theses_batch_indexation.notification;

import fr.abes.theses_batch_indexation.database.TheseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("theseProcessListener")
public class ThesesItemProcessListener implements ItemProcessListener<TheseModel, TheseModel> {

    @Override
    public void beforeProcess(TheseModel o) {
        log.debug("begin process of " + o.getIdDoc() + "...");
    }

    @Override
    public void afterProcess(TheseModel o, TheseModel o2) {
        log.debug("... end process of " + o.getIdDoc());

    }

    @Override
    public void onProcessError(TheseModel o, Exception e) {
        log.error("erreur lors du process de " + o.getIdDoc() + ", erreur = " + e.toString());
    }
}
