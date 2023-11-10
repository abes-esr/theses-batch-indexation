package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JdbcPagingCustomReader
        extends JdbcPagingItemReader
        implements ItemReader {
    private JobConfig config;

    public JdbcPagingCustomReader(
            @Autowired Environment env,
            @Qualifier("jobConfig") JobConfig config,
            @Qualifier("dataSourceLecture") DataSource dataSourceLecture) {

        HashMap<String, TableIndexationES> nomTableES = new HashMap<String, TableIndexationES>();
        // correspondance nom du job / nom de la table dans la BD
        nomTableES.put("indexationThesesDansES", TableIndexationES.indexation_es_these);
        nomTableES.put("indexationPersonnesDansES", TableIndexationES.indexation_es_personne);
        nomTableES.put("indexationRecherchePersonnesDansES", TableIndexationES.indexation_es_recherche_personne);
        nomTableES.put("indexationThematiquesDansES", TableIndexationES.indexation_es_thematique);

        this.config = config;
        this.setDataSource(dataSourceLecture);
        this.setName("theseReader");
        this.setQueryProvider(createQueryProvider(nomTableES.get(env.getProperty("spring.batch.job.names"))));
        this.setRowMapper(new TheseRowMapper());
        this.setPageSize(config.getChunk());

    }

    private PagingQueryProvider createQueryProvider(TableIndexationES nomTableIndexationES) {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("SELECT DOCUMENT.iddoc, DOCUMENT.nnt, doc, DOCUMENT.numsujet");

        if (config.getNomTable().toLowerCase().contains("document_test")) {
            queryProvider.setFromClause("from "+ config.getNomTable());
            queryProvider.setWhereClause("where nom_index = '" + config.getNomIndex() + "'");
            queryProvider.setSortKeys(sortByIdAsc());
            return queryProvider;
        }

        queryProvider.setFromClause("from DOCUMENT, " + nomTableIndexationES.name());
        setWhereClause(queryProvider, nomTableIndexationES);
        queryProvider.setSortKeys(sortByIdAsc());
        return queryProvider;
    }
    private void setWhereClause(
            OraclePagingQueryProvider queryProvider, TableIndexationES nomTableIndexationES) {

        if (config.getWhereLimite() > 0) {
            queryProvider.setWhereClause("where DOCUMENT.iddoc = "+ nomTableIndexationES.name() + ".iddoc and rownum < " + config.getWhereLimite());
        }
        else {
            queryProvider.setWhereClause("where DOCUMENT.iddoc = "+ nomTableIndexationES.name() +".iddoc");
        }
    }

    private Map<String, Order> sortByIdAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("iddoc", Order.ASCENDING);
        return sortConfiguration;
    }
}
