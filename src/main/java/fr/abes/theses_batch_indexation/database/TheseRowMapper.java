package fr.abes.theses_batch_indexation.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.abes.theses_batch_indexation.database.TheseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import oracle.jdbc.OracleResultSet;
import oracle.xdb.XMLType;
@Slf4j
public class TheseRowMapper implements RowMapper<TheseModel> {

    @Override
    public TheseModel mapRow(ResultSet rs, int rowNum) throws SQLException {

        try {
            TheseModel these = new TheseModel();
            OracleResultSet rsOra = (OracleResultSet) rs;

            these.setIdDoc(rsOra.getInt("iddoc"));
            log.debug("dans TheseRowMapper, l'iddoc est : " + these.getIdDoc());
            these.setNnt(rsOra.getString("nnt"));
            these.setIdSujet(rsOra.getString("numsujet"));

            try {
                these.setDoc(XMLType.createXML(rsOra.getOPAQUE("doc")));
            } catch (java.sql.SQLException e) {
            }

            return these;

        }
        catch (NullPointerException e) {
            log.error("dans TheseRowMapper : " + e.toString());
            return null;
        }
    }

}
