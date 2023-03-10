package tictactoeserver.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.apache.derby.jdbc.ClientDriver;

public class Repository {

    private static Repository repository;
    Connection connection;

    private Repository() {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe", "root", "root");
        } catch (SQLException ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error DB");
            alert.setHeaderText("Error in DataBase");
            alert.setContentText("You should start Database first" + ex.getLocalizedMessage());
            alert.showAndWait();

        }
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

}
