package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JdbcPagingPersonnesCustomReader
        extends JdbcPagingItemReader
        implements ItemReader {
    private JobConfig config;

    public JdbcPagingPersonnesCustomReader(
            @Autowired Environment env,
            @Qualifier("jobConfig") JobConfig config,
            @Qualifier("dataSourceLecture") DataSource dataSourceLecture) {

        this.config = config;
        this.setDataSource(dataSourceLecture);
        this.setName("theseReader");
        this.setQueryProvider(createQueryProvider());
        this.setRowMapper(new TheseRowMapper());
        this.setPageSize(config.getChunk());

    }

    private PagingQueryProvider createQueryProvider() {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("SELECT iddoc, nnt, doc, numsujet");
        queryProvider.setFromClause("from DOCUMENT");
        setWhereClause(queryProvider);
        queryProvider.setSortKeys(sortByIdAsc());
        return queryProvider;
    }

    private void setWhereClause(
            OraclePagingQueryProvider queryProvider) {

        if (config.getNomTable().toLowerCase().contains("document_test")) {
            queryProvider.setWhereClause("where nom_index = '" + config.getNomIndex() + "'");
        } else if (config.getWhereLimite() > 0) {
            queryProvider.setWhereClause("where rownum < " + config.getWhereLimite());
        }
    }

    private Map<String, Order> sortByIdAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("iddoc", Order.ASCENDING);
        return sortConfiguration;
    }
}