package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class MiniHearthstone {
    private List<String> players = new ArrayList<>();
    
    public MiniHearthstone() {

    }

    public void addPlayer(String name) {
        players.add(name);
    }

    public List<String> getPlayers() {
        return players;
    }
}