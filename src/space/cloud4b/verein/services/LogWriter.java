package space.cloud4b.verein.services;

import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;

public abstract class LogWriter {

    public static void writeMitgliedUpdateLog(Mitglied mitglied) {
        try (FileWriter fw = new FileWriter("ressources/files/logfiles/logfile.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println();
            out.print("***** ");
            out.print(new Timestamp(new Date().getTime()));
            out.print(" | Contact #" + mitglied.getId() + " UPDATE");
            out.println(" *****");
            out.print(System.getProperty("user.name") + " | ");
            out.print(System.getProperty("user.home") + " | ");
            out.print(System.getProperty("os.name") + " | ");
            out.print(System.getProperty("user.timezone") + " | ");
            out.println(System.getProperty("http.nonProxyHosts"));
            out.println("***** old values in mysql-db *****");

            try (Connection conn = new MysqlConnection().getConnection();
                 Statement st = conn.createStatement()) {
                String query = "SELECT * FROM usr_web116_5.kontakt WHERE KontaktId=" + mitglied.getId();
                ResultSet result = st.executeQuery(query);
                ResultSetMetaData rsmd = result.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (result.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        out.print(rsmd.getColumnName(i) + "=");
                        out.print(result.getString(i) + " | ");
                    }
                    out.println();
                }
            }
            out.println("***** neu values from contact-object *****");
            // TODO getter bei Contact noch nicht komplett
            out.println("MitgliedId=" + mitglied.getId() + " | KontaktNachname=" + mitglied.getNachName() + " | KontaktVorname=" + mitglied.getVorname() + " | KontaktAdresse=" + mitglied.getAdresse() + " | KontaktAdresszusatz=" + mitglied.getAdresszusatz() + " | KontaktOrt=" + mitglied.getOrt() + " | KontaktPLZ=" + mitglied.getPlz() + " | KontaktGeburtsdatum=" + mitglied.getGeburtsdatum() + " | KontaktAnredeStatus=" + mitglied.getAnredeElement() + " | KontaktKategorieA=" + mitglied.getKategorieIElement() + " | KontaktKategorieB=" + mitglied.getKategorieIIElement() + " | KontaktIstVorstandsmitglied=" + mitglied.getIstVorstandsmitglied());
        } catch (IOException | SQLException e) {
            //exception handling left as an exercise for the reader
        }

    }

    public static void writeTerminUpdateLog(Termin termin) {
        //TOT
    }
}
