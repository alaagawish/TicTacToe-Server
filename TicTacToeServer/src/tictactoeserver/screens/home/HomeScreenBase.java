package tictactoeserver.screens.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class HomeScreenBase extends AnchorPane {

    protected final Button saveButton;
    protected final PieChart pieChart;

    public HomeScreenBase() {

        saveButton = new Button();

        setId("AnchorPane");
        setPrefHeight(800.0);
        setPrefWidth(1280.0);
        setStyle("-fx-background-color: linear-gradient(#ffffff,#E5EDEE);;");

        saveButton.setLayoutX(540.0);
        saveButton.setLayoutY(35.0);
        saveButton.setMnemonicParsing(false);
        saveButton.setPrefHeight(100.0);
        saveButton.setPrefWidth(200.0);
        saveButton.setStyle("-fx-background-color: rgba(130,213,49,0.7); -fx-background-radius: 40; -fx-effect: dropshadow( one-pass-box  , #BFBFC3 , 10 ,0.3 , -4, 4 );");
        saveButton.setText("Start");
        saveButton.setTextFill(javafx.scene.paint.Color.valueOf("#f8f8f8"));
        saveButton.setFont(new Font("Comic Sans MS Bold", 45.0));

        ObservableList<PieChart.Data> pieChartList = initPieChartData();
        pieChart = new PieChart(pieChartList);
        pieChart.setLayoutX(298.0);
        pieChart.setLayoutY(147.0);
        pieChart.setPrefHeight(619.0);
        pieChart.setPrefWidth(685.0);
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(30);
        pieChart.setStartAngle(90);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setLegendSide(Side.BOTTOM);

        getChildren().add(saveButton);
        getChildren().add(pieChart);

    }

    private ObservableList<PieChart.Data> initPieChartData() {
        ObservableList<PieChart.Data> pieChartData;
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Online", 25),
                new PieChart.Data("Offline", 45)
        );

        return pieChartData;
    }
}
