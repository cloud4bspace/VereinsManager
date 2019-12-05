package space.cloud4b.verein.view.termine;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import space.cloud4b.verein.MainApp;
import space.cloud4b.verein.controller.AdressController;
import space.cloud4b.verein.controller.KalenderController;
import space.cloud4b.verein.model.verein.status.StatusElement;
import space.cloud4b.verein.view.mainframe.MainFrameController;
import sun.jvm.hotspot.oops.IntField;

import javax.xml.soap.Text;
import java.util.ArrayList;

public class TerminViewController {

    @FXML
    public Slider stundenSlider = new Slider(0, 23, 12);
    @FXML
    public Slider minutenSlider = new Slider(0,59,30);
    @FXML
    public TextField stundenFeld = new TextField();
    @FXML
    public TextField minutenFeld = new TextField();


    private Stage dialogStage;
    private MainFrameController mainFrameController;
    private KalenderController kalenderController;
    private MainApp mainApp;

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
        stundenSlider.setTooltip(new Tooltip("Hallo"));
        stundenSlider.valueProperty().addListener((obs, oldval, newVal) ->
                stundenSlider.setValue(Math.round(newVal.doubleValue())));
        stundenFeld.textProperty()
                .bindBidirectional(stundenSlider.valueProperty(), new NumberStringConverter());

        minutenSlider.valueProperty().addListener((obs, oldval, newVal) ->
                minutenSlider.setValue(Math.round(newVal.doubleValue())));
        minutenFeld.textProperty()
                .bindBidirectional(minutenSlider.valueProperty(), new NumberStringConverter());


    }

}
