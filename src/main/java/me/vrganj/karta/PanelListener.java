package me.vrganj.karta;

import com.google.common.collect.Multimap;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import me.vrganj.karta.panel.Panel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// TODO: worlds
public class PanelListener implements Listener {

    private final Multimap<Long, Panel> panels;

    public PanelListener(Multimap<Long, Panel> panels) {
        this.panels = panels;
    }

    @EventHandler
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        var player = event.getPlayer();

        for (var panel : panels.get(event.getChunk().getChunkKey())) {
            panel.show(player);
        }
    }

    @EventHandler
    public void onChunkUnload(PlayerChunkUnloadEvent event) {
        var player = event.getPlayer();

        for (var panel : panels.get(event.getChunk().getChunkKey())) {
            panel.hide(player);
        }
    }
}
