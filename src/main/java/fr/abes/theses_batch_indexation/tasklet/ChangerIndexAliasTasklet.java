package fr.abes.theses_batch_indexation.tasklet;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.UpdateAliasesRequest;
import co.elastic.clients.elasticsearch.indices.UpdateAliasesResponse;
import co.elastic.clients.elasticsearch.indices.update_aliases.Action;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.utils.ElasticSearchUtils;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.NomIndexSelecteur;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ChangerIndexAliasTasklet implements Tasklet {

    private final MappingJobName mappingJobName;

    private final NomIndexSelecteur nomIndexSelecteur;

    @Value("${nombre.minimal.personnes}")
    private long nombreMinimalPersonnes;

    public ChangerIndexAliasTasklet(MappingJobName mappingJobName, NomIndexSelecteur nomIndexSelecteur) {
        this.mappingJobName = mappingJobName;
        this.nomIndexSelecteur = nomIndexSelecteur;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {


        //todo: verifier que l'init soit bien fait (et le faire avant la bdd : comme ca même s'il y a une erreur pendant
        //      la phase bdd, on est assuré que ca ne passera pas le second test) OK
        //TODO : test si il y a au moins 800K personnes dans l'index  Ok
        //TODO: Sépaarer initialiseIndex OK

        String newIndex = mappingJobName.getNomIndexES().get(chunkContext.getStepContext().getJobName());
        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils(newIndex);
        String alias = mappingJobName.getNomAliasES().get(chunkContext.getStepContext().getJobName());
        String oldIndex = nomIndexSelecteur.getNomIndexActuel(alias);

        if (elasticSearchUtils.countIndex() < nombreMinimalPersonnes) {
            throw new Exception("Moins de " + nombreMinimalPersonnes + " dans l'index " + newIndex);
        }


        try {
            // Supprimer l'alias de l'ancien index
            Action removeAliasAction = Action.of(a -> a.remove(r -> r.index(oldIndex).alias(alias)));

            // Ajouter l'alias au nouvel index
            Action addAliasAction = Action.of(a -> a.add(r -> r.index(newIndex).alias(alias)));

            // Construire la requête de mise à jour des alias
            UpdateAliasesRequest request = UpdateAliasesRequest.of(uar -> uar.actions(removeAliasAction, addAliasAction));

            // Exécuter la requête
            UpdateAliasesResponse response = ElasticClient.getElasticsearchClient().indices().updateAliases(request);

            if (response.acknowledged()) {
                log.info("Alias mis à jour avec succès.");
            } else {
                log.error("Échec de la mise à jour de l'alias.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return RepeatStatus.FINISHED;
    }
}
