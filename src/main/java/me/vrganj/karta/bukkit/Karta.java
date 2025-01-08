package me.vrganj.karta.bukkit;

import me.vrganj.karta.api.image.source.FileImageSource;
import me.vrganj.karta.api.renderer.PrimitiveRenderer;
import me.vrganj.karta.bukkit.panel.NmsPanelFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Karta extends JavaPlugin {
    public static final Component PREFIX = MiniMessage.miniMessage().deserialize("<dark_gray>[<#32a86d>Karta</#32a86d>]</dark_gray> ");

    private BukkitPanelManager panelManager;

    @Override
    public void onEnable() {
        var imageSource = new FileImageSource(getDataFolder());
        var renderer = new PrimitiveRenderer();
        this.panelManager = new BukkitPanelManager(this, getSLF4JLogger(), new NmsPanelFactory(getSLF4JLogger(), imageSource, renderer));
        panelManager.load();

        Bukkit.getPluginManager().registerEvents(new PanelListener(panelManager), this);

        Objects.requireNonNull(getCommand("karta")).setExecutor(new KartaCommand(getSLF4JLogger(), panelManager, imageSource));
    }

    @Override
    public void onDisable() {
        panelManager.save();
    }
}
