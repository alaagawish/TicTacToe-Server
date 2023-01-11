package tictactoeserver.screens.home;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tictactoeserver.network.Network;
import tictactoeserver.repository.PlayerRepository;

public class HomeScreenBase extends AnchorPane {

    protected static int offlineNumber;
    protected static int onlineNumber;
    protected final Button startButton;
    protected PieChart pieChart;
    protected Label percentageLabel;
    private boolean flag;
    public static Network network;
    private Thread thread;
    private final PlayerRepository playerRepository;
    protected ObservableList<PieChart.Data> pieChartList;

    public HomeScreenBase(Stage stage) {
        playerRepository = new PlayerRepository();
        startButton = new Button();
        percentageLabel = new Label();
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

        pieChart = new PieChart();

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
                onlineNumber = playerRepository.selectOnline();
                offlineNumber = playerRepository.selectOffline();
                pieChartList = initPieChartData(onlineNumber, offlineNumber);
                pieChart.setData(pieChartList);
                percentageLabel = showPercentage(pieChart);
                startButton.setText("Stop");
                startButton.setStyle("-fx-background-color:  rgba(235, 59, 62,1); -fx-background-radius: 40; -fx-effect: dropshadow( one-pass-box  , #BFBFC3 , 10 ,0.3 , -4, 4 );");
                pieChart.setVisible(true);
                flag = true;
                network = new Network();
                updateChart();

            } else {
                startButton.setText("Start");
                startButton.setStyle("-fx-background-color: rgba(130,213,49,0.7); -fx-background-radius: 40; -fx-effect: dropshadow( one-pass-box  , #BFBFC3 , 10 ,0.3 , -4, 4 );");
                pieChart.setVisible(false);
                flag = false;
                network.close();
                thread.stop();

            }

        });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("from set onClose request and stop the network");
                thread.stop();
                System.out.println("The network.close executed");
            }
        });

    }

    private ObservableList<PieChart.Data> initPieChartData(int onlineNumber, int offlineNumber) {
        ObservableList<PieChart.Data> pieChartData;
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Online", onlineNumber),
                new PieChart.Data("Offline", offlineNumber)
        );

        return pieChartData;
    }

    private Label showPercentage(PieChart pc) {
        Label caption = new Label("");
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

    public void updateChart() {
        System.out.println("From update Chart and the flag is " + flag);
        if (flag == true) {
            System.out.println("from flag");
            thread = new Thread() {
                @Override
                public void run() {
                    System.out.println("from run of thread object");

                    Runnable updater = new Runnable() {

                        @Override
                        public void run() {
                            System.err.println("it runs from runnable");
                            onlineNumber = playerRepository.selectOnline();
                            offlineNumber = playerRepository.selectOffline();
                            pieChartList = initPieChartData(onlineNumber, offlineNumber);
                            pieChart.setData(pieChartList);
                        }
                    };

                    while (network.isAlive()) {
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HomeScreenBase.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace();
                            System.out.println("Exception from InterruptedException of thread" + ex);
                        }
                        // UI update is run on the Application thread
                        Platform.runLater(updater);
                    }

                }
            };

            // don't let thread prevent JVM shutdown
            thread.setDaemon(true);
            thread.start();
        }
    }
}
