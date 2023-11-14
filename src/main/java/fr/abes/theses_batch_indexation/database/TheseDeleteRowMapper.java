package fr.abes.theses_batch_indexation.database;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleResultSet;
import oracle.xdb.XMLType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class TheseDeleteRowMapper implements RowMapper<TheseModel> {

    @Override
    public TheseModel mapRow(ResultSet rs, int rowNum) throws SQLException {

    try {
        TheseModel these = new TheseModel();
        OracleResultSet rsOra = (OracleResultSet) rs;

        these.setIdDoc(rsOra.getInt("iddoc"));
        log.info("dans TheseDeleteRowMapper, l'iddoc est : " + these.getIdDoc());
        these.setNnt(rsOra.getString("nnt"));
        these.setIdSujet(rsOra.getString("numsujet"));


        return these;

    }
        catch (NullPointerException e) {
        log.error("dans TheseRowMapper : " + e.toString());
        return null;
    }
  }
}
