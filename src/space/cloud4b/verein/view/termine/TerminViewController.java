package space.cloud4b.verein.view.termine;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.model.verein.kalender.Teilnehmer;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.DatabaseReader;
import space.cloud4b.verein.view.mainframe.MainFrameController;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private Hyperlink doodleHyperlink = new Hyperlink("Anwesenheiten bearbeiten");
    @FXML
    private Label angemeldetLabel = new Label();
    @FXML
    private Label vielleichtLabel = new Label();
    @FXML
    private Label neinLabel = new Label();
    @FXML
    private Label anzMitgliederLabel = new Label();

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
        stundenVonFeld.setTextFormatter(new TextFormatter<Integer>(change -> {
            // Deletion should always be possible.
            if (change.isDeleted()) {
                return change;
            }

            // How would the text look like after the change?
            String txt = change.getControlNewText();

            // Try parsing and check if the result is in [0, 64].
            try {
                int n = Integer.parseInt(txt);
                return 0 <= n && n <= 23 ? change : null;
            } catch (NumberFormatException e) {
                return null;
            }
        }));
        minutenVonSlider.valueProperty().addListener((obs, oldval, newVal) ->
                minutenVonSlider.setValue(Math.round(newVal.doubleValue())));
        minutenVonFeld.textProperty()
                .bindBidirectional(minutenVonSlider.valueProperty(), new NumberStringConverter());
        minutenVonFeld.setTextFormatter(new TextFormatter<Integer>(change -> {
            // Deletion should always be possible.
            if (change.isDeleted()) {
                return change;
            }

            // How would the text look like after the change?
            String txt = change.getControlNewText();

            // Try parsing and check if the result is in [0, 64].
            try {
                int n = Integer.parseInt(txt);
                return 0 <= n && n <= 59 ? change : null;
            } catch (NumberFormatException e) {
                return null;
            }
        }));

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
        showTeilnehmerListe(terminListe.get(indexClosestToNow));

        // Teilnehmerkategorien
        Status kategorieI = new Status(2);
        Status kategorieII = new Status(4);
        comboBoxKategorieI.getItems().addAll(kategorieI.getElementsAsArrayList());
        comboBoxKategorieII.getItems().addAll(kategorieII.getElementsAsArrayList());

    }
    // Hyperlink zum Doodle-Formular
    @FXML
    public void handleLinkToDoodle() {
            mainApp.getMainController().showDoodle();
    }
    public void terminAuswahlComboBoxAction() {
        setTermin(terminAuswahlComboBox.getValue());
        showTeilnehmerListe(terminAuswahlComboBox.getValue());
    }
    public void setTermin(Termin termin){
        this.termin = termin;
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


        if(termin.getKatIElement()!=null) {
            comboBoxKategorieI.getSelectionModel().select(termin.getKatIElement());
        }
        if(termin.getKatIIElement()!=null) {
            comboBoxKategorieII.getSelectionModel().select(termin.getKatIIElement());
        }


        // Tab Kontrolle/Planung
        angemeldetLabel.setText(Integer.toString(DatabaseReader.getAnzAnmeldungen(termin)));
        vielleichtLabel.setText(Integer.toString(DatabaseReader.getAnzVielleicht(termin)));
        neinLabel.setText(Integer.toString(DatabaseReader.getAnzNein(termin)));
        anzMitgliederLabel.setText(Integer.toString(DatabaseReader.readAnzahlMitglieder()));
     //   comboBoxKategorieI.getSelectionModel().select(termin.getKatIElement().getStatusElementKey());
    }

    public void showTeilnehmerListe(Termin termin) {
        teilnehmerTabelle.setItems(FXCollections.observableArrayList(DatabaseReader.getTeilnehmer(termin)));
        mitgliedSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getMitglied().getNachnameProperty());
        anmeldeStatusSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getAnmeldungProperty());

       teilnehmerTabelle.setRowFactory(row -> new TableRow<Teilnehmer>() {
            @Override
            public void updateItem(Teilnehmer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getAnmeldungWert() == 1) {
                    setStyle("-fx-background-color: rgba(0,251,0,0.17);");
                } else if (item.getAnmeldungWert() == 2) {
                    setStyle("-fx-background-color: rgba(255,0,0,0.15);");
                } else if (item.getAnmeldungWert() == 3) {
                    setStyle("-fx-background-color: rgba(255,165,0,0.2);");
                }
            }
        });
    }

    /**
     * neuen Termin erfassen
     */
    public void handleErfassenButton() {
        mainApp.showTerminErfassen();
    }

    /**
     * Den aktuellen Termin neu laden, wenn der Reset-Button betätigt wird.
     */
    public void handleResetButton() {
        setTermin(termin);
    }

    /**
     * überprüft die Daten und speichert sie
     */
    public void handleSpeichernButton() {
        if (isInputValid()) {
           // unsavedChanges = false;
            System.out.println(terminDatumPicker.getValue());
            System.out.println(termin);
            System.out.println("Stunde: " + stundenVonFeld.getText());
            System.out.println("Minute: " + minutenVonFeld.getText());
            termin.setDatum(Date.valueOf(terminDatumPicker.getValue()).toLocalDate());

            if(stundenVonFeld.getText().length() > 0 && minutenVonFeld.getText().length() > 0) {
                termin.setZeit(LocalDateTime.of(terminDatumPicker.getValue(),
                        LocalTime.of(Integer.parseInt(stundenVonFeld.getText()),
                                Integer.parseInt(minutenVonFeld.getText()))));
            } else {
                termin.setZeit(null);
            }

            if(stundenBisFeld.getText().length() > 0 && minutenBisFeld.getText().length() > 0) {
                termin.setZeitBis(LocalDateTime.of(terminDatumPicker.getValue(),
                        LocalTime.of(Integer.parseInt(stundenBisFeld.getText()),
                                Integer.parseInt(minutenBisFeld.getText()))));
            } else {
                termin.setZeitBis(null);
            }
            termin.setTerminText(terminText.getText());
            termin.setTeilnehmerKatI(comboBoxKategorieI.getValue());
            termin.setTeilnehmerKatII(comboBoxKategorieII.getValue());
            termin.setOrt(terminOrt.getText());
            termin.setDetails(terminDetails.getText());
            termin.setTeilnehmerKatI(comboBoxKategorieI.getValue());
            termin.setTeilnehmerKatII(comboBoxKategorieII.getValue());
            termin.setTrackChangeTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            termin.setTrackChangeUsr(System.getProperty("user.name"));
            DatabaseOperation.updateTermin(termin);
            letzteAenderungLabel.setText(termin.getLetzteAenderung());
            terminAuswahlComboBox.getItems().addAll(kalenderController.getTermineAsArrayList());
            //terminAuswahlComboBox.getSelectionModel().clearAndSelect(termin);
            terminAuswahlComboBox.getSelectionModel().select(termin);
            mainFrameController.setInfo("Änderungen gespeichert", "OK", true);
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMeldung = "";
        Boolean isValid = true;

        if (terminDatumPicker == null) {
            errorMeldung += "Datumsangabe ist ungültig!";
            isValid = false;
        }
        if (terminText.getText() == null || terminText.getText().length() == 0) {
            errorMeldung += "Text ungültig!";
            isValid = false;
        }

        if(!isValid && errorMeldung.length()>0) {
            mainFrameController.setInfo(errorMeldung, "NOK", true);
        }
        return isValid;

    }



}
