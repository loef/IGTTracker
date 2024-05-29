package com.loefars.igttracker;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RegexConstraint;

@Modmenu(modId = "igttracker")
@Config(name = "igttracker-config", wrapperName = "IGTTrackerConfig")
public class PlaytimeConfig {
    @RegexConstraint("[A-Fa-f0-9]{6}")
    public String color = "FF5555";  // Publicly accessible

}
