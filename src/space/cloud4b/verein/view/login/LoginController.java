package space.cloud4b.verein.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;

import java.awt.*;

public class LoginController {
    private MainApp mainApp;
    private Stage dialogStage;

    @FXML
    private TextField userName;
    @FXML
    private PasswordField pwFeld;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void handleLoginButton() {

    }


}
