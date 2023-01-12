package tictactoeserver.models;

import java.util.ArrayList;
import java.util.List;

public class Message {

    String operation;
    String status;
    List<Player> players;
    List<Move> moves;

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public Message() {
        players = new ArrayList<Player>();
        moves = new ArrayList<Move>();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Player players) {
        this.players.add(players);
    }

    public void setPlayers(List<Player> players) {
        this.players.addAll(players);
    }

    @Override
    public String toString() {
        return " [operation=" + operation + ", player[" + players + "]";
    }

}
