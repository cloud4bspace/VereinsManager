package space.cloud4b.verein.view.browser;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class WebView001 extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("Web View");
        scene = new Scene(new Browser("https://www.cloud4b.space/VereinsManager/Hilfe/help.html"),750,500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("../css/BrowserToolbar.css");
        //TODO Path to stylesheet not correct...
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    public void start() {

        showBrowser();
    }

    public void showBrowser(){

    }
}


