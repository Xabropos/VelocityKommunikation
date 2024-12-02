package be.kingquest.testredis.redis;


import be.kingquest.testredis.redis.packet.TestPacket;
import be.kingquest.testredis.redis.packet.command.CommandPaket;
import be.kingquest.testredis.redis.packet.test.RedisTestPacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Synchronizer {

    default void packedReceived(TestPacket packet) {
        ignoreException(() -> preHandlePacket(packet));
        try {
            final Method method = getClass().getMethod("handlePacket", packet.getClass());
            method.invoke(this, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            handleCustomPacket(packet);
        }
        ignoreException(() -> postHandlePacket(packet));
    }
    default void preHandlePacket(TestPacket packet) {}

    default void postHandlePacket(TestPacket packet) {}
    default void handleCustomPacket(TestPacket packet) {}

    private void ignoreException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {}
    }

    default void handlePacket(RedisTestPacket teamChatPacket) {}

    default void handlePacket(CommandPaket commandPaket) {}

}
