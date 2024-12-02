package be.kingquest.testredis.redis.connection;

import be.kingquest.testredis.redis.handler.CustomMessageHandler;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class FriendPubSub extends JedisPubSub {

    private final String messageChannel;
    private final String messageSalt;
    private final List<CustomMessageHandler> customMessageHandlers;
    private final Logger log;

    public FriendPubSub(String messageChannel, String messageSalt, List<CustomMessageHandler> customMessageHandlers, Logger log) {
        this.messageChannel = messageChannel;
        this.messageSalt = messageSalt;
        this.customMessageHandlers = customMessageHandlers;
        this.log = log;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!Objects.equals(messageChannel, channel)) return;
        final String decoded;
        try {
            decoded = RedisPublisher.fromBase64(message.substring(this.messageSalt.length()));
        } catch (Throwable ex) {
            log.severe("Error while decoding Redis message. Maybe the salt is configured incorrect? (Error: '"+ex+"' Message: '"+message+"')");
            return;
        }
        customMessageHandlers.stream()
                .filter(handler -> decoded.startsWith(handler.getIdentifier()))
                .findFirst()
                .ifPresentOrElse(
                        handler -> handler.processMessage(decoded.substring(handler.getIdentifier().length())),
                        () -> log.warning("Could not find handler for message. Maybe the salt is configured incorrect? (Message: '"+message+"')")
                );
    }
}
