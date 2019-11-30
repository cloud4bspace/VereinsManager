package space.cloud4b.verein.daten.mysql.service;

import space.cloud4b.verein.daten.mysql.connection.MysqlConnection;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.Status;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatenManipulator {

    /**
     * Ermittelt verschiedene Daten zum übergebenen Status-Objekt und
     * ergänzt die entsprechenden Instanzvariabeln
     */
    public static void updateMitglied(Mitglied mitglied) {
       /* try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * FROM status WHERE StatusId=" + status.getStatusId();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                status.setStatusTextLang(rs.getString("StatusNameLong"));
                status.setStatusTextKurz(rs.getString("StatusNameShort"));
                status.setStatusSymbol(rs.getString("StatusSymbol"));
            }
        } catch (SQLException e) {
            System.out.println("Status-Informationen konnten nicht ermittelt werden");
        }*/
    }
}
