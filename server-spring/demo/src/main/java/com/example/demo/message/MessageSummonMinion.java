package com.example.demo.message;

public class MessageSummonMinion extends Message {

    private int idCard;

    public MessageSummonMinion() {}

    public MessageSummonMinion(boolean player1, int idCard) {
        super(player1);
        this.idCard = idCard;
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int newIdCard) {
        idCard = newIdCard;
    }
}