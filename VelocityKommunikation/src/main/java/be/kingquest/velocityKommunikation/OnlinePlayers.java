package be.kingquest.velocityKommunikation;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.sql.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static be.kingquest.velocityKommunikation.listener.PlayerConnectionListener.velocityKommunikation;
import static be.kingquest.velocityKommunikation.redis.OnlinePlayerManager.getOnlinePlayers;

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
        List<String> onlinePlayers = getOnlinePlayers();
        source.sendMessage(Component.text("§e" + onlinePlayers));
        source.sendMessage(Component.text(""));


    }
}
