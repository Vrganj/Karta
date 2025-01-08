package me.vrganj.karta;

import me.vrganj.karta.image.renderer.DeprecatedRenderer;
import me.vrganj.karta.image.source.FileImageSource;
import me.vrganj.karta.panel.NmsPanelFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Karta extends JavaPlugin {
    public static final Component PREFIX = MiniMessage.miniMessage().deserialize("<dark_gray>[<#32a86d>Karta</#32a86d>]</dark_gray> ");

    private PanelManager panelManager;

    @Override
    public void onEnable() {
        var imageSource = new FileImageSource(getDataFolder());
        var renderer = new DeprecatedRenderer();
        this.panelManager = new PanelManager(this, getSLF4JLogger(), new NmsPanelFactory(getSLF4JLogger(), imageSource, renderer));
        panelManager.load();

        Objects.requireNonNull(getCommand("karta")).setExecutor(new KartaCommand(getLogger(), panelManager, imageSource));
    }

    @Override
    public void onDisable() {
        panelManager.save();
    }
}
