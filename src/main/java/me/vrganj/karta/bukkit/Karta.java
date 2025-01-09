package me.vrganj.karta.bukkit;

import me.vrganj.karta.api.PanelManager;
import me.vrganj.karta.api.image.source.FileImageSource;
import me.vrganj.karta.api.renderer.PrimitiveRenderer;
import me.vrganj.karta.bukkit.panel.NmsPanelFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Karta extends JavaPlugin {
    private PanelManager panelManager;

    @Override
    public void onEnable() {
        var imageSource = new FileImageSource(getDataFolder());
        var renderer = new PrimitiveRenderer();
        this.panelManager = new PanelManager(getDataFolder(), getSLF4JLogger(), new NmsPanelFactory(getSLF4JLogger(), imageSource, renderer), new BukkitChunkService());
        panelManager.load();

        Bukkit.getPluginManager().registerEvents(new PanelListener(panelManager), this);

        Objects.requireNonNull(getCommand("karta")).setExecutor(new KartaCommand(getSLF4JLogger(), panelManager, imageSource));
    }

    @Override
    public void onDisable() {
        panelManager.save();
    }
}
