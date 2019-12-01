package space.cloud4b.verein.services;

import space.cloud4b.verein.model.verein.adressbuch.Kontakt;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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

    public static ArrayList<Mitglied> getMitgliederAsArrayList() {
        Status anredeStatus = new Status(1);
        ArrayList<Mitglied> mitgliederListe = new ArrayList<>();
        ArrayList<Kontakt> kontaktListe = new ArrayList<>();
        LocalDate heute = LocalDate.now();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * from kontakt ORDER BY KontaktNachname, KontaktVorname";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Kontakt kontakt;
                int kontaktId = rs.getInt("KontaktId");
                String kontaktNachname = rs.getString("KontaktNachname");
                String kontaktVorname = rs.getString("KontaktVorname");

                // Überprüfe den Mitglieder-Status
                // aktives Mitglied -->
              /*  if(!rs.getString("KontaktEintrittsdatum").isEmpty() && rs.getString("KontaktAustrittsdatum").isEmpty()){
                      // Date.valueOf(rs.getString("KontaktAustrittsdatum")).toLocalDate().isAfter(heute)){
                    System.out.println("-------> aktives Mitglied");
                } else {
                    System.out.println("----> kein Mitglied");
                }*/



                /**
                 * Objekte werden erzeugt
                 */
                if(rs.getBoolean("KontaktIstMitglied") ) {
                    kontakt = new Mitglied(kontaktId, kontaktNachname, kontaktVorname);
                }  else {
                    kontakt = new Kontakt(kontaktId, kontaktNachname, kontaktVorname);
                }
                System.out.println("Objekt wurde angelegt: " + kontakt.toString() + "/" + kontakt.getClass());

                /**
                 * Instanzvariabeln der neuen Objekte werden vervollständigt
                 */
                kontakt.setAdresse(rs.getString("KontaktAdresse"));
                kontakt.setAdresszusatz(rs.getString("KontaktAdresszusatz"));

                kontakt.setPlz(rs.getInt("KontaktPLZ"));
                kontakt.setOrt(rs.getString("KontaktOrt"));

                kontakt.setBemerkungen(rs.getString("KontaktBemerkungen"));
                kontakt.setMobile(rs.getString("KontaktMobile"));
                kontakt.setTelefon(rs.getString("KontaktTelefon"));
                kontakt.setEmail(rs.getString("KontaktEMail"));
                kontakt.setEmailII(rs.getString("KontaktEMailII"));

                String kontaktGeburtsdatumString = rs.getString("KontaktGeburtsdatum");
                String kontaktAustrittsdatumString = rs.getString("KontaktAustrittsdatum");
                LocalDate kontaktGeburtsdatum = null;
                LocalDate kontaktAustrittsdatum = null;
                if(kontaktGeburtsdatumString != null && kontaktGeburtsdatumString !="0000-00-00" && !kontaktGeburtsdatumString.isEmpty()) {
                    kontaktGeburtsdatum = Date.valueOf(kontaktGeburtsdatumString).toLocalDate();
                    kontakt.setGeburtsdatum(kontaktGeburtsdatum);
                }


                if(kontaktAustrittsdatumString != null && kontaktAustrittsdatumString != "0000-00-00" && !kontaktAustrittsdatumString.isEmpty()) {
                    kontaktAustrittsdatum = Date.valueOf(kontaktAustrittsdatumString).toLocalDate();
                    kontakt.setAustrittsDatum(kontaktAustrittsdatum);
                }

                //kontakt.setAnredeStatus(rs.getInt("KontaktAnredeStatus"));
                kontakt.setAnredeStatus(anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus")));

                if(kontakt instanceof Mitglied) {
                    String kontaktEintrittsdatumString = rs.getString("KontaktEintrittsdatum");
                    LocalDate kontaktEintrittsdatum = null;
                    if(kontaktEintrittsdatumString != null && kontaktEintrittsdatumString !="0000-00-00") {
                        kontaktEintrittsdatum = Date.valueOf(kontaktEintrittsdatumString).toLocalDate();
                    }

                    ((Mitglied) kontakt).setEintrittsDatum(kontaktEintrittsdatum);

                }
                System.out.println("Objekt wurde vervollständigt: " + kontakt.toString() + "/" + kontakt.getClass());

                /**
                 * Kontakt wird zur Liste hinzugefügt
                 */
                kontaktListe.add(kontakt);
                if(kontakt instanceof Mitglied) {
                    mitgliederListe.add((Mitglied) kontakt);
                }


            }

        } catch (SQLException e) {
            System.out.println("SQL-Connection steht für DatenLieferung nicht zur Verfügung");

        }

        return mitgliederListe;
    }
}
