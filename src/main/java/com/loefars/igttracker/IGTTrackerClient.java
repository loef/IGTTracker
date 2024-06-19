package com.loefars.igttracker;

import net.fabricmc.api.ClientModInitializer;
import com.loefars.igttracker.client.PlaytimeHUD;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class IGTTrackerClient implements ClientModInitializer {
    public static final com.loefars.igttracker.IGTTrackerConfig CONFIG = com.loefars.igttracker.IGTTrackerConfig.createAndLoad();
    private static KeyBinding toggleHudKey;
    public static boolean hudVisible = true;

    @Override
    public void onInitializeClient() {
        System.out.println("Initializing IGTTrackerClient...");

        PlaytimeTimer.initialize(); // Initialize the timer to set up the tick event listener

        // Register key binding
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.igttracker.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SEMICOLON,
                "category.igttracker"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHudKey.wasPressed()) {
                hudVisible = !hudVisible;
                System.out.println("HUD visibility toggled: " + hudVisible);
            }
        });

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
