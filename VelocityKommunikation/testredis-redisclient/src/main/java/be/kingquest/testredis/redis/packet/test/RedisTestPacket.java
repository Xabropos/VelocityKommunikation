package be.kingquest.testredis.redis.packet.test;

import be.kingquest.testredis.redis.packet.TestPacket;
import lombok.Getter;

@Getter
public class RedisTestPacket extends TestPacket {

    private final String msg;

    public RedisTestPacket(String source, String msg) {
        super(source);
        this.msg = msg;
    }
}
