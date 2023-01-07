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

    public Player editPassword(String username, String password) {
        Player player = new Player();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String pw = "";
        try {
            System.out.println("new pw in player repo" + password);
            preparedStatement = repository.connection.prepareStatement("UPDATE ROOT.PLAYER SET PASSWORD=? where PLAYERNAME=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            if (preparedStatement.executeUpdate() > 0) {

                System.out.println("done execute update");

                player = login(username, password);
                System.out.println(player);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;

    }

    public Player login(String username, String password) {
        Player player = new Player();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String pw = "";
        try {
            preparedStatement = repository.connection.prepareStatement("select * from ROOT.PLAYER where PLAYERNAME=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            System.out.println("resultset result: " + resultSet);
            if (resultSet.next()) {

                pw = resultSet.getString(5);
                System.out.println("password: " + pw);
                if (pw.equals(password)) {
                    player.setId(resultSet.getInt("ID"));
                    player.setUsername(resultSet.getString("PLAYERNAME"));
                    player.setScore(resultSet.getInt("SCORE"));
                    player.setPassword(resultSet.getString("PASSWORD"));
                    player.setStatus(resultSet.getString("STATUS"));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;

    }

}
