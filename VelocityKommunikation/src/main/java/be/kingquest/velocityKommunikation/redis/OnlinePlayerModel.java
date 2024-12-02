package be.kingquest.velocityKommunikation.redis;

public class OnlinePlayerModel {
    private String uuid;
    private String name;

    public OnlinePlayerModel(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
