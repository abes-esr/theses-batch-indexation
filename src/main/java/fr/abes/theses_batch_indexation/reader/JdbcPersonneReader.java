package fr.abes.theses_batch_indexation.reader;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@Slf4j
public class JdbcPersonneReader implements ItemReader<TheseModel>, StepExecutionListener {

    final DataSource dataSourceLecture;

    MappingJobName mappingJobName = new MappingJobName();
    JdbcTemplate jdbcTemplate;

    private String tableName;

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
    }


    @Override
    public TheseModel read() {

        List<TheseModel> theseModels= jdbcTemplate.query("select * from " + tableName + " where iddoc = 523634 FETCH NEXT 1 ROWS ONLY",
                new TheseRowMapper());

        return theseModels.stream().findFirst().orElse(null);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
