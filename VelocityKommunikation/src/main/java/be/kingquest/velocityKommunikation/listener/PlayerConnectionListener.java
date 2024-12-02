package be.kingquest.velocityKommunikation.listener;

import be.kingquest.testredis.redis.packet.model.OnlinePlayerModel;
import be.kingquest.velocityKommunikation.VelocityKommunikation;
import be.kingquest.velocityKommunikation.redis.RedisManager;
import be.kingquest.velocityKommunikation.redis.RedisUtil;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import redis.clients.jedis.Jedis;

public class PlayerConnectionListener {

    public static VelocityKommunikation velocityKommunikation;

    public PlayerConnectionListener(VelocityKommunikation velocityKommunikation) {
        this.velocityKommunikation = velocityKommunikation;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent e) {
        velocityKommunikation.getRedisManager().getRedisClient().addOnlinePlayer(new OnlinePlayerModel(e.getPlayer().getUniqueId(), e.getPlayer().getUsername()));
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent e) {
        velocityKommunikation.getRedisManager().getRedisClient().removeOnlinePlayer(e.getPlayer().getUniqueId());
    }
}
