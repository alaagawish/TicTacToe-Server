package tictactoeserver.models;

public class Player {

    private String username;
    private String password;
    String ipAddress;
    String status;
    int score;
    int id;
    int portNum;

    public Player(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public Player() {

    }

    public Player(String username, String password, int score, String status, int id) {

        this.username = username;
        this.password = password;
        this.score = score;
        this.id = id;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        this.username = username;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
//               return "Player{" + "id=" + id + ", playerName=" + playerName + ", ipAddress=" + ipAddress + ", status=" + status + ", password=" + password + ", score=" + score + '}';
        return "Player{" + ", username=" + username + ", password=" + password + '}';

    }
}
