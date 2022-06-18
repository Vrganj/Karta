package me.vrganj.karta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Karta extends JavaPlugin {
    static final Component PREFIX = MiniMessage.miniMessage().deserialize("<dark_gray>[<#32a86d>Karta</#32a86d>]</dark_gray> ");
    private PanelManager panelManager;

    @Override
    public void onEnable() {
        panelManager = new PanelManager(this);
        Objects.requireNonNull(getCommand("karta")).setExecutor(new KartaCommand(this));
    }

    public PanelManager getPanelManager() {
        return panelManager;
    }
}
