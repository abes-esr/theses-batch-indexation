package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class JdbcPersonneReader implements ItemReader<TheseModel>, StepExecutionListener {

    final DataSource dataSourceLecture;

    @Autowired
    MappingJobName mappingJobName;
    JdbcTemplate jdbcTemplate;

    List<TheseModel> theseModels;

    private String tableName;

    private AtomicInteger n = new AtomicInteger();

    public JdbcPersonneReader(@Qualifier("dataSourceLecture") DataSource dataSourceLecture, JdbcTemplate jdbcTemplate) {
        this.dataSourceLecture = dataSourceLecture;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setDataSource(dataSourceLecture);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.tableName = mappingJobName.getNomTableES().get(
                stepExecution.getJobExecution().getJobInstance().getJobName()
        ).name();

        theseModels= jdbcTemplate.query("select * from " + tableName + " where nnt is not null FETCH NEXT 10 ROWS ONLY",
                new TheseRowMapper());
        n.set(0);
    }


    @Override
    public TheseModel read() {

        return theseModels.get(n.getAndIncrement());

        //return theseModels.stream().findFirst().orElse(null);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
