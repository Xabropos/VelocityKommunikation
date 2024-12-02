package be.kingquest.velocityKommunikation.redis;

import redis.clients.jedis.JedisPooled;

import java.util.List;

public class OnlinePlayerManager {
    private static JedisPooled jedis;

    public static JedisPooled RedisManagerLoadData() {
        String host = "89.58.30.136";
        int port = 6379;
        String user = "root"; // Optional, falls ACL verwendet wird
        String password = "Q6yZcoqs1bXWcH6";

        // Verbindung herstellen
         return jedis = new JedisPooled(host, port, user, password);
    }

    public OnlinePlayerManager(JedisPooled jedis) {
        this.jedis = jedis;
    }

    public static void addPlayer(String playerName) {
        jedis.rpush("OnlineSpieler", playerName);
    }

    public static void removePlayer(String playerName) {
        jedis.lrem("OnlineSpieler", 1, playerName);
    }

    public static List<String> getOnlinePlayers() {
        return jedis.lrange("OnlineSpieler", 0, -1);
    }
}