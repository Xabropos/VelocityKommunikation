package be.kingquest.velocityKommunikation.redis;
import be.kingquest.velocityKommunikation.VelocityKommunikation;
import lombok.*;
import org.jetbrains.annotations.NotNull;

public class RedisManager extends Manager{

    @NotNull
    private RedisManagerConfig config;

    public RedisManager(VelocityKommunikation velocityKommunikation) {
        super(velocityKommunikation, "redis.json");
        config = new RedisManagerConfig();
    }

    @Override
    public void onLoad() {

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

        @Override
        public void loadDefaults() {
            this.host = "89.58.30.136";
            this.port = 6379;
            this.user = "root";
            this.password = "Q6yZcoqs1bXWcH6";
        }
    }
}
