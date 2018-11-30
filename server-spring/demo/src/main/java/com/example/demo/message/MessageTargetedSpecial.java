package com.example.demo.message;

public class MessageTargetedSpecial extends Message {

    private int target;

    public MessageTargetedSpecial() {}

    public MessageTargetedSpecial(boolean player1, int idCardTarget) {
        super(player1);
        this.target = idCardTarget;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int idCard) {
        this.target = idCard;
    }
}