package space.cloud4b.verein.view.mitglieder;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.AdressController;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.daten.mysql.service.DatenManipulator;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.model.verein.status.Status;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.services.DatabaseOperation;
import space.cloud4b.verein.services.Observer;
import space.cloud4b.verein.view.mainframe.MainFrameController;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.soap.Text;
import java.io.*;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class MitgliedViewController implements Observer {

    private ArrayList<Observer> observerList;
    AdressController adressController;

    @FXML
    public ComboBox<StatusElement> comboBoxAnrede = new ComboBox<StatusElement>();
    @FXML
    public ComboBox<StatusElement> comboBoxKategorieI = new ComboBox<StatusElement>();
    @FXML
    public ComboBox<StatusElement> comboBoxKategorieII = new ComboBox<StatusElement>();
    @FXML
    private TableView<Mitglied> mitgliedTabelle;
    @FXML
    private TableColumn<Mitglied, Number> idSpalte;
    @FXML
    private TableColumn<Mitglied, String> vornameSpalte;
    @FXML
    private TableColumn<Mitglied, String> nachnameSpalte;
    @FXML
    private Label idLabel;
    @FXML
    private Label letzteAenderungLabel;
    @FXML
    private TextField nachNameFeld;
    @FXML
    private TextField vorNameFeld;
    @FXML
    private TextField adresseFeld;
    @FXML
    private TextField adressZusatzFeld;
    @FXML
    private TextField plzFeld;
    @FXML
    private TextField ortFeld;
    @FXML
    private TextArea bemerkungsFeld;
    @FXML
    private TextField mobileFeld;
    @FXML
    private TextField telefonFeld;
    @FXML
    private TextField eMailFeld;
    @FXML
    private TextField eMailIIFeld;
    @FXML
    private DatePicker geburtsdatumPicker;
    @FXML
    private Label alterLabel;
    @FXML
    private DatePicker eintrittsDatumPicker;
    @FXML
    private DatePicker austrittsDatumPicker;
    @FXML
    private Label mitgliedSeitLabel;
    @FXML
    private ImageView profilBild;
    @FXML
    private CheckBox istVorstandsmitgliedCheckBox;
    @FXML
    private Button nextMitgliedButton;


    private Stage dialogStage;
    private MainFrameController mainFrameController;
    private boolean okClicked = false; // TODO braucht es das?
    private MainApp mainApp;
    private Mitglied aktuellesMitglied = null;
    private ArrayList<Mitglied> mitgliedArrayList;

    public MitgliedViewController() {
        //this.aktuellesMitglied = mainApp.getVerein().getAdressBuch().getMitgliederListeAsArrayList().get(0);
    }

    /**
     * Initialisierung der controller class
     */
    @FXML
    private void initialize() {

        idSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getIdProperty());
        vornameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getVornameProperty());
        nachnameSpalte.setCellValueFactory(
                cellData -> cellData.getValue().getNachnameProperty());
        // Listen for selection changes and show the person details when changed.
        mitgliedTabelle.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setMitglied(newValue));

        Status anrede = new Status(1);
        comboBoxAnrede.getItems().addAll(anrede.getElementsAsArrayList());
        Status kategorieI = new Status(2);
        comboBoxKategorieI.getItems().addAll(kategorieI.getElementsAsArrayList());
        Status kategorieII = new Status(4);
        comboBoxKategorieII.getItems().addAll(kategorieII.getElementsAsArrayList());
    }

    public boolean isOkClicked() {
        return this.okClicked;
    }

    /**
     * Sets the stage of this dialog
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void handleGetNextMitglied() {
        int i = mitgliedArrayList.indexOf(aktuellesMitglied) + 1;
        if (i < mitgliedArrayList.size()) {
            setMitglied(mitgliedArrayList.get(i));

        } else {
            mainFrameController.setInfo("Blättern nicht möglich..", "NOK");
        }
    }

    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

    /**
     * Setzt das zu editierende Mitglied in den Dialog
     *
     * @param mitglied
     */
    public void setMitglied(Mitglied mitglied) {
        if(mitglied == null){
            mitglied = mitgliedArrayList.get(0);
        }
        this.aktuellesMitglied = mitglied;
        idLabel.setText(("Details zu Mitglied #" + mitglied.getId()));
        letzteAenderungLabel.setText(mitglied.getLetzteAenderung());
        comboBoxAnrede.getSelectionModel().select(mitglied.getAnredeElement().getStatusElementKey());
        nachNameFeld.setText(mitglied.getNachName());
        vorNameFeld.setText(mitglied.getVorname());
        adresseFeld.setText(mitglied.getAdresse());
        adressZusatzFeld.setText(mitglied.getAdresszusatz());
        plzFeld.setText(Integer.toString(mitglied.getPlz()));
        ortFeld.setText(mitglied.getOrt());
        bemerkungsFeld.setText(mitglied.getBemerkungen());
        mobileFeld.setText(mitglied.getMobile());
        telefonFeld.setText(mitglied.getTelefon());
        eMailFeld.setText(mitglied.getEmail());
        eMailIIFeld.setText(mitglied.getEmailII());
        geburtsdatumPicker.setValue(mitglied.getGeburtsdatum());
        alterLabel.setText("Geburtsdatum (" + Period.between(mitglied.getGeburtsdatum(), LocalDate.now()).getYears() + ")");
        mitgliedSeitLabel.setText("Eintritt (" + Period.between(mitglied.getEintrittsdatum(), LocalDate.now()).getYears() + ")");
        eintrittsDatumPicker.setValue(mitglied.getEintrittsdatum());
        austrittsDatumPicker.setValue(mitglied.getAustrittsDatum());
        comboBoxKategorieI.getSelectionModel().select(mitglied.getKategorieIElement().getStatusElementKey());
        comboBoxKategorieII.getSelectionModel().select(mitglied.getKategorieIIElement().getStatusElementKey());
        istVorstandsmitgliedCheckBox.setSelected(mitglied.getIstVorstandsmitglied().getValue());
        // nur jpg und png sind erlaubt...
        // TODO Image löschen, wenn es keines gibt.
        try {
            FileInputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_" + mitglied.getId() + ".jpg");
            Image image = new Image(inputStream);
            profilBild.setImage(image);
        } catch (FileNotFoundException e) {
            System.out.println("kein passendes Profilbild im jpg-Format gefunden");
            try {
                FileInputStream inputStream = new FileInputStream("ressources/images/profilbilder/ProfilBild_" + mitglied.getId() + ".png");
                Image image = new Image(inputStream);
                profilBild.setImage(image);
            } catch (FileNotFoundException ex) {
                System.out.println("kein Profilbild gefunden; Dummy wird gesetzt");
                try {
                    FileInputStream inputStream = new FileInputStream("ressources/images/profilbilder/Dummy.png");
                    Image image = new Image(inputStream);
                    profilBild.setImage(image);
                } catch (FileNotFoundException exe) {
                    System.out.println("gar nichts konnte gemacht werden");
                }
            }
        }


    }

    public void handelProfilbildButton() {
        //TODO Filter setzen for png und jpg
        FileChooser fileChooser = new FileChooser();
        // FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
        //TODO diese Filter funktionieren nicht für Mac OSX
        // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG (*.JPEG, *.jpeg)", "*.jpeg", "*.JPEG"));
        // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.png", "png"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile);
            saveNeuesProfilbild(selectedFile);

        } else {
            System.out.println("File selection cancelled.");
        }

    }

    public void saveNeuesProfilbild(File file) {
        Optional<String> ext = getExtensionByStringHandling(file.getName());
        String extStr = ext.get();
        System.out.println("Ext: " + ext.get());

        // File newFile = new File("profilbild.png");
        // file.renameTo(newFile);
        Path src = Paths.get(file.getAbsolutePath());
        Path dst = Paths.get("/Users/bernhardkaempf/Downloads/VereinsManager/ressources/images/profilbilder/ProfilBild_" + aktuellesMitglied.getId() + "." + extStr);
        try {
            java.nio.file.Files.copy(
                    src, dst, StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING
            );
            setMitglied(this.aktuellesMitglied);

        } catch (IOException e) {
            System.out.println("File konnte nicht gespeicher twerden" + e);
        }
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.adressController = new AdressController(this);
        this.adressController.Attach(this);
        mitgliedTabelle.setItems(this.mainApp.getVerein().getAdressBuch().getMitgliederListe());
        mitgliedTabelle.getSelectionModel().selectFirst();
       // mitgliedArrayList = new ArrayList<>(this.mainApp.getVerein().getAdressBuch().getMitgliederListeAsArrayList());
        mitgliedArrayList = new ArrayList<>(this.adressController.getMitgliederListe());

        aktuellesMitglied = mitgliedArrayList.get(0);

    }
    public void handleResetButton() {
        setMitglied(aktuellesMitglied);
    }

    public void handleLoeschenButton() {
        // Show the error message.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Löschen bestätigen");
        alert.setHeaderText("Willst Du den Kontakt wirklich löschen?");
        alert.setContentText("Löschen von\n\n" + aktuellesMitglied + "\n\nmit OK bestätigen oder abbrechen");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {

            mitgliedTabelle.getItems().remove(mitgliedTabelle.getSelectionModel().getSelectedItem());
            // TODO Profilbild: move to delete
            // TODO Löschung an MYSQL weitergeben
        }
    }
    /**
     * überprüft die Daten und speichert sie
     */
    public void handleSpeichernButton() {
        if (isInputValid()) {
            aktuellesMitglied.setNachName(nachNameFeld.getText());
            aktuellesMitglied.setVorName(vorNameFeld.getText());
            aktuellesMitglied.setAdresse(adresseFeld.getText());
            aktuellesMitglied.setAdresszusatz(adressZusatzFeld.getText());
            aktuellesMitglied.setPlz(Integer.parseInt(plzFeld.getText()));
            aktuellesMitglied.setOrt(ortFeld.getText());
            aktuellesMitglied.setGeburtsdatum(geburtsdatumPicker.getValue());
            aktuellesMitglied.setAnredeStatus(comboBoxAnrede.getValue());
            aktuellesMitglied.setMobile(mobileFeld.getText());
            aktuellesMitglied.setTelefon(telefonFeld.getText());
            aktuellesMitglied.setEmail(eMailFeld.getText());
            aktuellesMitglied.setEmailII(eMailIIFeld.getText());
            aktuellesMitglied.setBemerkungen(bemerkungsFeld.getText());
            aktuellesMitglied.setEintrittsDatum(eintrittsDatumPicker.getValue());
            aktuellesMitglied.setAustrittsDatum(austrittsDatumPicker.getValue());
            aktuellesMitglied.setKategorieIStatus(comboBoxKategorieI.getValue());
            aktuellesMitglied.setKategorieIIStatus(comboBoxKategorieII.getValue());
            aktuellesMitglied.setIstVorstandsmitglied(istVorstandsmitgliedCheckBox.isSelected());
            aktuellesMitglied.setLetzteAenderungTimestamp(Timestamp.valueOf(LocalDateTime.now()));

            setMitglied(aktuellesMitglied);

            // an SQL-Tabelle weitergeben
            DatabaseOperation.updateMitglied(aktuellesMitglied);

            mainFrameController.setInfo("Änderungen gespeichert!", "OK");

            // Tabelle aktualisieren
            mitgliedTabelle.refresh();
        } else {
           // mainFrameController.setInfo("NOK", "NOK");
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

        if (nachNameFeld.getText() == null || nachNameFeld.getText().length() == 0) {
            errorMeldung += "Nachname ist ungültig!\n";
            isValid = false;
        }
        if (vorNameFeld.getText() == null || vorNameFeld.getText().length() == 0) {
            errorMeldung += "Vorname ist ungültig!\n";
            isValid = false;
        }
        if (plzFeld.getText() == null || plzFeld.getText().length() == 0) {
            errorMeldung += "Ungültige PLZ!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(plzFeld.getText());
            } catch (NumberFormatException e) {
                errorMeldung += "PLZ muss eine Zahl sein!\n";
            }
        }

        mainFrameController.setInfo(errorMeldung, "NOK");
        return isValid;
    }

    @Override
    public void update(Object o) {
        System.out.println("Update-Meldung erhalten");
        if (o instanceof AdressController) {
            AdressController ac = (AdressController) o;
            Platform.runLater(new Runnable() {
                @Override
                public void run() { mitgliedTabelle.setItems(((AdressController) o).getMitgliederListe()); }
            });
        }
    }
}
