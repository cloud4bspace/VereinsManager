package space.cloud4b.verein.services;

import space.cloud4b.verein.model.verein.adressbuch.Kontakt;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.model.verein.kalender.Teilnehmer;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.services.connection.MysqlConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;

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
     * ermittelt die Teilnehmerliste zu einem Termin
     */
    public static ArrayList<Teilnehmer> getTeilnehmer(Termin termin) {
        ArrayList<Teilnehmer> teilnehmerListe = new ArrayList<>();
        Status anmeldung = new Status(5);
        Status teilnahme = new Status(6);
        int terminId = termin.getTerminId();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT kontakt.KontaktId, kontakt.KontaktNachname, kontakt.KontaktVorname FROM terminkontrolle LEFT JOIN kontakt ON kontakt.KontaktId = KontrolleMitgliedId WHERE KontrolleTerminId = " + terminId + " GROUP BY KontrolleMitgliedId";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                teilnehmerListe.add(
                        new Teilnehmer(
                                new Mitglied(rs.getInt("KontaktId"), rs.getString("KontaktNachname"), rs.getString("KontaktVorname"))));
            }
            int i = 0;
            while(teilnehmerListe.size() > i){
                int teilId = teilnehmerListe.get(i).getMitglied().getId();
                query = "SELECT `KontrolleWert` AS AnmeldeStatus FROM `terminkontrolle` " +
                        "WHERE `KontrolleTerminId` = " + terminId + " " +
                        "AND `KontrolleMitgliedId` = " + teilId +
                        " AND `KontrolleArt`='Anmeldung'";
                rs = st.executeQuery(query);
                while (rs.next()) {
                   // anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus"))
                    teilnehmerListe.get(i).setAnmeldeStatus(anmeldung.getStatusElemente().get(rs.getInt("AnmeldeStatus")));
                }
                i++;

            }
            // Infos zum Teilnahmestatus hinzufügen
            i = 0;
            while(teilnehmerListe.size() > i){
                int teilId = teilnehmerListe.get(i).getMitglied().getId();
                query = "SELECT `KontrolleWert` AS TeilnahmeStatus FROM `terminkontrolle` " +
                        "WHERE `KontrolleTerminId` = " + terminId + " " +
                        "AND `KontrolleMitgliedId` = " + teilId +
                        " AND `KontrolleArt`='Anwesenheit'";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    // anredeStatus.getStatusElemente().get(rs.getInt("KontaktAnredeStatus"))
                    teilnehmerListe.get(i).setTeilnahmeStatus(teilnahme.getStatusElemente().get(rs.getInt("TeilnahmeStatus")));
                }
                i++;

            }
            return teilnehmerListe;
        } catch (SQLException e) {
            System.out.println("Anzahl Termine konnte nicht ermittelt werden ("+ e + ")");

        } finally {
            return teilnehmerListe;
        }

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

    /**
     * Zählt die Anzahl Termine im laufenden Jahr
     * @return
     */
    public static Timestamp readLetzteAenderung() {
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT MAX(KontaktTrackChangeTimestamp) AS LetzteAenderung FROM usr_web116_5.kontakt";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                //System.out.println("letzte Aenderung: " + rs.getString("LetzteAenderung"));
                return Timestamp.valueOf(rs.getString("LetzteAenderung"));
            }
        } catch (SQLException e) {
            System.out.println("letzte Änderung konnte nicht ermittelt werden ("+ e + ")");

        }
        return null;
    }

    public static ArrayList<Mitglied> getMitgliederAsArrayList() {
        Status anredeStatus = new Status(1);
        Status kategorieIStatus = new Status(2);
        Status kategorieIIStatus = new Status(4);
        ArrayList<Mitglied> mitgliederListe = new ArrayList<>();
        ArrayList<Kontakt> kontaktListe = new ArrayList<>();
        LocalDate heute = LocalDate.now();
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.kontakt ORDER BY KontaktNachname, KontaktVorname";
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
                kontakt.setLetzteAenderungUser(rs.getString("KontaktTrackChangeUsr"));
                kontakt.setLetzteAenderungTimestamp(rs.getTimestamp("KontaktTrackChangeTimestamp"));

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
                    ((Mitglied) kontakt).setKategorieIStatus(kategorieIStatus.getStatusElemente().get(rs.getInt("KontaktKategorieA")));
                    ((Mitglied) kontakt).setKategorieIIStatus(kategorieIIStatus.getStatusElemente().get(rs.getInt("KontaktKategorieB")));
                    ((Mitglied) kontakt).setIstVorstandsmitglied(rs.getBoolean("KontaktIstVorstandsmitglied"));
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

    public static ArrayList<Termin> getKommendeTermineAsArrayList(){
        ArrayList<Termin> terminListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.termin WHERE TerminDatum >= CURRENT_DATE() ORDER BY TerminDatum ASC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Termin termin;
                LocalDateTime terminZeit = null;
                LocalDateTime terminZeitBis = null;
                int terminId = rs.getInt("TerminId");
                LocalDate terminDatum = Date.valueOf(rs.getString("TerminDatum")).toLocalDate();

                String terminText = rs.getString("TerminText");

                /**
                 * Objekte werden erzeugt und der Terminliste hinzugefügt
                 */
                termin = new Termin(terminId, terminDatum, terminText);
                if(rs.getString("TerminZeit") != null) {
                    terminZeit = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeit")).toLocalTime());
                    termin.setZeit(terminZeit);
                }
                if(rs.getString("TerminZeitBis") != null) {
                    terminZeitBis = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeitBis")).toLocalTime());
                    termin.setZeitBis(terminZeitBis);
                }
                System.out.println("****Local: " + terminZeit);
                System.out.println("****LocalBis: " + terminZeitBis);

                terminListe.add(termin);

                System.out.println("Termine erstellt");

            }

        } catch (SQLException e) {
            System.out.println("Termine konnten nicht erzeugt werden");

        }
        return terminListe;
    }

    public static ArrayList<Termin> getTermineAsArrayList(){
        ArrayList<Termin> terminListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT * from usr_web116_5.termin ORDER BY TerminDatum ASC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Termin termin;
                LocalDateTime terminZeit = null;
                LocalDateTime terminZeitBis = null;
                int terminId = rs.getInt("TerminId");
                LocalDate terminDatum = Date.valueOf(rs.getString("TerminDatum")).toLocalDate();

                String terminText = rs.getString("TerminText");

                /**
                 * Objekte werden erzeugt und der Terminliste hinzugefügt
                 */
                termin = new Termin(terminId, terminDatum, terminText);
                if(rs.getString("TerminZeit") != null) {
                    terminZeit = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeit")).toLocalTime());
                    termin.setZeit(terminZeit);
                }
                if(rs.getString("TerminZeitBis") != null) {
                    terminZeitBis = LocalDateTime.of(terminDatum, Time.valueOf(rs.getString("TerminZeitBis")).toLocalTime());
                    termin.setZeitBis(terminZeitBis);
                }
                if(rs.getString("TerminOrt") != null){
                    termin.setOrt(rs.getString("TerminOrt"));
                }
                if(rs.getString("TerminDetails") != null){
                    termin.setDetails(rs.getString("TerminDetails"));
                }
                if(rs.getString("TerminTrackChangeUsr") != null){
                    termin.setTrackChangeUsr(rs.getString("TerminTrackChangeUsr"));
                }
                if(rs.getString("TerminTrackChangeTimestamp") != null) {
                    termin.setTrackChangeTimestamp(rs.getTimestamp("TerminTrackChangeTimestamp"));
                }

      /*          if(rs.getString("TerminTeilnehmerKatA") != null){
                    termin.setTeilnehmerKatI();
                }
                termin.setTeilnehmerKatI();
                termin.setTeilnehmerKatII();*/



                terminListe.add(termin);
                System.out.println("Termine erstellt");
            }

        } catch (SQLException e) {
            System.out.println("Termine konnten nicht erzeugt werden");
        }
        return terminListe;
    }

    /**
     * Ermittelt anhand der Geburtstage und der Eintrittsdaten pro Mitglied
     * das nächste Geburtsdatum und das nächste Jubiläum
     * @return
     */
    public static ArrayList<Jubilaeum> getJubilaeenAsArrayList() {
        int jahr = Year.now().getValue();
        System.out.println("Jahr jetzt:  " + jahr);
        // Geburtstage
        ArrayList<Jubilaeum> jubilaeumsListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT KontaktId, KontaktGeburtsdatum, KontaktNachname, KontaktVorname FROM usr_web116_5.kontakt WHERE KontaktIstMitglied = 1 AND KontaktGeburtsdatum IS NOT NULL AND KontaktGeburtsdatum NOT LIKE '0000-%'";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String geburtsDatum = rs.getString("KontaktGeburtsdatum");
                if(geburtsDatum != null) {
                    int geburtsJahr = Integer.parseInt(geburtsDatum.substring(0, 4));
                    // alter in diesem Jahr
                    int alter = jahr - geburtsJahr;
                    geburtsDatum = jahr + geburtsDatum.substring(4, 10);
                    System.out.println("GebdiesesJahr: " + geburtsDatum);
                    ;
                    LocalDate geburtsDatumLD = Date.valueOf(geburtsDatum).toLocalDate();
                    // Wenn der nächste Geburtstag grösser ist als heute

                    if (geburtsDatumLD.isAfter(LocalDate.now().minusDays(1))) {
                        jubilaeumsListe.add(new Jubilaeum(999, geburtsDatumLD, alter + ". Geburtstag von " + rs.getString("KontaktVorname") + " " + rs.getString("KontaktNachname")));
                    } else {
                        // nächster Geburtstag ist erst im nächsten Jahr
                        jubilaeumsListe.add(new Jubilaeum(999, geburtsDatumLD.plusYears(1), (alter + 1) + ". Geburtstag von " + rs.getString("KontaktVorname") + " " + rs.getString("KontaktNachname")));
                    }
                }
            }

            // Vereinsmitgliedschafts-Jubiläen ermittelnt
            query = "SELECT KontaktId, KontaktEintrittsdatum, KontaktNachname, KontaktVorname FROM usr_web116_5.kontakt WHERE KontaktIstMitglied = 1 AND KontaktGeburtsdatum IS NOT NULL AND KontaktGeburtsdatum NOT LIKE '0000-%'";
            rs = st.executeQuery(query);
            int i = 20000;
            while (rs.next()) {
                String eintrittsDatum = rs.getString("KontaktEintrittsdatum");
                int eintrittsJahr = Integer.parseInt(eintrittsDatum.substring(0,4));
                // alter in diesem Jahr
                int anzahlJahre = jahr - eintrittsJahr;
                eintrittsDatum = jahr + eintrittsDatum.substring(4,10);
                System.out.println("JubdiesesJahr: " + eintrittsDatum);

                LocalDate eintrittsDatumLD = Date.valueOf(eintrittsDatum).toLocalDate();
                // Wenn der nächste Geburtstag grösser ist als heute

                if(eintrittsDatumLD.isAfter(LocalDate.now().minusDays(1))) {
                    jubilaeumsListe.add(new Jubilaeum(i, eintrittsDatumLD, "Jubiläum: " + anzahlJahre + " Jahr(e) " + rs.getString("KontaktVorname")+ " " + rs.getString("KontaktNachname")));
                } else {
                    // nächster Geburtstag ist erst im nächsten Jahr
                    jubilaeumsListe.add(new Jubilaeum(i, eintrittsDatumLD.plusYears(1), "Jubiläum: " + (anzahlJahre + 1 ) + " Jahr(e) " + rs.getString("KontaktVorname")+ " " + rs.getString("KontaktNachname")));
                }
                i++;
            }




        } catch(SQLException e){
            System.out.println("nächste Geburtstage konnten nicht ermittelt werden");
        }

        //Liste sortieren nach Datum...
        Collections.sort(jubilaeumsListe, (a, b)->a.getDatum().compareTo(b.getDatum()));
        return jubilaeumsListe;
    }
}
