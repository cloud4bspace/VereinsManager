package space.cloud4b.verein.services;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class DatabaseOperation {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm");

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
     * neues Mitglied in der Datenbank anlegen
     */
    public static int saveNewMember(String nachname, String vorname, String eintrittsDatum){

        String query= "INSERT INTO usr_web116_5.kontakt (KontaktId, KontaktNachname, " +
                    "KontaktVorname, KontaktEintrittsdatum, KontaktIstMitglied, KontaktTrackChangeUsr, " +
                    "KontaktTrackChangeTimestamp) VALUES (NULL, ?, ?, ?, '1', ?, CURRENT_TIMESTAMP)";
        MysqlConnection conn = new MysqlConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nachname);
            ps.setString(2, vorname);
            ps.setString(3, eintrittsDatum);
            ps.setString(4, System.getProperty("user.name"));
            System.out.println("neues Mitglied hinzugefügt: " + ps.executeUpdate());
           // System.out.println(ps.getGeneratedKeys());
            ResultSet keys = null;
            keys = ps.getGeneratedKeys();
            keys.next();
            int newKey = keys.getInt(1);
            return newKey;

    } catch(SQLException e) {
        System.out.println("Fehler " + e);
        }
        return 0;
    }

    /**
     * Mitglied in den Datenbanken löschen
     */
    public static void deleteMitglied(Mitglied mitglied) {
        try(Connection conn = new MysqlConnection().getConnection();
            Statement st = conn.createStatement()) {
            String query= "DELETE FROM kontakt WHERE KontaktId=" + mitglied.getId();
            st.execute(query);


            query = "DELETE FROM `terminkontrolle` WHERE `KontrolleMitgliedId` = " + mitglied.getId();
            st.execute(query);

        } catch(SQLException e) {
            System.out.println("Mitglied konnte nicht gelöscht werden (" + e + ")");
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
        LogWriter.writeMitgliedUpdateLog(mitglied);
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

    /**
     * Das Übergebene Objekt vom Typ Termin wird in der Termin-Tabelle aktualisiert
     * @param termin der geänderte Termin
     */
    public static void updateTermin(Termin termin) {
        LogWriter.writeTerminUpdateLog(termin);
        MysqlConnection conn = new MysqlConnection();

        // create the java mysql update preparedstatement
        String query = "UPDATE usr_web116_5.termin SET TerminDatum = ?,"
                + " TerminText = ?,"
                + " TerminOrt = ?,"
                + " TerminDetails = ?,"
                + " TerminZeit = ?,"
                + " TerminZeitBis = ?," // 6
                + " TerminTeilnehmerKatA = ?, "
                + " TerminTeilnehmerKatB = ?, "
                + " TerminTrackChangeUsr = ?,"
                + " TerminTrackChangeTimestamp = CURRENT_TIMESTAMP"
                + " WHERE TerminId = ?";
        //einzelne Änderungen abfragen und dann Wert alt und neu, User und Timestamp in logdatei schreiben
        PreparedStatement preparedStmt = null;
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            preparedStmt = conn.getConnection().prepareStatement(query);
            preparedStmt.setString(1, termin.getDatum().toString());
            preparedStmt.setString(2, termin.getTerminText().getValue());
            preparedStmt.setString(3, termin.getOrt());
            preparedStmt.setString(4, termin.getDetails());
            if(termin.getTerminZeitVon() == null) {
                preparedStmt.setString(5, "");
            } else {
                preparedStmt.setString(5, termin.getTerminZeitVon().format(formatter));
            }
            if(termin.getTerminZeitBis() == null) {
                preparedStmt.setString(6, "");
            } else {
                preparedStmt.setString(6, termin.getTerminZeitBis().format(formatter));
            }
            preparedStmt.setInt(7, termin.getKatIElement().getStatusElementKey());
            preparedStmt.setInt(8, termin.getKatIIElement().getStatusElementKey());
            preparedStmt.setString(9, System.getProperty("user.name"));
            preparedStmt.setInt(10, termin.getTerminId());
            // execute the java preparedstatement
            System.out.println("Rückmeldung preparedStmt: " + preparedStmt.executeUpdate());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * legt in der MySql-Datenbank einen neuen Termin an mit den übergebenen Werten
     * @param terminText Bezeichnung des Termins
     * @param terminDatum Datum des Termins
     * @return gibt die Id des neuen Datensatzes zurück
     */
    public static int saveNewTermin(String terminText, String terminDatum) {

        String query= "INSERT INTO usr_web116_5.termin (TerminId, TerminDatum, TerminText, " +
                "TerminTrackChangeUsr, " +
                "TerminTrackChangeTimestamp) VALUES (NULL, ?, ?, ?, CURRENT_TIMESTAMP)";
        MysqlConnection conn = new MysqlConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, terminDatum);
            ps.setString(2, terminText);
            ps.setString(3, System.getProperty("user.name"));
            System.out.println("neuer Temin hinzugefügt: " + ps.executeUpdate());
            // System.out.println(ps.getGeneratedKeys());
            ResultSet keys = null;
            keys = ps.getGeneratedKeys();
            keys.next();
            int newKey = keys.getInt(1);
            return newKey;

        } catch(SQLException e) {
            System.out.println("Fehler " + e);
        }
        return 0;
    }
}
