package space.cloud4b.verein.view.dashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Jubilaeum;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Position;
import space.cloud4b.verein.model.verein.kontrolle.rangliste.Rangliste;
import space.cloud4b.verein.model.verein.status.StatusElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DashBoardController {

    private MainApp mainApp;
    private GridPane gridPane;

    @FXML
    private TableView<Mitglied> mitgliederTabelle;
    @FXML
    private TableColumn<Mitglied, Number> idSpalte;
    @FXML
    private TableColumn<Mitglied, String> nachNameSpalte;
    @FXML
    private TableColumn<Mitglied, String> vorNameSpalte;
    @FXML
    private TableColumn<Mitglied, StatusElement> anredeSpalte;
    @FXML
    private TableColumn<Mitglied, String> ortSpalte;

    @FXML
    private TableView<Termin> termineTabelle;
    @FXML
    private TableColumn<Termin, String> terminDatumSpalte;
    @FXML
    private TableColumn<Termin, String> terminZeitSpalte;
    @FXML
    private TableColumn<Termin, String> terminTextSpalte;

    @FXML
    private TableView<Jubilaeum> jubilaeumTabelle;
    @FXML
    private TableColumn<Jubilaeum, LocalDate> jubilaeumDatumSpalte;
    @FXML
    private TableColumn<Jubilaeum, String> jubilaeumDatumStringSpalte;
    @FXML
    private TableColumn<Jubilaeum, String> jubilaeumTextSpalte;

    @FXML
    private TableView<Position> ranglisteTabelle;
    @FXML
    private TableColumn<Position, Number> rangSpalte;
    @FXML
    private TableColumn<Position, String> mitgliedSpalte;
    @FXML
    private TableColumn<Position, Number> anzTermineSpalte;
    @FXML
    private TableColumn<Position, Number> anzAnwesenheiten;
    @FXML
    private TableColumn<Position, Number> anwesenheitsAnteilSpalte;


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        mitgliederTabelle.setItems(this.mainApp.getVerein().getAdressBuch().getMitgliederListe());
        termineTabelle.setItems(this.mainApp.getVerein().getKalender().getNaechsteTerminListe());
        jubilaeumTabelle.setItems(this.mainApp.getVerein().getKalender().getJubilaeumsListe());
        ranglisteTabelle.setItems(this.mainApp.getVerein().getRangliste().getRanglistenListe());
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Mitgliederliste initialisieren
        idSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getIdProperty());
        vorNameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getVornameProperty());
        nachNameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getNachnameProperty());
        anredeSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnredeProperty());
        ortSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getOrtProperty());


        // Terminliste initialisieren
        terminDatumSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getDateAsLocalStringMedium());
       terminZeitSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getZeitTextProperty());
        terminTextSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getTextProperty());


        // Jubiläumsliste initialisieren
      //  jubilaeumDatumSpalte.setCellValueFactory(
           //     cellData -> cellData.getValue().getDatumProperty());
        jubilaeumTextSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getTextProperty());
        jubilaeumDatumStringSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getDateAsNiceString());

        // Rangliste initialisieren
        rangSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getRangProperty());
        mitgliedSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getKurzbezeichnungProperty());
        anzTermineSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnzahlTermineProperty());
        anzAnwesenheiten.setCellValueFactory(
                cellData -> cellData.getValue().getAnzahlAnwesenheitenProperty());
        anwesenheitsAnteilSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnwesenheitsAnteilProperty());

    }
}
