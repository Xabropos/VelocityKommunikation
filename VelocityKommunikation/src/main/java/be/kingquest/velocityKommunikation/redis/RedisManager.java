package be.kingquest.velocityKommunikation.redis;
import be.kingquest.testredis.redis.RedisClient;
import be.kingquest.testredis.utils.Utils;
import be.kingquest.velocityKommunikation.VelocityKommunikation;
import lombok.*;
import org.jetbrains.annotations.NotNull;

public class RedisManager extends Manager{

    @NotNull
    private RedisManagerConfig config;
    @Getter
    private RedisClient redisClient;

    public RedisManager(VelocityKommunikation velocityKommunikation) {
        super(velocityKommunikation, "redis.json");
        config = new RedisManagerConfig();
    }

    @Override
    public void onLoad() {
        createFile();
        readFile();
        if(redisClient != null) {
            this.redisClient.close();
        }
        this.redisClient = RedisClient.builder()
                .host(config.getHost())
                .port(config.getPort())
                .user(config.getUser())
                .password(config.getPassword())
                .clientName(config.getClientName())
                .build();

        velocityKommunikation.runAsync(() -> redisClient.getRedisCache().connect());
        velocityKommunikation.runAsync(() -> redisClient.getRedisPublisher().connect());
        velocityKommunikation.runAsync(() -> redisClient.getRedisSubscriber().connect());

    }


    @Override
    public void createFile() {
        if (!file.exists()) {
            config.loadDefaults();
            save(config);
        }
    }

    @Override
    public void readFile() {
        config = getConfigJson(RedisManagerConfig.class);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisManagerConfig extends Manager.ManagerConfig {

        private String host;
        private int port;
        private String user;
        private String password;
        private String clientName;
        private int database;
        private String keyPrefix;
        private String messageChannel;
        private String messageSalt;
        private String serverName;

        @Override
        public void loadDefaults() {
            this.host = "89.58.30.136";
            this.port = 6379;
            this.user = "root";
            this.password = "Q6yZcoqs1bXWcH6";
            this.clientName = "onlinePlayers";
            this.database = 0;
            this.keyPrefix = "kingquest.";
            this.messageChannel = "kingquest";
            this.messageSalt = Utils.getRandomString(100);
            this.serverName = "velocity1";
        }
    }
}
