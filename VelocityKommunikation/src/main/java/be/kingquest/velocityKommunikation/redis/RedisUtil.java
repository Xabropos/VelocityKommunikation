package be.kingquest.velocityKommunikation.redis;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    private static Jedis jedis;

    // Verbindung zum Redis-Server herstellen
    public static Jedis connect() {
        jedis = new Jedis("89.58.30.136", 6379);  // Redis Server-Adresse und Port
        jedis.auth("Q6yZcoqs1bXWcH6");  // Wenn Redis mit Passwort geschützt ist
        return jedis;
    }

    // Verbindung schließen
    public static Jedis disconnect() {
        if (jedis != null) {
            jedis.close();
        }
        return null;
    }
}
