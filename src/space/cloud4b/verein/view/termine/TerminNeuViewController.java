package space.cloud4b.verein.view.termine;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.model.verein.kalender.Termin;
import space.cloud4b.verein.services.DatabaseOperation;

public class TerminNeuViewController {
    MainApp mainApp;
    Stage dialogStage;
    @FXML
    private DatePicker terminDatumPicker;
    @FXML
    private TextField terminTextFeld;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void handleAbbrechen() {
        this.dialogStage.close();
    }

    public void handleSpeichern() throws InterruptedException {
        int neueId = 0;
        Termin neuerTermin = null;
        if(isValid()){
            this.dialogStage.close();
            neueId = DatabaseOperation.saveNewTermin(terminTextFeld.getText(), terminDatumPicker.getValue().toString());
            System.out.println("NeueTerminID: " + neueId);
            // neuesMitglied = new Mitglied(neueId, nachnameFeld.getText(), vornameFeld.getText(), eintrittsDatumPicker.getValue().toString());
        }

        // mainApp.getMitgliedViewController().setMitglied(neueId);
    }

    private boolean isValid(){
        boolean isValid = true;
        if(terminDatumPicker.getValue() == null) {
            isValid = false;
        }
        if(terminTextFeld.getText() == null) {
            isValid = false;
        }
        return isValid;
    }

}
