package fr.abes.theses_batch_indexation.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Slf4j
public class DbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("dataSourceLecture") DataSource dataSourceLecture;

    public void supprimerTheseATraiter(String id, TableIndexationES tableIndexationES) {
        jdbcTemplate.setDataSource(dataSourceLecture);
        jdbcTemplate.update("DELETE FROM "+ tableIndexationES.name() +" WHERE NNT = ? OR NUMSUJET = ?", id, id);
        log.info(id + " est indexé.");
    }

    public boolean estPresentDansTableDocument(int iddoc) {
        jdbcTemplate.setDataSource(dataSourceLecture);
        return jdbcTemplate.queryForList("select * from DOCUMENT where iddoc = " + iddoc).size() > 0;
    }

    public void mettreToutesLesThesesAIndexer(TableIndexationES tableIndexationES) {
        jdbcTemplate.setDataSource(dataSourceLecture);
        jdbcTemplate.update("insert into " + tableIndexationES.name() + " (iddoc, nnt, numsujet) select iddoc, nnt, numsujet from document");
    }
}
