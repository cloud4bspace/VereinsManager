package space.cloud4b.verein.services;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public abstract class DatabaseOperation {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    /**
     * Erstellt die erforderlichen Tabellen für Adressen und Termine aus Tabellen-Vorlagen
     * und füllt sie mit initialen Datensätzen
     */
    public static void createDatabaseFromTemplate() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "TRUNCATE usr_web116_5.kontakt;";
            st.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS kontakt LIKE kontakt_template;";
            st.executeUpdate(query);
            query = "INSERT kontakt SELECT * FROM kontakt_template;";
            st.executeUpdate(query);
            System.out.println("Datenbank für Kontakte erstellt und mit Beispieldaten gefüllt");

            query = "TRUNCATE usr_web116_5.termin;";
            st.executeUpdate(query);
            query = "CREATE TABLE IF NOT EXISTS usr_web116_5.termin LIKE usr_web116_5.termin_template;";
            st.executeUpdate(query);
            query = "INSERT usr_web116_5.termin SELECT * FROM usr_web116_5.termin_template;";
            st.executeUpdate(query);
            System.out.println("Datenbank für Termine erstellt und mit Beispieldaten gefüllt");

        } catch (SQLException e) {
            System.out.println("Datenbanken konnten nicht bereitgestellt werden (" + e + ")");

        }
    }

    /**
     * Prüft aufgrund des Austrittsdatums den aktuellen Mitgliederstatus
     */
    public static void checkMitgliederStatus() {
        try(Connection conn = new MysqlConnection().getConnection();
            Statement st = conn.createStatement()) {
            String query= "UPDATE usr_web116_5.kontakt SET `KontaktIstMitglied` = 0 WHERE KontaktAustrittsdatum < CURRENT_DATE AND KontaktAustrittsdatum NOT LIKE '0000-00-00'";
            st.executeUpdate(query);

        } catch(SQLException e) {

        }


    }

    /**
     * Das Übergebene Objekt vom Typ Mitglied wird in der Kontakt-Tabelle aktualisiert
     * @param mitglied das geänderte und zu aktualisierende Mitlied
     */
    public static void updateMitglied(Mitglied mitglied) {
        // writeLog(contact);
        MysqlConnection conn = new MysqlConnection();
        boolean istMitglied = false;

        // create the java mysql update preparedstatement
        String query = "UPDATE usr_web116_5.kontakt SET KontaktNachname = ?,"
                + " KontaktVorname = ?,"
                + " KontaktAdresse = ?,"
                + " KontaktAdresszusatz = ?,"
                + " KontaktPLZ = ?,"
                + " KontaktOrt = ?," // 6
                + " KontaktGeburtsdatum = ?, "
                + " KontaktAnredeStatus = ?, "
                + " KontaktEintrittsdatum = ?,"
                + " KontaktAustrittsdatum = ?," // 10
                + " KontaktKategorieA = ?,"
                + " KontaktKategorieB = ?,"
                + " KontaktIstMitglied = ?,"
                + " KontaktIstVorstandsmitglied = ?,"
                + " KontaktBemerkungen = ?,"
                + " KontaktMobile = ?,"
                + " KontaktTelefon = ?,"
                + " KontaktEMail = ?,"
                + " KontaktEMailII = ?,"
                + " KontaktTrackChangeUsr = ?,"
                + " KontaktTrackChangeTimestamp = CURRENT_TIMESTAMP"
                + " WHERE KontaktId = ?";
        //einzelne Änderungen abfragen und dann Wert alt und neu, User und Timestamp in logdatei schreiben
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.getConnection().prepareStatement(query);
            preparedStmt.setString(1, mitglied.getNachName());
            preparedStmt.setString(2, mitglied.getVorname());
            preparedStmt.setString(3, mitglied.getAdresse());
            preparedStmt.setString(4, mitglied.getAdresszusatz());
            preparedStmt.setInt(5, mitglied.getPlz());
            preparedStmt.setString(6, mitglied.getOrt());
            preparedStmt.setString(7, mitglied.getGeburtsdatum().toString());
            preparedStmt.setInt(8, mitglied.getAnredeElement().getStatusElementKey());
            preparedStmt.setString(9, mitglied.getEintrittsdatum().toString());
            if (mitglied.getAustrittsDatum() != null) {
                if (mitglied.getAustrittsDatum().isAfter(LocalDate.now())) {
                    istMitglied = true;
                } else {
                    istMitglied = false;
                }
                preparedStmt.setString(10, mitglied.getAustrittsDatum().toString());
            } else {
                istMitglied = true;
                preparedStmt.setString(10, null);
            }
            preparedStmt.setInt(11, mitglied.getKategorieIElement().getStatusElementKey());
            preparedStmt.setInt(12, mitglied.getKategorieIIElement().getStatusElementKey());

            preparedStmt.setBoolean(13, istMitglied);
            preparedStmt.setBoolean(14, mitglied.getIstVorstandsmitglied().getValue());
            preparedStmt.setString(15, mitglied.getBemerkungen());
            preparedStmt.setString(16, mitglied.getMobile());
            preparedStmt.setString(17, mitglied.getTelefon());
            preparedStmt.setString(18, mitglied.getEmail());
            preparedStmt.setString(19, mitglied.getEmailII());
            preparedStmt.setString(20, System.getProperty("user.name"));
            preparedStmt.setInt(21, mitglied.getId());
            // execute the java preparedstatement
            System.out.println("Rückmeldung preparedStmt: " + preparedStmt.executeUpdate());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
