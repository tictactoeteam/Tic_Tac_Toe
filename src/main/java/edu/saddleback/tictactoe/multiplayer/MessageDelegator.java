package edu.saddleback.tictactoe.multiplayer;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.HashMap;

/**
 * Sends the recieved message to the correct registered handler on the server, also handles the presence updates.
 */
public class MessageDelegator extends SubscribeCallback {
    private HashMap<String, MessageHandler> handlers;

    /**
     * Constructor
     */
    public MessageDelegator() {
        this.handlers = new HashMap<>();
    }

    /**
     * Adds handler to hashMap.
     * @param type
     * @param handler
     */
    public void addHandler(String type, MessageHandler handler) {
        handlers.put(type, handler);
    }

    /**
     * Makes one handler for multiple message types.
     * @param types
     * @param handler
     */
    public void addHandler(String[] types, MessageHandler handler) {
        for (int i=0; i<types.length; ++i){
            handlers.put(types[i], handler);
        }
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
    }

    /**
     * Actually sends the message to the right handler
     * @param pubnub
     * @param message
     */
    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        String type = message.getMessage().getAsJsonObject().get("type").getAsString();
        JsonObject data = message.getMessage().getAsJsonObject().get("data").getAsJsonObject();

        data.addProperty("UUID", message.getPublisher());

        MessageHandler handler = handlers.get(type);

        if (handler != null) {
            handler.handleMessage(data, pubnub, message.getPublisher());
        }
    }

    /**
     * Prints presence data to the server.
     * @param pubnub
     * @param presence
     */
    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        if (presence.getEvent().equals("join")) {
            System.out.println("Player joined");
            System.out.println(presence.getUuid());
            presence.getHereNowRefresh();
            presence.getUuid(); // 175c2c67-b2a9-470d-8f4b-1db94f90e39e
            presence.getTimestamp(); // 1345546797
            presence.getOccupancy(); // 2
        }
        else if(presence.getEvent().equals("leave")){
            System.out.println("Player left");
            System.out.println(presence.getUuid());
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
        System.out.println(pubnub.hereNow());
    }
}
