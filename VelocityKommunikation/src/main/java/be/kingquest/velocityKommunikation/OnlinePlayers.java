package be.kingquest.velocityKommunikation;

import be.kingquest.testredis.redis.packet.model.OnlinePlayerModel;
import be.kingquest.velocityKommunikation.redis.RedisUtil;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import redis.clients.jedis.Jedis;

import java.sql.Array;
import java.util.Map;
import java.util.Set;

import static be.kingquest.velocityKommunikation.listener.PlayerConnectionListener.velocityKommunikation;

public class OnlinePlayers implements SimpleCommand{

    private final ProxyServer server;

    // Constructor to inject the ProxyServer
    public OnlinePlayers(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        // Alle Field-Wert-Paare ausgeben
        source.sendMessage(Component.text("§eOnline Users:"));
        Set<OnlinePlayerModel> onlinePlayers = velocityKommunikation.getRedisManager().getRedisClient().getOnlinePlayers();
        source.sendMessage(Component.text("§e" + onlinePlayers));
        source.sendMessage(Component.text(""));


    }
}
