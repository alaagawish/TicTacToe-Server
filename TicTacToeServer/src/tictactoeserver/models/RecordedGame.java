package tictactoeserver.models;

public class RecordedGame {

    int recordId;
    String recordName;
    int playerId;

    public RecordedGame() {
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "RecordedGame{" + "recordId=" + recordId + ", recordName=" + recordName + ", playerId=" + playerId + '}';
    }

}
