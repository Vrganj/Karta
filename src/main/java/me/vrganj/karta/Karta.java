package me.vrganj.karta;

import me.vrganj.karta.image.renderer.DeprecatedRenderer;
import me.vrganj.karta.image.source.FileImageSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Karta extends JavaPlugin {
    public static final Component PREFIX = MiniMessage.miniMessage().deserialize("<dark_gray>[<#32a86d>Karta</#32a86d>]</dark_gray> ");

    @Override
    public void onEnable() {
        var imageSource = new FileImageSource(getDataFolder());
        var renderer = new DeprecatedRenderer();
        var panelManager = new PanelManager(this, getLogger(), imageSource, renderer);

        Objects.requireNonNull(getCommand("karta")).setExecutor(new KartaCommand(getLogger(), panelManager, imageSource));
    }
}
