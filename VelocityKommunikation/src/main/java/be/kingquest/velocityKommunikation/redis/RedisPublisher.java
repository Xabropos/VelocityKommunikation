package be.kingquest.velocityKommunikation.redis;
import redis.clients.jedis.Jedis;

public class RedisPublisher {

    private Jedis jedis;

    public RedisPublisher() {
        this.jedis = new Jedis("89.58.30.136", 6379); // Stelle eine Verbindung zum Redis-Server her
    }

    // Nachricht auf einem Kanal veröffentlichen
    public void publishMessage(String channel, String message) {
        jedis.publish("onlinePlayers", message);
    }

    public void close() {
        jedis.close();
    }

    public static void senden(String[] args) {
        RedisPublisher publisher = new RedisPublisher();
        publisher.publishMessage("onlinePlayers", "Hello, Redis!");
        publisher.close();
    }
}
