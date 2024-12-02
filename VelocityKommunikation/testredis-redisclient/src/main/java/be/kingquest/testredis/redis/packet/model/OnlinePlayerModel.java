package be.kingquest.testredis.redis.packet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlinePlayerModel {

    private UUID uuid;
    private String name;

}
