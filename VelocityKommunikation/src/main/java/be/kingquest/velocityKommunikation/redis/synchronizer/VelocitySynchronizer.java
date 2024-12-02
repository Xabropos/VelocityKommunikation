package be.kingquest.velocityKommunikation.redis.synchronizer;

import be.kingquest.testredis.redis.Synchronizer;
import be.kingquest.testredis.redis.packet.command.CommandPaket;
import be.kingquest.velocityKommunikation.VelocityKommunikation;

public class VelocitySynchronizer implements Synchronizer {

    private final VelocityKommunikation velocityKommunikation;

    public VelocitySynchronizer(VelocityKommunikation velocityKommunikation) {
        this.velocityKommunikation = velocityKommunikation;
    }

    @Override
    public void handlePacket(CommandPaket commandPaket) {
        velocityKommunikation.getLogger().info("Received command packet: {}", commandPaket);
        velocityKommunikation.getProxyServer().getPlayer(commandPaket.getPlayerName()).ifPresentOrElse(player -> {
            velocityKommunikation.getLogger().info("Executing command {} for player {}", commandPaket.getCommand(), player.getUsername());
            velocityKommunikation.getProxyServer().getCommandManager().executeAsync(player, commandPaket.getCommand());
        }, () -> {
            velocityKommunikation.getLogger().warn("Player {} not found", commandPaket.getPlayerName());
        });
    }
}
