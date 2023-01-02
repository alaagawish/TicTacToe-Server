package tictactoeserver.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public boolean login(String username, String password) {

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String pw = "";
        try {
            preparedStatement = repository.connection.prepareStatement("select * from ROOT.PLAYER where PLAYERNAME=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            pw = resultSet.getString(5);

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pw.equals(password)) {
            return true;
        } else {
            return false;
        }

    }
}
