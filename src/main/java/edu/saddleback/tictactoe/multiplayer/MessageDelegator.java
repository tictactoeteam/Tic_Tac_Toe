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

    }
}
