package space.cloud4b.verein.view.termine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.AdressController;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.kalender.Teilnehmer;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.view.mainframe.MainFrameController;


import javax.xml.soap.Text;
import java.time.LocalDate;
import java.util.ArrayList;

public class TerminViewController {

    @FXML
    private ComboBox<Termin> terminAuswahlComboBox = new ComboBox<>();
    @FXML
    private Label letzteAenderungLabel;
    @FXML
    private TextField terminText;
    @FXML
    private DatePicker terminDatumPicker;
    @FXML
    private Slider stundenVonSlider = new Slider(0, 23, 12);
    @FXML
    private Slider minutenVonSlider = new Slider(0,59,30);
    @FXML
    private TextField stundenVonFeld = new TextField();
    @FXML
    private TextField minutenVonFeld = new TextField();
    @FXML
    private Slider stundenBisSlider = new Slider(0, 23, 12);
    @FXML
    private Slider minutenBisSlider = new Slider(0,59,30);
    @FXML
    private TextField stundenBisFeld = new TextField();
    @FXML
    private TextField minutenBisFeld = new TextField();
    @FXML
    private ComboBox<StatusElement> comboBoxKategorieI = new ComboBox<StatusElement>();
    @FXML
    private ComboBox<StatusElement> comboBoxKategorieII = new ComboBox<StatusElement>();
    @FXML
    private TextField terminOrt = new TextField();
    @FXML
    private TextArea terminDetails = new TextArea();
    @FXML
    private TableView<Teilnehmer> teilnehmerTabelle;
    @FXML
    private TableColumn<Teilnehmer, String> mitgliedSpalte;
    @FXML
    private TableColumn<Teilnehmer, StatusElement> anmeldeStatusSpalte;
    @FXML
    private TableColumn<Teilnehmer, StatusElement> anwesenheitStatusSpalte;


    private Stage dialogStage;
    private MainFrameController mainFrameController;
    private KalenderController kalenderController = new KalenderController();
    private MainApp mainApp;
    private ArrayList<Termin> terminListe = new ArrayList<>();
    private Termin termin = null;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
     /*   this.kalenderController = new KalenderController(this);
        this.kalenderController.Attach(this);*/


    }
    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {
        int indexClosestToNow = -1;
        int i = 0;
        this.terminListe = kalenderController.getTermineAsArrayList();
        while (indexClosestToNow < 0 && terminListe.size() > i) {
            // wenn heute, dann dieses, sonst das nächste after
            if(terminListe.get(i).getDatum().isEqual(LocalDate.now())){
                indexClosestToNow = i;
            };
            if(terminListe.get(i).getDatum().isAfter(LocalDate.now())) {
                indexClosestToNow = i;
            };
            i++;
        }
        terminAuswahlComboBox.getItems().addAll(terminListe);
        terminAuswahlComboBox.getSelectionModel().select(indexClosestToNow);

        // TODO Tooltip ergänzen
        // Zeitangabe von/am
        stundenVonSlider.setTooltip(new Tooltip("Hallo"));
        stundenVonSlider.valueProperty().addListener((obs, oldval, newVal) ->
                stundenVonSlider.setValue(Math.round(newVal.doubleValue())));
        stundenVonFeld.textProperty()
                .bindBidirectional(stundenVonSlider.valueProperty(), new NumberStringConverter());
        minutenVonSlider.valueProperty().addListener((obs, oldval, newVal) ->
                minutenVonSlider.setValue(Math.round(newVal.doubleValue())));
        minutenVonFeld.textProperty()
                .bindBidirectional(minutenVonSlider.valueProperty(), new NumberStringConverter());

        // Zeitangabe bis
        stundenBisSlider.setTooltip(new Tooltip("Hallo"));
        stundenBisSlider.valueProperty().addListener((obs, oldval, newVal) ->
                stundenBisSlider.setValue(Math.round(newVal.doubleValue())));
        stundenBisFeld.textProperty()
                .bindBidirectional(stundenBisSlider.valueProperty(), new NumberStringConverter());
        minutenBisSlider.valueProperty().addListener((obs, oldval, newVal) ->
                minutenBisSlider.setValue(Math.round(newVal.doubleValue())));
        minutenBisFeld.textProperty()
                .bindBidirectional(minutenBisSlider.valueProperty(), new NumberStringConverter());
        setTermin(terminListe.get(indexClosestToNow));

        // Teilnehmerkategorien
        Status kategorieI = new Status(2);
        Status kategorieII = new Status(4);
        comboBoxKategorieI.getItems().addAll(kategorieI.getElementsAsArrayList());
        comboBoxKategorieII.getItems().addAll(kategorieII.getElementsAsArrayList());

    }

    public void terminAuswahlComboBoxAction() {
        setTermin(terminAuswahlComboBox.getValue());
        showTeilnehmerListe(terminAuswahlComboBox.getValue());
    }
    public void setTermin(Termin termin){
        letzteAenderungLabel.setText(termin.getLetzteAenderung());
        terminDatumPicker.setValue(termin.getDatum());
        terminText.setText(termin.getTextProperty().getValue());
        terminOrt.setText(termin.getOrtProperty().getValue());
        terminDetails.setText(termin.getDetailsProperty().getValue());
        if(termin.getTerminZeitVon() != null) {
            stundenVonFeld.setText(Integer.toString(termin.getTerminZeitVon().getHour()));
            minutenVonFeld.setText(Integer.toString(termin.getTerminZeitVon().getMinute()));
        } else {
            stundenVonFeld.clear();
            minutenVonFeld.clear();
        }
        if(termin.getTerminZeitBis() != null) {
            stundenBisFeld.setText(Integer.toString(termin.getTerminZeitBis().getHour()));
            minutenBisFeld.setText(Integer.toString(termin.getTerminZeitBis().getMinute()));
        } else {
            stundenBisFeld.clear();
            minutenBisFeld.clear();
        }
     //   comboBoxKategorieI.getSelectionModel().select(termin.getKatIElement().getStatusElementKey());
    }

    public void showTeilnehmerListe(Termin termin) {
        teilnehmerTabelle.setItems(FXCollections.observableArrayList(DatabaseReader.getTeilnehmer(termin)));
        mitgliedSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getMitglied().getNachnameProperty());
        anmeldeStatusSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnmeldungProperty());
        anwesenheitStatusSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getTeilnahmeProperty());
    }

}
