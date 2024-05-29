package com.loefars.igttracker;

import com.loefars.igttracker.client.PlaytimeHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class IGTTrackerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register HUD
        HudRenderCallback.EVENT.register(new PlaytimeHUD());

        ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PlaytimeTimer.loadTimeAsync();
            PlaytimeTimer.start();
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PlaytimeTimer.stop();
            PlaytimeTimer.saveTimeAsync();
        });
    }
}
