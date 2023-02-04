package com.fazziclay.janconf.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("JanConf loaded as bukkit-plugin.");
    }
}
