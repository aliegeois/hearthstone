package com.example.demo;

abstract class Message {
    protected boolean player1;
    
    public Message() {}

    public Message(boolean player1) {
        this.player1 = player1;
    }

    public boolean getPlayer1() {
        return player1;
    }

    public void setPlayer1(boolean value) {
        player1 = value;
    }
}