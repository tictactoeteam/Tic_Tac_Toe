package edu.saddleback.tictactoe.multiplayer;

import com.pubnub.api.PubNub;

import java.util.UUID;

public class ServerThread implements Runnable {
    private PubNub pubnub;

    public PubNub getPubnub() {
        return pubnub;
    }

    public void setPubnub(PubNub pubnub) {
        this.pubnub = pubnub;
    }

    @Override
    public void run() {

    }
}
