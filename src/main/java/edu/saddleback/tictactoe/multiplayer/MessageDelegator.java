package edu.saddleback.tictactoe.multiplayer;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.HashMap;

public class MessageDelegator extends SubscribeCallback {
    private HashMap<String, MessageHandler> handlers;

    public MessageDelegator() {
        this.handlers = new HashMap<>();
    }

    public void addHandler(String type, MessageHandler handler) {
        handlers.put(type, handler);
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        String type = message.getMessage().getAsJsonObject().get("type").getAsString();
        JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

        MessageHandler handler = handlers.get(type);

        if (handler != null) {
            handler.handleMessage(data, pubnub, message.getPublisher());
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        if (presence.getEvent().equals("join")) {
            System.out.println("Player joined");
            presence.getHereNowRefresh();
            presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
            presence.getTimestamp(); // 1345546797
            presence.getOccupancy(); // 2
        }
        else if(presence.getEvent().equals("leave")){
            System.out.println("Player left");
            presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
            presence.getTimestamp(); // 1345546797
            presence.getOccupancy(); // 2
        }
        else{
            System.out.println("Timed out");
            presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
            presence.getTimestamp(); // 1345546797
            presence.getOccupancy(); // 2

            
        }
    }
}
