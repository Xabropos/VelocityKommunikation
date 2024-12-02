package be.kingquest.velocityKommunikation.redis;

import be.kingquest.testredis.redis.RedisClient;
import be.kingquest.velocityKommunikation.VelocityKommunikation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class Manager {

    @NotNull
    private RedisManager.RedisManagerConfig config;
    private RedisClient redisClient;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Getter
    private static final HashMap<String, Manager> managers = new HashMap<>();
    private static int lowestPriority = 6;

    @Getter
    private int priority = 5;
    protected final VelocityKommunikation velocityKommunikation;
    protected final File file;

    public Manager(VelocityKommunikation velocityKommunikation, String fileName) {
        this.velocityKommunikation = velocityKommunikation;
        file = new File(velocityKommunikation.dataDirectory().toFile().getAbsolutePath(), fileName);
        registerManager(this);
    }

    public abstract void onLoad();

    public void onReload() {
        onLoad();
    }

    protected <T extends ManagerConfig> T getConfigJson(Class<T> managerConfig) {
        try {
            return gson.fromJson(Files.readString(file.toPath(), StandardCharsets.UTF_8), managerConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ManagerConfig managerConfig) {
        try {
            if(!file.exists()) {
                Files.createDirectories(file.getParentFile().toPath());
                Files.createFile(file.toPath());
            }
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
            writer.write(gson.toJson(managerConfig));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    public void log(Level level, String msg, Object... args) {
        if (level.equals(Level.WARNING)) {
            VelocityKommunikation.getLogger().warn("("+getClass().getSimpleName()+"): "+msg, args);
        } else if (level.equals(Level.SEVERE)) {
            VelocityKommunikation.getLogger().error("("+getClass().getSimpleName()+"): "+msg, args);
        } else {
            VelocityKommunikation.getLogger().info("("+getClass().getSimpleName()+"): "+msg, args);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
        if (priority >= lowestPriority) lowestPriority = priority+1;
    }

    public static void loadManagers() {
        loadManagers(null, null);
    }

    public static void loadManagers(Consumer<Manager> beforeLoad, Consumer<ManagerResponse> afterLoading) {
        if (beforeLoad == null) beforeLoad = m -> {};  // Standard-Consumer für "do nothing"
        if (afterLoading == null) afterLoading = r -> {};  // Standard-Consumer für "do nothing"

        for (int i = 0; i < lowestPriority; i++) {
            for (Manager m : managers.values()) {
                if (m.getPriority() == i) {
                    loadManager(m, beforeLoad, afterLoading);
                }
            }
        }
    }

    public static void loadManager(Manager manager, Consumer<Manager> beforeLoad, Consumer<ManagerResponse> afterLoading) {
        long before = System.currentTimeMillis();
        try {
            manager.log("Loading {}...", manager.getClass().getSimpleName());
            beforeLoad.accept(manager);  // Keine Nullprüfung mehr nötig
            manager.onLoad();

            long time = System.currentTimeMillis() - before;
            manager.log("{} loaded after {}ms.", manager.getClass().getSimpleName(), time);
            afterLoading.accept(new ManagerResponse(manager, true, null, time));
        } catch (Exception e) {
            manager.log(Level.SEVERE, "Error while loading {}: {}", manager.getClass().getSimpleName(), e.toString());
        }
    }

    public static void registerManager(Manager m) {
        managers.put(m.getClass().getSimpleName().toLowerCase(), m);
    }

    public abstract void createFile();

    public abstract void readFile();

    public record ManagerResponse(Manager manager, boolean success, Exception exception, long time) {}

    public abstract static class ManagerConfig {

        public abstract void loadDefaults();

    }
}
