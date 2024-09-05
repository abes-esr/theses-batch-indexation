package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@Slf4j
public class JdbcPersonneReader implements ItemReader<TheseModel>, StepExecutionListener {

    final DataSource dataSourceLecture;

    private final MappingJobName mappingJobName;
    JdbcTemplate jdbcTemplate;

    private String tableName;

    public JdbcPersonneReader(@Qualifier("dataSourceLecture") DataSource dataSourceLecture, MappingJobName mappingJobName, JdbcTemplate jdbcTemplate) {
        this.dataSourceLecture = dataSourceLecture;
        this.mappingJobName = mappingJobName;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setDataSource(dataSourceLecture);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.tableName = mappingJobName.getNomTableES().get(
                stepExecution.getJobExecution().getJobInstance().getJobName()
        ).name();
    }


    @Override
    public TheseModel read() {

        List<TheseModel> theseModels= jdbcTemplate.query("select * from " + tableName + " FETCH NEXT 1 ROWS ONLY",
                new TheseRowMapper());

        return theseModels.stream().findFirst().orElse(null);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
