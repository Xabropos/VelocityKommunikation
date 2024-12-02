package be.kingquest.testredis.redis;

import be.kingquest.testredis.redis.connection.RedisCache;
import be.kingquest.testredis.redis.connection.RedisPublisher;
import be.kingquest.testredis.redis.connection.RedisSubscriber;
import be.kingquest.testredis.redis.handler.CustomMessageHandler;
import be.kingquest.testredis.redis.packet.PacketContainer;
import be.kingquest.testredis.redis.packet.TestPacket;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static be.kingquest.testredis.redis.RedisClient.RedisKeys.ONLINE_PLAYERS;

@Getter
public class RedisClient {

    public static final Gson GSON = new Gson();

    private final RedisCache redisCache;
    private final RedisSubscriber redisSubscriber;
    private final RedisPublisher redisPublisher;
    private final String keyPrefix;

    @Builder
    public RedisClient(String host, int port, String user, String password, String clientName, int database, String keyPrefix, String messageChannel, String messageSalt, Synchronizer synchronizer, Logger logger) {
        this.redisCache = new RedisCache(host, port, user, password, clientName, database, logger, keyPrefix);
        this.redisSubscriber = new RedisSubscriber(host, port, user, password, clientName, database, messageChannel, messageSalt, synchronizer, logger);
        this.redisPublisher = new RedisPublisher(host, port, user, password, clientName, database, messageChannel, logger, messageSalt);
        this.keyPrefix = keyPrefix;
    }

    public void close() {
        this.redisCache.close();
        this.redisSubscriber.close();
        this.redisPublisher.close();
    }

    public void sendPacket(TestPacket packet) {
        final PacketContainer packetContainer = new PacketContainer(packet.getClass(), packet);
        publish(RedisKeys.PACKET_PREFIX+packetContainer.toMessage());
    }

    public void addOnlinePlayer(OnlinePlayerModel player) {
        set(ONLINE_PLAYERS, player.getUuid().toString(), player);
    }

    public Optional<OnlinePlayerModel> getOnlinePlayer(UUID uuid) {
        return get(ONLINE_PLAYERS, uuid.toString(), OnlinePlayerModel.class);
    }

    public void removeOnlinePlayer(UUID uuid) {
        redisCache.remove(ONLINE_PLAYERS, uuid.toString());
    }

    public Set<OnlinePlayerModel> getOnlinePlayers() {
        final Set<OnlinePlayerModel> players = new HashSet<>();
        final Set<String> uuids = keys(ONLINE_PLAYERS);
        uuids.stream().map(s -> replacePrefix(ONLINE_PLAYERS, s))
                .forEach(s -> {
                    try {
                        final UUID uuid = UUID.fromString(s);
                        getOnlinePlayer(uuid).ifPresent(players::add);
                    } catch (IllegalArgumentException ignored) {}
                });
        return players;
    }

    public void publish(String message) {
        this.redisPublisher.publish(message);
    }

    public void addCustomMessageHandler(CustomMessageHandler customMessageHandler) {
        this.redisSubscriber.addMessageHandler(customMessageHandler);
    }

    public void set(String key, Object value) {
        this.redisCache.set(key, value);
    }

    public void set(String key, Object value, Type type) {
        this.redisCache.set(key, value, type);
    }

    public void set(String prefix, String key, Object value) {
        this.redisCache.set(prefix, key, value);
    }

    public void set(String prefix, String key, Object value, Type type) {
        this.redisCache.set(prefix, key, value, type);
    }

    public <T> Optional<T> get(String key, Class<T> classOfT) {
        return this.redisCache.get(key, classOfT);
    }

    public <T> Optional<T> get(String key, Type type) {
        return this.redisCache.get(key, type);
    }

    public Optional<String> get(String key) {
        return this.redisCache.get(key);
    }

    public Optional<String> get(String prefix, String key) {
        return this.redisCache.get(prefix, key);
    }

    public <T> Optional<T> get(String prefix, String key, Class<T> classOfT) {
        return this.redisCache.get(prefix, key, classOfT);
    }

    public <T> Optional<T> get(String prefix, String key, Type type) {
        return this.redisCache.get(prefix, key, type);
    }

    public Set<String> keys(String prefix) {
        return this.redisCache.keys(prefix);
    }

    public boolean isReady() {
        return this.redisCache.isReady() && this.redisPublisher.isReady() && this.redisSubscriber.isReady();
    }

    public void awaitReady() {
        final AtomicBoolean ready = new AtomicBoolean(isReady());
        do {
            ready.set(isReady());

        } while (!ready.get());
    }

    public void runAfterReady(Runnable run) {
        new Thread(() -> {
            final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            while (atomicBoolean.get()) {
                atomicBoolean.set(!this.isReady());
            }
            run.run();
        }).start();
    }

    private String replacePrefix(String prefix, String fullKey) {
        return fullKey.replace(keyPrefix+prefix+":", "");
    }

    public static class RedisKeys {
        public static final String PACKET_PREFIX = "king_quest_friend_packet ";

        public static final String ONLINE_PLAYERS = "king_quest_online_players";

    }

}
