package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class TheseItemReader {

    private final JobConfig config;
    protected final DataSource dataSourceLecture;

    @Value("${index.name}")
    private String nomIndex;

    @Value("${table.name}")
    private String nomTable;


    public TheseItemReader(JobConfig config, DataSource dataSourceLecture) {
        this.config = config;
        this.dataSourceLecture = dataSourceLecture;
    }

    @Bean
    public ItemReader<TheseModel> read() {
        log.info("début du reader these thread safe...");

        try {
            return new JdbcPagingItemReaderBuilder<TheseModel>().name("theseReader").dataSource(dataSourceLecture).queryProvider(createQueryProvider()).rowMapper(new TheseRowMapper()).pageSize(config.getChunk()).build();
        } catch (Exception e) {
            log.error("erreur lors de la creation du JdbcPagingItemReader : " + e);
            return null;
        }
    }

    private PagingQueryProvider createQueryProvider() {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("SELECT iddoc, nnt, doc, numsujet");
        queryProvider.setFromClause("from " + nomTable);

        setWhereClause(queryProvider);
        queryProvider.setSortKeys(sortByIdAsc());
        return queryProvider;
    }

    private void setWhereClause(OraclePagingQueryProvider queryProvider) {
        if (nomTable.toLowerCase().contains("document_test")) {
            queryProvider.setWhereClause("where nom_index = '" + nomIndex + "'");
        } else {
            if (config.getWhereLimite() > 0)
                queryProvider.setWhereClause("where rownum < " + config.getWhereLimite());
        }
    }

    private Map<String, Order> sortByIdAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("iddoc", Order.ASCENDING);
        return sortConfiguration;
    }
}
