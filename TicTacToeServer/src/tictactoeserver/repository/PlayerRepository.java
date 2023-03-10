package tictactoeserver.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
            preparedStatement = repository.connection.prepareStatement("UPDATE ROOT.PLAYER SET PASSWORD=? where PLAYERNAME=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            if (preparedStatement.executeUpdate() > 0) {

                player = loginE(username, password);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;

    }

    public Player requestGame(String username, String password) {
        Player player = new Player();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String pw = "";
        try {
            preparedStatement = repository.connection.prepareStatement("UPDATE ROOT.PLAYER SET PASSWORD=? where PLAYERNAME=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            if (preparedStatement.executeUpdate() > 0) {

//                System.out.println("done execute update");
                player = loginE(username, password);
//                System.out.println(player);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;

    }

    public synchronized Player login(String username, String password) {
        Player player = new Player();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String pw = "";
        int resultFromUpdate;
        String query = "select * from ROOT.PLAYER where PLAYERNAME=? and PASSWORD=?";
        try {
            preparedStatement = repository.connection.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                resultSet.next();
            }
            pw = resultSet.getString("PASSWORD");

            if (pw.equals(password)) {
                resultFromUpdate = updateStatusOnline(username);
                System.out.println("password: " + pw);
                System.out.println("Status: " + resultSet.getString("STATUS"));
                System.out.println("result from Update " + resultFromUpdate + "");
                player.setId(resultSet.getInt("ID"));
                player.setUsername(resultSet.getString("PLAYERNAME"));
                player.setScore(resultSet.getInt("SCORE"));
                player.setPassword(resultSet.getString("PASSWORD"));
                player.setStatus(resultSet.getString("STATUS"));
                player.setIpAddress(resultSet.getString("IPADDRESS"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;

    }

    public synchronized boolean logout(String userName) {
        PreparedStatement preparedStatement;
        String query = "update ROOT.PLAYER set STATUS='offline' where PLAYERNAME=?";

        try {
            preparedStatement = repository.connection.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, userName);
            int result = preparedStatement.executeUpdate();
            System.out.println("done execute logout");
            System.out.println(result);
            if (result == 1) {
                System.out.println("enter logout");
                return true;
            } else {
                System.out.println("result from logout = 0");
                return false;
            }

        } catch (SQLException ex) {
            System.err.println("EXception from logout function " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return false;

    }

    public synchronized Player loginE(String username, String password) {
        Player player = new Player();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String pw = "";
        int resultFromUpdate;
        try {
            preparedStatement = repository.connection.prepareStatement("select * from ROOT.PLAYER where PLAYERNAME=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            System.out.println("resultset result: " + resultSet);
            if (resultSet.next()) {
                resultFromUpdate = updateStatusOnline(username);
                pw = resultSet.getString(5);
                System.out.println("password: " + pw);
                System.out.println("result from Update " + resultFromUpdate + "");
                player.setId(resultSet.getInt("ID"));
                player.setUsername(resultSet.getString("PLAYERNAME"));
                player.setScore(resultSet.getInt("SCORE"));
                player.setPassword(resultSet.getString("PASSWORD"));
                player.setStatus(resultSet.getString("STATUS"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return player;

    }

    private synchronized int updateStatusOnline(String playerName) {
        PreparedStatement preparedStatement;
        int result = 0;
        String query = "update player set status='online' where PLAYERNAME=?";
        try {
            preparedStatement = repository.connection.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, playerName);
            result = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;

    }

    public Player registerPlayer(String userName, String password) {
        Player player = new Player();
        try {
            int lastinsertid = 0;

            PreparedStatement ps1 = repository.connection.prepareStatement("Select PlayerName from Player Where PlayerName = ?");
            ps1.setString(1, userName);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                System.out.print("Person name is exit, please enter another userName");
            } else {
                PreparedStatement ps1LastId = repository.connection.prepareStatement("Select MAX(Id) FROM ROOT.Player");
                ResultSet rs2 = ps1LastId.executeQuery();
                if (rs2.next()) {
                    lastinsertid = rs2.getInt(1);
                    System.out.println("lastID: " + lastinsertid);
                    lastinsertid++;
                    System.out.println("increase" + lastinsertid);
                }
                PreparedStatement pst = repository.connection.prepareStatement("INSERT INTO ROOT.PLAYER (Id,PlayerName,IPAddress,Status,Password,Score) VALUES (? ,?, ? ,? , ? , ?)");
                pst.setInt(1, lastinsertid);
                pst.setString(2, userName);
                pst.setString(3, "123.123.123.123");
                pst.setString(4, "online");
                pst.setString(5, password);
                pst.setInt(6, 0);
                pst.executeUpdate();

                player.setId(lastinsertid);
                player.setUsername(userName);
                player.setPassword(password);
                player.setStatus("online");
                player.setScore(0);
                System.out.print("new Player : " + player.getId() + " " + player.getUsername() + " " + player.getPassword() + " "
                        + player.getStatus() + "nnnn");
            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return player;
    }

    public List<Player> getOfflinePlayers() {
        List<Player> players = new ArrayList<>();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = repository.connection.prepareStatement("select * from ROOT.PLAYER where STATUS= 'online'",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println("player: " + resultSet.getString(2));

                Player player = new Player(resultSet.getString("playername"), resultSet.getString("password"), resultSet.getInt("SCORE"), resultSet.getString("STATUS"), resultSet.getInt("id"));

                players.add(player);

                System.out.println(player.getId() + ", " + player.getUsername()
                        + ", " + player.getStatus() + ", " + player.getPassword() + ", " + player.getScore());
            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players;

    }

    public synchronized int selectOffline() {

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String query = "select count(status) from player where status='offline'";
        int offlineNumber = 0;
        try {
            preparedStatement = repository.connection.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                offlineNumber = resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        return offlineNumber;

    }

    public synchronized int selectOnline() {
        PreparedStatement ps;
        ResultSet rs;
        String query = "select count(status) from player where status='available' or status  ='online'";
        int onlineNumber = 0;
        try {
            ps = repository.connection.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            if (rs.next()) {
                onlineNumber = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        return onlineNumber;
    }

    public synchronized void updateScore(String username, int score) {
        try {
            PreparedStatement pst = repository.connection.prepareStatement("UPDATE ROOT.PLAYER SET SCORE=? where PLAYERNAME=?");
            pst.setInt(1, score);
            pst.setString(2, username);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int selectScore(String username) {
        int score = 0;
        try {
            PreparedStatement ps = repository.connection.prepareStatement("SELECT SCORE FROM ROOT.PLAYER WHERE PLAYERNAME=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                score = rs.getInt("Score");
                System.out.println("Score: " + score);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return score;
    }

}
