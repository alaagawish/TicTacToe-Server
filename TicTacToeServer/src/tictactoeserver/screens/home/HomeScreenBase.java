package tictactoeserver.screens.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tictactoeserver.network.Network;

public class HomeScreenBase extends AnchorPane {

    protected final Button startButton;
    protected final PieChart pieChart;
    boolean flag;
    public static Network network;

    public HomeScreenBase() {

        startButton = new Button();
        flag = false;
        setId("AnchorPane");
        setPrefHeight(800.0);
        setPrefWidth(1280.0);
        setStyle("-fx-background-color: linear-gradient(#ffffff,#E5EDEE);;");

        startButton.setLayoutX(540.0);
        startButton.setLayoutY(35.0);
        startButton.setMnemonicParsing(false);
        startButton.setPrefHeight(100.0);
        startButton.setPrefWidth(200.0);
        startButton.setStyle("-fx-background-color: rgba(130,213,49,0.7); -fx-background-radius: 40; -fx-effect: dropshadow( one-pass-box  , #BFBFC3 , 10 ,0.3 , -4, 4 );");
        startButton.setText("Start");
        startButton.setTextFill(javafx.scene.paint.Color.valueOf("#f8f8f8"));
        startButton.setFont(new Font("Comic Sans MS Bold", 45.0));

        ObservableList<PieChart.Data> pieChartList = initPieChartData();
        pieChart = new PieChart(pieChartList);
        Label percentageLabel = showPercentage(pieChart);
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

        getChildren().addAll(startButton, pieChart, percentageLabel);
        pieChart.setVisible(false);

        startButton.setOnAction(e -> {
            if (!flag) {
                startButton.setText("Stop");
                startButton.setStyle("-fx-background-color:  rgba(235, 59, 62,1); -fx-background-radius: 40; -fx-effect: dropshadow( one-pass-box  , #BFBFC3 , 10 ,0.3 , -4, 4 );");
                pieChart.setVisible(true);
                flag = true;
                network = new Network();

            } else {
                startButton.setText("Start");
                startButton.setStyle("-fx-background-color: rgba(130,213,49,0.7); -fx-background-radius: 40; -fx-effect: dropshadow( one-pass-box  , #BFBFC3 , 10 ,0.3 , -4, 4 );");
                pieChart.setVisible(false);
                flag = false;
                network.close();
            }

        });

    }

    private ObservableList<PieChart.Data> initPieChartData() {
        ObservableList<PieChart.Data> pieChartData;
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Online", 25),
                new PieChart.Data("Offline", 45)
        );

        return pieChartData;
    }

    private Label showPercentage(PieChart pc) {
        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setFont(new Font("Comic Sans MS", 50.0));

        for (final PieChart.Data data : pc.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        double total = 0;
                        for (PieChart.Data d : pc.getData()) {
                            total += d.getPieValue();
                        }
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100 * data.getPieValue() / total);
                        caption.setText(text);
                    }
            );
        }
        return caption;
    }

}
