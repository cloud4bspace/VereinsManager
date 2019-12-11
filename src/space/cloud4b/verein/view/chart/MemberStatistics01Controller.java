package space.cloud4b.verein.view.chart;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import space.cloud4b.verein.daten.mysql.service.DatenLieferant;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;

public class MemberStatistics01Controller {

    ObservableList<Mitglied> contactData;


    @FXML
    private PieChart pieChart;

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        pieChartData = DatenLieferant.getDataForPieChart01();
        pieChart.setData(pieChartData);

    }




}
