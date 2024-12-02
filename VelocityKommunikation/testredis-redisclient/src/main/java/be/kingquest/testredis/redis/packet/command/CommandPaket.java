package be.kingquest.testredis.redis.packet.command;

import be.kingquest.testredis.redis.packet.TestPacket;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommandPaket extends TestPacket {

    private final String playerName;
    private final String command;

    public CommandPaket(String source, String playerName, String command) {
        super(source);
        this.playerName = playerName;
        this.command = command;
    }

}
