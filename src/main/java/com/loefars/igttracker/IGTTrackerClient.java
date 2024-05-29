package com.loefars.igttracker;

import net.fabricmc.api.ClientModInitializer;
import com.loefars.igttracker.client.PlaytimeHUD;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class IGTTrackerClient implements ClientModInitializer {
    public static final com.loefars.igttracker.IGTTrackerConfig CONFIG = com.loefars.igttracker.IGTTrackerConfig.createAndLoad();
    @Override

    public void onInitializeClient() {
        System.out.println("Initializing IGTTrackerClient...");

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            System.out.println("Joined a server or world, setting save path, starting and loading timer...");
            PlaytimeTimer.setSavePath(client);
            PlaytimeTimer.loadTimeAsync();
            PlaytimeTimer.start();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            System.out.println("Disconnected from a server or world, stopping and saving timer...");
            PlaytimeTimer.stop();
        });

        HudRenderCallback.EVENT.register(new PlaytimeHUD());
    }

}
