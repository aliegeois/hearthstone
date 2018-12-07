package com.example.demo.message;

public class MessageJoinLobby {
    private String playerName;

    public MessageJoinLobby() {}

    public MessageJoinLobby(String playerName) {
        // faire des trucs
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String newPlayerName) {
        playerName = newPlayerName;
    }
}