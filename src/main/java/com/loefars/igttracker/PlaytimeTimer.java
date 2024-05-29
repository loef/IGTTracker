package com.loefars.igttracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class PlaytimeTimer {
    private static long startTime;
    private static long savedTime = 0; // Total saved time
    private static boolean running = false;
    private static final Path SAVE_PATH = Paths.get(System.getProperty("user.home"), ".minecraft", "igttracker");

    public static void start() {
        startTime = System.currentTimeMillis();
        running = true;
        System.out.println("Timer started.");
    }

    public static void stop() {
        if (running) {
            savedTime += System.currentTimeMillis() - startTime;
            running = false;
            System.out.println("Timer stopped.");
            saveTimeAsync();
        }
    }

    public static void saveTimeAsync() {
        CompletableFuture.runAsync(() -> saveTime());
    }

    private static void saveTime() {
        try {
            Files.createDirectories(SAVE_PATH); // Ensure directory exists
            Path filePath = SAVE_PATH.resolve("playtime.txt");
            Files.writeString(filePath, String.valueOf(savedTime));
            System.out.println("Playtime saved successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save playtime: " + e.getMessage());
        }
    }


    public static void loadTimeAsync() {
        CompletableFuture.runAsync(() -> loadTime());
    }

    private static void loadTime() {
        try {
            Path filePath = SAVE_PATH.resolve("playtime.txt");
            if (Files.exists(filePath)) {
                String data = Files.readString(filePath);
                savedTime = Long.parseLong(data);
                System.out.println("Playtime loaded from: " + filePath);
            } else {
                System.out.println("No existing playtime file found at: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to load playtime: " + e.getMessage());
        }
    }
    public static long getElapsedTime() {
        long elapsedTime = running ? (System.currentTimeMillis() - startTime) + savedTime : savedTime;
        System.out.println("Getting elapsed time: " + elapsedTime);
        return elapsedTime;
    }
}
