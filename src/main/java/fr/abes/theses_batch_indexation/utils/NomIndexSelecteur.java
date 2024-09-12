package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class NomIndexSelecteur {

    private final ElasticConfig elasticConfig;

    @Value("#{'${indexes.suffixe.personnes}'.split(';')}")
    private List<String> suffixeList;

    public NomIndexSelecteur(ElasticConfig elasticConfig) {
        this.elasticConfig = elasticConfig;
    }

    private Set<String> getIndexes(String alias) throws IOException {
        return ElasticClient.getElasticsearchClient().indices().getAlias(
                a -> a.name(alias)
        ).result().keySet();
    }

    public String getNomIndexActuel(String alias) throws Exception {
        ElasticClient.chargeClient(elasticConfig.getHostname(), elasticConfig.getPort(), elasticConfig.getScheme(), elasticConfig.getUserName(), elasticConfig.getPassword(), elasticConfig.getProtocol());

        return getIndexes(alias).stream().findFirst().orElseThrow();

    }

    public String getNomIndexSuivant(String alias) throws Exception {

        ElasticClient.chargeClient(elasticConfig.getHostname(), elasticConfig.getPort(), elasticConfig.getScheme(), elasticConfig.getUserName(), elasticConfig.getPassword(), elasticConfig.getProtocol());

        Set<String> listeIndexActuel = getIndexes(alias);
        String preffixeIndex = alias.substring(0, alias.indexOf("_alias"));

        if (listeIndexActuel.size() > 1) {
            throw new Exception("Il y a plusieurs index dans l'alias :" + alias);
        }

        String nomIndexActuel = listeIndexActuel.stream().findFirst().orElseThrow();
        int i = 0;
        for (String sufixe : suffixeList) {
            if (Objects.equals(preffixeIndex + sufixe, nomIndexActuel)) {
                break;
            } else {
                i++;
            }
        }
        return preffixeIndex + suffixeList.get((i+1) % suffixeList.size());
    }
}
