package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
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

        HashMap<String, String> nomColonneES = new HashMap<String, String>();
        nomColonneES.put("indexationThesesDansES", "ENVOIELASTICTHESE");
        nomColonneES.put("indexationPersonnesDansES", "ENVOIELASTICPERSONNE");
        nomColonneES.put("indexationRecherchePersonnesDansES", "ENVOIELASTICRECHERCHEPERSONNE");
        nomColonneES.put("indexationThematiquesDansES", "ENVOIELASTICTHEMATIQUE");

        this.config = config;
        this.setDataSource(dataSourceLecture);
        this.setName("theseReader");
        this.setQueryProvider(createQueryProvider(nomColonneES.get(env.getProperty("spring.batch.job.names"))));
        this.setRowMapper(new TheseRowMapper());
        this.setPageSize(config.getChunk());

    }

    private PagingQueryProvider createQueryProvider(String nomColonne) {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("SELECT iddoc, nnt, doc, numsujet");
        queryProvider.setFromClause("from DOCUMENT");
        setWhereClause(queryProvider, nomColonne);
        queryProvider.setSortKeys(sortByIdAsc());
        return queryProvider;
    }
    private void setWhereClause(
            OraclePagingQueryProvider queryProvider,
            String nomColonne) {

        queryProvider.setWhereClause("where rownum < "+ config.getWhereLimite() +" AND "
                + nomColonne +" = 0");
    }

    private Map<String, Order> sortByIdAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("iddoc", Order.ASCENDING);
        return sortConfiguration;
    }
}
