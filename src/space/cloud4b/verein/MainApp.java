package space.cloud4b.verein;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import space.cloud4b.verein.controller.MainController;
import space.cloud4b.verein.einstellungen.Einstellung;
import space.cloud4b.verein.model.verein.Verein;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.view.chart.BirthdayStatisticsController;
import space.cloud4b.verein.view.dashboard.DashBoardController;
import space.cloud4b.verein.view.mainframe.MainFrameController;
import space.cloud4b.verein.view.mitglieder.MitgliedViewController;

import java.io.File;
import java.io.IOException;

public class MainApp extends Application {
    private Verein verein;
    private Stage primaryStage;
    private BorderPane mainFrame;
    private MainFrameController mainFrameController;
    private MainController mainController;

    public MainApp() {

        verein = new Verein(Einstellung.getVereinsName());
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(verein.toString());
        this.primaryStage.getIcons().add(new Image("file:ressources/images/address_book_32.png"));
        this.primaryStage.setMaximized(true);
        this.mainController = new MainController(this);
        initMainFrame();
        showDashboard();
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initMainFrame() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/mainframe/MainFrame.fxml"));
            mainFrame = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(mainFrame);
            primaryStage.setScene(scene);
            // Give the controller access to the main app.
            MainFrameController controller = loader.getController();
            controller.setMainApp(this);
            this.mainFrameController = controller;
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showDashboard() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/dashboard/DashBoard.fxml"));
            AnchorPane dashBoard = (AnchorPane) loader.load();
            dashBoard.setPadding(new Insets(10,10,10,10));
            // Set person overview into the center of root layout.
            mainFrame.setCenter(dashBoard);

            // Give the controller access to the main app.
            DashBoardController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zeigt den Mitgliederbereich
     */
    public boolean showContactEditDialog(Mitglied mitglied) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/mitglieder/MitgliedView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            mainFrame.setCenter(page);
            MitgliedViewController controller = loader.getController();
            controller.setMitglied(this.getVerein().getAdressBuch().getMitgliederListeAsArrayList().get(0));
            controller.setMainApp(this);
            controller.setMainFrameController(mainFrameController);


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/chart/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            // controller.setPersonData(contactData);
            controller.setPersonData(verein.getAdressBuch().getMitgliederListe());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public Verein getVerein() {
        return verein;
    }
    public MainController getMainController() { return mainController; }
    public MainFrameController getMainFrameController() {return mainFrameController; }

    public static void main(String[] args) {
        launch(args);
    }

}
