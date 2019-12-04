package space.cloud4b.verein.daten.mysql.service;

import space.cloud4b.verein.services.connection.MysqlConnection;
import space.cloud4b.verein.model.verein.Verein;
import space.cloud4b.verein.model.verein.adressbuch.AdressBuch;
import space.cloud4b.verein.model.verein.adressbuch.Kontakt;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Position;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

public abstract class DatenLieferant {

    /**
     * Ermittelt verschiedene Daten zum übergebenen Status-Objekt und
     * ergänzt die entsprechenden Instanzvariabeln
     * @param status
     */
    public static void statusInfosSetzen(Status status) {
        try (Connection conn = new MysqlConnection().getConnection();
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
        }
    }

    /**
     * Ermittelt die Status-Elemente zur übergebenen Status-ID und
     * instanziert die einzelnen Status-Elemente und fügt diese zur
     * HashMap hinzu und gibt diese zurück zum Status-Objekt
     * @param statusId eindeutige ID-Nummer des Status-Objekts
     * @return HashMap mit den einzelnen Status-Elementen
     */
    public static HashMap<Integer, StatusElement> statusHashMapLaden(int statusId) {
        HashMap<Integer, StatusElement> statusHashMap = new HashMap<>();
//TODO Statuselemente fertig machen, wenn DB bereit ist
        try (Connection conn = new MysqlConnection().getConnection();
             Statement st = conn.createStatement()) {
            String query = "SELECT status.StatusId AS StatusId, StatusNameLong, StatusNameShort, StatusElementKey, "
                                   + "StatusElementNameLong, StatusElementNameShort, StatusElementUnicodeChar FROM usr_web116_5.status LEFT JOIN usr_web116_5.statusElement ON "
                                    + "status.StatusId = statusElement.StatusId WHERE status.statusId="
                                    + statusId
                                   + " ORDER BY `statusElement`.`StatusElementKey` ASC;";
            System.out.println("SQLStatus: " + query);

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                statusHashMap.put(rs.getInt("StatusElementKey"), new StatusElement(rs.getInt("StatusElementKey"),
                        rs.getString("StatusElementNameLong"),rs.getString("StatusElementNameShort"), rs.getString("StatusElementUnicodeChar")));
            }
        } catch(SQLException e){
            System.out.println("Status konnte nicht erzeugt werden");
        }
        return statusHashMap;
    }

    public static ArrayList<Mitglied> mitgliederLaden(AdressBuch adressBuch) {
        Status anredeStatus = adressBuch.getAnredeStatus();
        Status kategorieIStatus = adressBuch.getKategorieIStatus();
        Status kategorieIIStatus = adressBuch.getKategorieIIStatus();
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
                    ((Mitglied) kontakt).setKategorieIStatus(kategorieIStatus.getStatusElemente().get(rs.getInt("KontaktKategorieA")));
                    ((Mitglied) kontakt).setKategorieIIStatus(kategorieIIStatus.getStatusElemente().get(rs.getInt("KontaktKategorieB")));
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

    public static ArrayList<Termin> termineLaden(){
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

    public static ArrayList<Jubilaeum> jubilaenLaden() {
        int jahr = Year.now().getValue();
        System.out.println("Jahr jetzt:  " + jahr);
        // Geburtstage
        ArrayList<Jubilaeum> jubilaeumsListe = new ArrayList<>();
        try (Connection conn = new MysqlConnection().getConnection(); Statement st = conn.createStatement()) {
            String query = "SELECT KontaktId, KontaktGeburtsdatum, KontaktNachname, KontaktVorname FROM usr_web116_5.kontakt WHERE KontaktIstMitglied = 1 AND KontaktGeburtsdatum IS NOT NULL AND KontaktGeburtsdatum NOT LIKE '0000-%'";

            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String geburtsDatum = rs.getString("KontaktGeburtsdatum");
                int geburtsJahr = Integer.parseInt(geburtsDatum.substring(0,4));
                // alter in diesem Jahr
                int alter = jahr - geburtsJahr;
                geburtsDatum = jahr + geburtsDatum.substring(4,10);
                System.out.println("GebdiesesJahr: " + geburtsDatum);
                 ;
                LocalDate geburtsDatumLD = Date.valueOf(geburtsDatum).toLocalDate();
                // Wenn der nächste Geburtstag grösser ist als heute

                if(geburtsDatumLD.isAfter(LocalDate.now().minusDays(1))) {
                    jubilaeumsListe.add(new Jubilaeum(999, geburtsDatumLD, alter + ". Geburtstag von " + rs.getString("KontaktVorname")+ " " + rs.getString("KontaktNachname")));
                } else {
                    // nächster Geburtstag ist erst im nächsten Jahr
                    jubilaeumsListe.add(new Jubilaeum(999, geburtsDatumLD.plusYears(1), (alter + 1 ) + ". Geburtstag von " + rs.getString("KontaktVorname")+ " " + rs.getString("KontaktNachname")));
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
        Collections.sort(jubilaeumsListe, (a,b)->a.getDatum().compareTo(b.getDatum()));
        return jubilaeumsListe;
    }

    public static ArrayList<Position> fuelleRangliste(Verein verein) {
        ArrayList<Position> rangliste = new ArrayList<>();
        ArrayList<Mitglied> mitgliederListe = verein.getAdressBuch().getMitgliederListeAsArrayList();
        ArrayList<Termin> terminListe = verein.getKalender().getTerminListeAsArrayList();

        Iterator<Mitglied> i = mitgliederListe.iterator();
        System.out.println("Mitglieder werden durchlaufen");
        while (i.hasNext()) {
            String query;
            int anzTermineAktuellesJahr = 0;
            int anzAnwesenheiten = 0;
            double anwesenheitenAnteil = 0; //Anteil in Prozent
            // Fokus 1 = dieses Jahr
            Mitglied mitglied = i.next();
           // System.out.println(i.next());


            try (Connection conn = new MysqlConnection().getConnection(); Connection conn2 = new MysqlConnection().getConnection(); Statement st = conn.createStatement(); Statement st2 = conn2.createStatement()) {
                query = "SELECT COUNT(*) AS anzahlTermine FROM `termin` WHERE YEAR(TerminDatum) " +
                        "= YEAR(CURRENT_DATE) AND `TerminDatum` >= '" + mitglied.getEintrittsDatumAsISOString() + "'";
               // System.out.println(query);

                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    anzTermineAktuellesJahr = rs.getInt("anzahlTermine");
                    System.out.println("Termine 2019 für mitglied " + mitglied + " = " + rs.getInt("anzahlTermine"));
                    //rangliste.add(new Position(rs.getString("KontaktNachname");))
                }

                // Anzahl der Anwesenheiten ermitteln für dieses Mitglied
                query = "SELECT COUNT(*) AS anzAnwesenheiten FROM `terminkontrolle` LEFT JOIN kontakt " +
                        "ON kontakt.KontaktId = `KontrolleMitgliedId` LEFT JOIN termin ON termin.TerminId " +
                        "= `KontrolleTerminId`WHERE `KontrolleMitgliedId`=" + mitglied.getId() +
                        " AND `KontrolleArt`='Anwesenheit' " +
                        "AND KontrolleWert = 1 AND YEAR(termin.TerminDatum) = YEAR(CURRENT_DATE)";
               // System.out.println(query);

                ResultSet rs2 = st2.executeQuery(query);
                while (rs2.next()) {
                    anzAnwesenheiten = rs2.getInt("anzAnwesenheiten");
                   // System.out.println("Anwesenheiten:  = " + rs2.getInt("anzAnwesenheiten"));
                }
                anwesenheitenAnteil = (anzAnwesenheiten * 100 / anzTermineAktuellesJahr);
                System.out.println("Anteil: " + anwesenheitenAnteil);


                rangliste.add(new Position(verein.getRangliste(), mitglied, mitglied.getKurzbezeichnung(), anzTermineAktuellesJahr, anzAnwesenheiten, anwesenheitenAnteil));

                // rangliste sortieren nach Anteilen...
                //Collections.sort(rangliste, (a,b)->a.getAnwesenheitsAnteil().compareTo(b.getAnwesenheitsAnteil()));


            } catch (SQLException e) {
                System.out.println("..da hat etwas nicht funktioniert");
            }
        }

        // Rangliste sortieren, absteigend nach Anwesenheitsanteil
        rangliste.sort(Comparator.comparingDouble(Position::getAnwesenheitsAnteil).reversed());
        System.out.println("toll" + rangliste.size());

        // Ränge setzen
        Iterator<Position> p = rangliste.iterator();
        int rangZaehler = 0;
        double anteilVorgaenger = 0;
        while (p.hasNext()) {
            Position position = p.next();

            // behandle die erste Position in der Rangliste
            if (rangZaehler == 0) {
                position.setRangYTD(++rangZaehler);
            } else {
                if(position.getAnwesenheitsAnteil()<anteilVorgaenger){
                    rangZaehler++;
                }
                position.setRangYTD(rangZaehler);
            }
            anteilVorgaenger = position.getAnwesenheitsAnteil();
        }

        return rangliste;

    }
}
