package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
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
import java.util.Map;

@Component
@Slf4j
public class JdbcPagingCustomPersonneReader
        extends JdbcPagingItemReader
        implements ItemReader {
        private JobConfig config;

    public JdbcPagingCustomPersonneReader(
                @Autowired Environment env,
                MappingJobName mappingJobName,
                @Qualifier("jobConfig") JobConfig config,
                @Qualifier("dataSourceLecture") DataSource dataSourceLecture) {


            this.config = config;
            this.setDataSource(dataSourceLecture);
            this.setName("customPersonneReader");
            this.setQueryProvider(createQueryProvider(mappingJobName.getNomTableES().get(env.getProperty("spring.batch.job.names"))));
            this.setRowMapper(new TheseRowMapper());
            this.setPageSize(config.getChunk());

        }

        private PagingQueryProvider createQueryProvider(TableIndexationES nomTableIndexationES) {
            OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();

            queryProvider.setSelectClause("SELECT IDDOC, NNT, NUMSUJET, DOC, ORDRE");
            queryProvider.setFromClause("(SELECT IDDOC, NNT, NUMSUJET, DOC, ORDRE, ROW_NUMBER() OVER (PARTITION BY IDDOC ORDER BY ordre desc) AS rn FROM "
                    + nomTableIndexationES.name() + ") subquery");
            queryProvider.setWhereClause("WHERE rn = 1");
            queryProvider.setSortKeys(Map.of("ordre", Order.ASCENDING));

            return queryProvider;
        }
    }

