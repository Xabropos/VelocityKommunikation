package be.kingquest.velocityKommunikation;

import be.kingquest.velocityKommunikation.listener.PlayerConnectionListener;
import be.kingquest.velocityKommunikation.redis.Manager;
import be.kingquest.velocityKommunikation.redis.RedisManager;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "velocitykommunikation", name = "VelocityKommunikation", version = "1.0", authors = {"[KingQuest]"})
public class VelocityKommunikation {

    public static Logger getLogger() {
        return getLogger();
    }

    @Getter
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;
    public static RedisManager redisManager;

    @Inject
    public VelocityKommunikation(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.redisManager = new RedisManager(this);

    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    public Path dataDirectory() {
        return dataDirectory;
    }

    @Subscribe
    public void onProxyCommand(ProxyInitializeEvent event) {

        CommandMeta banSysMeta = proxyServer.getCommandManager().metaBuilder("onlineplayers")
                .aliases("onlinep")
                .build();
        proxyServer.getCommandManager().register(banSysMeta, new OnlinePlayers(proxyServer));
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new PlayerConnectionListener(this));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        Manager.loadManagers();

        redisManager.getRedisClient().runAfterReady(() -> {
            logger.info("Clearing all online players");
            redisManager.getRedisClient().getOnlinePlayers().forEach(onlinePlayerModel -> {
                redisManager.getRedisClient().removeOnlinePlayer(onlinePlayerModel.getUuid());
            });
            logger.info("Cleared all online players");
        });
    }
}