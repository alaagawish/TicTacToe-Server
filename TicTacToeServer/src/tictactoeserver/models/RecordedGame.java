/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.models;

/**
 *
 * @author moazk
 */
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
