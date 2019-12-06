package space.cloud4b.verein.view.mitglieder;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.services.DatabaseOperation;

public class MitgliedNeuViewController {
    MainApp mainApp;
    Stage dialogStage;
    @FXML
    private TextField nachnameFeld;
    @FXML
    private TextField vornameFeld;
    @FXML
    private DatePicker eintrittsDatumPicker;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void handleSpeichern() {
        System.out.println("Nachname: " + nachnameFeld.getText());
        System.out.println("Vorname: " + vornameFeld.getText());
        System.out.println("Eintritt: " + eintrittsDatumPicker.getValue());
        if(isValid()){
            this.dialogStage.close();
            DatabaseOperation.saveNewMember(nachnameFeld.getText(), vornameFeld.getText(), eintrittsDatumPicker.getValue().toString());


        }
    }

    private boolean isValid(){
        boolean isValid = true;
        if(nachnameFeld.getText() == null) {
            isValid = false;
        }
        if(vornameFeld.getText() == null) {
            isValid = false;
        }
        if(eintrittsDatumPicker.getValue() == null) {
            isValid = false;
        }
        return isValid;
    }
    public void handleAbbrechen() {
        this.dialogStage.close();
    }
}
