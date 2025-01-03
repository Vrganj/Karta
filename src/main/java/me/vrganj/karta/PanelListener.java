package me.vrganj.karta;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import me.vrganj.karta.panel.Panel;
import me.vrganj.karta.util.ChunkMap;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PanelListener implements Listener {

    private final ChunkMap<Panel> panels;

    public PanelListener(ChunkMap<Panel> panels) {
        this.panels = panels;
    }

    @EventHandler
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        var player = event.getPlayer();

        for (var panel : panels.get(event.getChunk())) {
            panel.show(player);
        }
    }

    @EventHandler
    public void onChunkUnload(PlayerChunkUnloadEvent event) {
        var player = event.getPlayer();

        for (var panel : panels.get(event.getChunk())) {
            panel.hide(player);
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() instanceof Karta) {
            for (var player : Bukkit.getOnlinePlayers()) {
                for (var chunk : player.getSentChunks()) {
                    for (var panel : panels.get(chunk)) {
                        panel.hide(player);
                    }
                }
            }
        }
    }
}
