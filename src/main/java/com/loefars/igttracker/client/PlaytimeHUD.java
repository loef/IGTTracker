package com.loefars.igttracker.client;

import com.loefars.igttracker.IGTTrackerClient;
import com.loefars.igttracker.PlaytimeTimer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class PlaytimeHUD implements HudRenderCallback {
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if (client.world == null) return;

        long playtimeMillis = PlaytimeTimer.getElapsedTime();
        long playtimeSeconds = playtimeMillis / 1000;

        long hours = playtimeSeconds / 3600;
        long minutes = (playtimeSeconds % 3600) / 60;
        long seconds = playtimeSeconds % 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int textWidth = client.textRenderer.getWidth(timeFormatted);

        try {
            int color = Integer.parseInt(IGTTrackerClient.CONFIG.color(), 16);


            drawContext.drawText(client.textRenderer, Text.literal(timeFormatted), 32, screenHeight - 24, color, true);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse color from configuration: " + e.getMessage());
            // Fallback color if parsing fails
            drawContext.drawText(client.textRenderer, Text.literal(timeFormatted), 32, screenHeight - 24, 0xFF5555, true);
        }
    }
}
