package fr.abes.theses_batch_indexation.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void marqueTheseCommeIndexee (String id) {
        jdbcTemplate.update("UPDATE DOCUMENT SET ENVOIELASTICTHESE = 1 WHERE NNT = ? OR NUMSUJET = ?", id, id);
    }
}
