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


    public TheseItemReader(JobConfig config,
                           DataSource dataSourceLecture) {
        this.config = config;
        this.dataSourceLecture = dataSourceLecture;
    }

    @Bean
    public ItemReader<TheseModel> read() {
        log.info("début du reader these thread safe...");

        try {
            return new JdbcPagingItemReaderBuilder<TheseModel>().name("theseReader")
                    .dataSource(dataSourceLecture)
                    .queryProvider(createQueryProvider())
                    .rowMapper(new TheseRowMapper())
                    .pageSize(config.getChunk())
                    .build();
        } catch (Exception e) {
            log.error("erreur lors de la creation du JdbcPagingItemReader : " + e);
            return null;
        }
    }

    private PagingQueryProvider createQueryProvider() {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("SELECT iddoc, nnt, doc, numsujet");
        queryProvider.setFromClause("from " + nomTable);
        if (nomIndex.length() > 0) {

            //queryProvider.setWhereClause("where nnt = '1993BOR23095'");
            //queryProvider.setWhereClause("where rownum < " + config.getWhereLimite());
            Map<String, Order> orderKeys = new HashMap<>();
            orderKeys.put("iddoc", Order.ASCENDING);
            queryProvider.setSortKeys(orderKeys);
            //queryProvider.setWhereClause("where nnt = '2000PA010697' or nnt = '2001MNHN0022'or nnt = '2003MON30025' or nnt = '2003PA100181' or nnt = '2011AIX10218' or nnt = '2012PA010501' or nnt = '2014TOU20035' or nnt = '2014TOU20047' or nnt = '2015TOU20116' or nnt = '2020PA100137' or nnt = '2020TOU20084'");
            //queryProvider.setWhereClause("where numsujet = 's347362'");
            queryProvider.setWhereClause("where nom_index = '" + nomIndex + "'");
        }
        queryProvider.setSortKeys(sortByIdAsc());
        return queryProvider;
    }

    private Map<String, Order> sortByIdAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("iddoc", Order.ASCENDING);
        return sortConfiguration;
    }
}
