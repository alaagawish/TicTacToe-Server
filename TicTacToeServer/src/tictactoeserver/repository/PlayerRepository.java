package tictactoeserver.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.models.Player;

public class PlayerRepository {

    Repository repository = Repository.getInstance();

    public void selectAllPlayer() {
        try {

            Statement stmt = repository.connection.createStatement();
            String queryString = new String("SELECT * FROM Player");
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
}
