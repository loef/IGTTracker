package com.loefars.igttracker;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.ui.core.Color;


@Modmenu(modId = "igttracker")
@Config(name = "igttracker-config", wrapperName = "IGTTrackerConfig")
public class PlaytimeConfig {
    //@RegexConstraint("[A-Fa-f0-9]{6}")
    public Color color = Color.ofHsv(0F, 0.666666F, 1F);  // Publicly accessible
    public float scale = 1;
    public int xPos = 24;
    public int yPos = 24;
    public enum Alignment {
        LEFT,
        RIGHT;
    }
    public Alignment align = Alignment.LEFT;
}
