package com.example.model;

import java.time.Instant;

public class GreetingPlus extends Greeting {
    private Instant timestamp;

    public GreetingPlus(String name, Instant timestamp){
        super(name);
        this.timestamp = timestamp;
    }

    public GreetingPlus(String name){
        super(name);
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
