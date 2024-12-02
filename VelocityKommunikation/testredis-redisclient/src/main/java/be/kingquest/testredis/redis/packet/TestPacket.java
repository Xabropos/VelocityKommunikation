package be.kingquest.testredis.redis.packet;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestPacket {

    private final String source;

}
