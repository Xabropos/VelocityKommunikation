package be.kingquest.testredis.redis.connection;


import java.util.Base64;
import java.util.logging.Logger;

public class RedisPublisher extends RedisConnection {

    private final String messageChannel;
    private final String messageSalt;

    public RedisPublisher(String host, int port, String user, String password, String clientName, int database, String messageChannel, Logger log, String messageSalt) {
        super(host, port, user, password, clientName, database, log);
        this.messageChannel = messageChannel;
        this.messageSalt = messageSalt;
    }

    public void publish(String message) {
        final String encoded = toBase64(message);
        this.jedis.publish(this.messageChannel, this.messageSalt+encoded);
    }

    public static String toBase64(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }

    public static String fromBase64(String base) {
        return new String(Base64.getDecoder().decode(base.getBytes()));
    }

}
