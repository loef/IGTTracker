package com.loefars.igttracker;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class PlaytimeTimer {
    private static long startTime;
    private static long savedTime = 0; // Total saved time in milliseconds
    private static boolean running = false;
    private static boolean paused = false; // New flag for paused state
    private static Path savePath;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused() && running) {
                pause();
            } else if (!client.isPaused() && paused) {
                resume();
            }
        });
    }

    public static void start() {
        if (!running) {
            startTime = System.currentTimeMillis();
            running = true;
            paused = false;
            System.out.println("Timer started.");
        }
    }

    public static void stop() {
        if (running) {
            long endTime = System.currentTimeMillis();
            savedTime += endTime - startTime; // Accumulate elapsed time into savedTime
            running = false;
            paused = false;
            System.out.println("Timer stopped. Total time saved: " + savedTime / 1000 + " seconds.");
            saveTimeAsync();
        }
    }

    private static void pause() {
        if (running) {
            long pauseTime = System.currentTimeMillis();
            savedTime += pauseTime - startTime;
            paused = true;
            running = false;
            System.out.println("Timer paused. Total time saved: " + savedTime / 1000 + " seconds.");
        }
    }

    private static void resume() {
        if (paused) {
            startTime = System.currentTimeMillis();
            paused = false;
            running = true;
            System.out.println("Timer resumed.");
        }
    }

    private static void saveTimeAsync() {
        CompletableFuture.runAsync(() -> saveTime());
    }

    private static void saveTime() {
        if (savePath == null) {
            System.err.println("Save path is not set. Cannot save playtime.");
            return;
        }
        try {
            Files.createDirectories(savePath.getParent()); // Ensure directory exists
            Files.writeString(savePath, String.valueOf(savedTime));
            System.out.println("Playtime saved successfully at: " + savePath);
        } catch (IOException e) {
            System.err.println("Failed to save playtime: " + e.getMessage());
        }
    }

    public static void loadTimeAsync() {
        CompletableFuture.runAsync(() -> loadTime());
    }

    private static void loadTime() {
        if (savePath == null) {
            System.err.println("Save path is not set. Cannot load playtime.");
            return;
        }
        try {
            if (Files.exists(savePath)) {
                String data = Files.readString(savePath);
                savedTime = Long.parseLong(data);
                System.out.println("Playtime loaded from: " + savePath + ", total " + savedTime / 1000 + " seconds.");
            } else {
                savedTime = 0;
                System.out.println("No existing playtime file found at: " + savePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to load playtime: " + e.getMessage());
        }
    }

    public static long getElapsedTime() {
        return running ? (System.currentTimeMillis() - startTime) + savedTime : savedTime;
    }

    public static void setSavePath(MinecraftClient client) {
        if (client.isIntegratedServerRunning()) {
            // Singleplayer world
            IntegratedServer server = client.getServer();
            if (server != null) {
                // Get the world folder name
                String worldFolder = client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
                savePath = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "igttracker", "sp-" + worldFolder + ".txt");
            }
        } else if (client.getCurrentServerEntry() != null) {
            // Multiplayer server
            String serverAddress = client.getCurrentServerEntry().address.replace("/", "").replace(":", "-");
            savePath = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "igttracker", "mp-" + serverAddress + ".txt");
        } else {
            savePath = null;
        }
        System.out.println("Save path set to: " + savePath);
    }
}
