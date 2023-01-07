package tictactoeserver.models;

import java.util.ArrayList;
import java.util.List;

public class Message {

    String operation;
    boolean status;
    String ipAddress;
    List<Player> players;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Message() {
        this.players = new ArrayList<>();
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setPlayers(Player player) {
        this.players.add(player);
    }

    public String getOperation() {
        return operation;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return " [operatio=" + operation + ", player[" + players + "]";
    }

}
