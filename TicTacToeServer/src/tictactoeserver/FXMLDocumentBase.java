package tictactoeserver;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public abstract class FXMLDocumentBase extends AnchorPane {

    protected final TableView tableView;
    protected final TableColumn tableColumn;
    protected final Label label;
    protected final TableColumn tableColumn0;
    protected final Button button;

    public FXMLDocumentBase() {

        tableView = new TableView();
        tableColumn = new TableColumn();
        label = new Label();
        tableColumn0 = new TableColumn();
        button = new Button();

        setId("AnchorPane");
        setPrefHeight(200);
        setPrefWidth(320);

        tableView.setLayoutX(95.0);
        tableView.setLayoutY(20.0);
        tableView.setPrefHeight(200.0);
        tableView.setPrefWidth(200.0);

        tableColumn.setPrefWidth(75.0);
        tableColumn.setText("C2");

        label.setMinHeight(16);
        label.setMinWidth(69);
        tableColumn.setGraphic(label);

        tableColumn0.setPrefWidth(75.0);
        tableColumn0.setText("C1");

        button.setOnAction(this::handleButtonAction);
        button.setText("Click Me!");
        tableColumn0.setGraphic(button);

        tableView.getColumns().add(tableColumn);
        tableView.getColumns().add(tableColumn0);
        getChildren().add(tableView);

    }

    protected abstract void handleButtonAction(javafx.event.ActionEvent actionEvent);

}
