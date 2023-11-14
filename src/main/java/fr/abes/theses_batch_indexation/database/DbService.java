package fr.abes.theses_batch_indexation.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void supprimerTheseATraiter(String id, TableIndexationES tableIndexationES) {
        jdbcTemplate.update("DELETE FROM "+ tableIndexationES.name() +" WHERE NNT = ? OR NUMSUJET = ?", id, id);
    }
}
