package be.kingquest.velocityKommunikation.listener;

import be.kingquest.velocityKommunikation.VelocityKommunikation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import static be.kingquest.velocityKommunikation.redis.OnlinePlayerManager.*;

public class PlayerConnectionListener {

    public static VelocityKommunikation velocityKommunikation;

    public PlayerConnectionListener(VelocityKommunikation velocityKommunikation) {
        this.velocityKommunikation = velocityKommunikation;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent e) {
        addPlayer(e.getPlayer().getUsername());
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent e) {
        removePlayer(e.getPlayer().getUsername());
    }
}
