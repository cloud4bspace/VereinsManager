package space.cloud4b.verein.services;

import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseReader {

    /**
     * Ermittelt verschiedene Daten zum übergebenen Status-Objekt und
     * ergänzt die entsprechenden Instanzvariabeln
     */
    public static int readAnzahlMitglieder() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS AnzahlMitglieder FROM kontakt WHERE KontaktIstMitglied = 1";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("AnzahlMitglieder");
            }
        } catch (SQLException e) {
            System.out.println("Anzahl Mitglieder konnte nicht ermittelt werden ("+ e + ")");

        }
        return 0;
    }

    /**
     * Zählt die Anzahl Termine im laufenden Jahr
     * @return
     */
    public static int readAnzahlTermine() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT COUNT(*) AS AnzahlTermine FROM termin WHERE YEAR(TerminDatum) = YEAR(CURRENT_DATE)";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getInt("AnzahlTermine");
            }
        } catch (SQLException e) {
            System.out.println("Anzahl Termine konnte nicht ermittelt werden ("+ e + ")");

        }
        return 0;
    }
}
