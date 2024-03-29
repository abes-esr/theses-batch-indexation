package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseDeleteRowMapper;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JdbcPagingDeleteReader extends JdbcPagingItemReader
        implements ItemReader {
    private JobConfig config;

    public JdbcPagingDeleteReader(
            @Autowired Environment env,
            @Autowired MappingJobName mappingJobName,
            @Qualifier("jobConfig") JobConfig config,
            @Qualifier("dataSourceLecture") DataSource dataSourceLecture) {


        this.config = config;
        this.setDataSource(dataSourceLecture);
        this.setName("theseDeleteReader");
        this.setQueryProvider(createQueryProvider(mappingJobName.getNomTableES().get(env.getProperty("spring.batch.job.names"))));
        this.setRowMapper(new TheseDeleteRowMapper());
        this.setPageSize(config.getChunk());

    }

    private PagingQueryProvider createQueryProvider(TableIndexationES nomTableIndexationES) {
        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("SELECT iddoc, nnt, numsujet");
        queryProvider.setFromClause("from " + nomTableIndexationES.name().toUpperCase());
        queryProvider.setSortKeys(sortByIdAsc());
        return queryProvider;
    }

    private Map<String, Order> sortByIdAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("iddoc", Order.ASCENDING);
        return sortConfiguration;
    }
}
