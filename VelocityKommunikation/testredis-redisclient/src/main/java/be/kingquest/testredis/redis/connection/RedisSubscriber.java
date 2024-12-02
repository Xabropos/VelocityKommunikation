package be.kingquest.testredis.redis.connection;


import be.kingquest.testredis.redis.Synchronizer;
import be.kingquest.testredis.redis.handler.CustomMessageHandler;
import be.kingquest.testredis.redis.handler.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RedisSubscriber extends RedisConnection {

    private final String messageChannel;
    private final String messageSalt;
    private final List<CustomMessageHandler> customMessageHandlers;

    public RedisSubscriber(String host, int port, String user, String password, String clientName, int database, String messageChannel, String messageSalt, Synchronizer synchronizer, Logger log) {
        super(host, port, user, password, clientName, database, log);
        this.messageChannel = messageChannel;
        this.messageSalt = messageSalt;
        this.customMessageHandlers = new ArrayList<>();
        this.customMessageHandlers.add(new PacketHandler(synchronizer));
    }

    @Override
    public void connect() {
        super.connect();
        subscribe();
    }

    public void subscribe() {
        this.jedis.subscribe(new FriendPubSub(this.messageChannel, this.messageSalt, this.customMessageHandlers, this.log), this.messageChannel);
    }

    public void addMessageHandler(CustomMessageHandler messageHandler) {
        if(this.customMessageHandlers.stream().anyMatch(handler -> handler.getIdentifier().equals(messageHandler.getIdentifier()))) {
            throw new IllegalArgumentException("Duplicate CustomMessageHandler identifier: "+messageHandler.getIdentifier());
        }
        this.customMessageHandlers.add(messageHandler);
    }

}
