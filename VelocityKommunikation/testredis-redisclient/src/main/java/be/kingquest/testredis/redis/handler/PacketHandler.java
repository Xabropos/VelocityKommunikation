package be.kingquest.testredis.redis.handler;

import be.kingquest.testredis.redis.Synchronizer;
import be.kingquest.testredis.redis.packet.PacketContainer;
import be.kingquest.testredis.redis.packet.TestPacket;
import lombok.Getter;

import static be.kingquest.testredis.redis.RedisClient.RedisKeys.PACKET_PREFIX;


@Getter
public class PacketHandler extends CustomMessageHandler {

    private final Synchronizer synchronizer;

    public PacketHandler(Synchronizer synchronizer) {
        super(PACKET_PREFIX);
        this.synchronizer = synchronizer;
    }

    @Override
    public void processMessage(String message) {
        final PacketContainer container = PacketContainer.fromMessage(message);
        final TestPacket packet = container.getPacket();
        synchronizer.packedReceived(packet);
    }
}
