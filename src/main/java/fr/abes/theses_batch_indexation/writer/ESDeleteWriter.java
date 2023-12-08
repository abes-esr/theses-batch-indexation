package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import fr.abes.theses_batch_indexation.database.DbService;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.ProxyRetry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ESDeleteWriter implements ItemWriter<TheseModel> {

    @Autowired
    ProxyRetry proxyRetry;

    @Autowired
    DbService dbService;
    @Autowired
    private Environment env;
    @Autowired
    MappingJobName mappingJobName;

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {

        BulkRequest.Builder br = new BulkRequest.Builder();
        String nomIndex = mappingJobName.getNomIndexES().get(env.getProperty("spring.batch.job.names"));

        for (TheseModel theseModel : items) {

            br.operations(op -> op
                    .delete(d->d.index(nomIndex.toLowerCase())
                            .id(theseModel.getNnt() == null? theseModel.getIdSujet() : theseModel.getNnt())
            )
        );
    }
        BulkResponse result = proxyRetry.executerDansES(br);

        for (BulkResponseItem item: result.items()) {

            if (item.error() != null) {
                log.error(item.error().reason().concat(" pour ").concat(item.id()));
            }
            else {
                switch (nomIndex) {
                    case "theses" :
                        dbService.supprimerTheseATraiter(item.id(), TableIndexationES.suppression_es_these);
                    case "thematiques":
                        dbService.supprimerTheseATraiter(item.id(), TableIndexationES.suppression_es_thematique);
                }
            }
        }
    }
}
