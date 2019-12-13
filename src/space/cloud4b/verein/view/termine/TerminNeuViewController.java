package space.cloud4b.verein.view.termine;

import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;

public class TerminNeuViewController {
    MainApp mainApp;
    Stage dialogStage;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void handleAbbrechen() {
        this.dialogStage.close();
    }
}
