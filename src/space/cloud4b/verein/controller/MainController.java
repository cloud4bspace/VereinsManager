package space.cloud4b.verein.controller;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.view.browser.Browser;

public class MainController {

    private MainApp mainApp;

    public MainController(MainApp mainApp) {
        System.out.println("MainController erzeugt");
        this.mainApp = mainApp;
    }

    public void showHilfe() {
        System.out.println("showHilfe");
        Stage stage = new Stage();
        stage.setTitle("Web View");
        Scene scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Hilfe/help.html"), 750, 500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();
    }

    public void showInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vereins-App");
        alert.setHeaderText("über..");
        //System.getProperty("user.name");
        alert.setContentText(System.getProperty("user.name") + "\nWebsite: https://cloud4b.space");
        alert.showAndWait();
    }



}
