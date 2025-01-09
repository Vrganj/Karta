package me.vrganj.karta.bukkit;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import me.vrganj.karta.api.PanelManager;
import me.vrganj.karta.api.panel.placement.PanelPlacement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PanelListener implements Listener {

    private final PanelManager panelManager;

    public PanelListener(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    @EventHandler
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        var player = event.getPlayer();

        for (var panel : panelManager.getPanels().get(event.getChunk().getWorld().getName(), event.getChunk().getChunkKey())) {
            panel.show(player.getUniqueId());
        }
    }

    @EventHandler
    public void onChunkUnload(PlayerChunkUnloadEvent event) {
        var player = event.getPlayer();

        for (var panel : panelManager.getPanels().get(event.getChunk().getWorld().getName(), event.getChunk().getChunkKey())) {
            panel.hide(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPanelBreak(PlayerUseUnknownEntityEvent event) {
        Player player = event.getPlayer();

        // TODO: optimize

        var panel = panelManager.getPanel(player.getUniqueId(), event.getPlayer().getWorld().getName(), event.getEntityId());

        if (panel == null) {
            return;
        }

        if (player.hasPermission("karta.admin") || panel.getOwnerId().equals(player.getUniqueId())) {
            if (event.isAttack()) {
                panelManager.removePanel(panel);
            } else {
                var placement = new PanelPlacement(panel.getPlacement().location(), panel.getPlacement().face(), panel.getPlacement().rotation().clockwise(), panel.getPlacement().dimensions());
                panelManager.removePanel(panel);
                panelManager.addDefaultPanel(panel.getOwnerId(), panel.getImageInput().imageKey(), placement);
            }
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() instanceof Karta) {
            for (var player : Bukkit.getOnlinePlayers()) {
                for (var chunk : player.getSentChunks()) {
                    for (var panel : panelManager.getPanels().get(chunk.getWorld().getName(), chunk.getChunkKey())) {
                        panel.hide(player.getUniqueId());
                    }
                }
            }
        }
    }
}
