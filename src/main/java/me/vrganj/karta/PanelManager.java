package me.vrganj.karta;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.stream.IntStream;

public class PanelManager implements Listener {
    private final Multimap<Long, Panel> panels = ArrayListMultimap.create();
    private final Karta plugin;

    public PanelManager(Karta plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addPanel(Panel panel) {
        addPanel(panel, true);
    }

    public void addPanel(Panel panel, boolean forceRender) {
        panels.put(panel.location().getChunk().getChunkKey(), panel);

        if (forceRender) {
            // TODO: check whether they're actually in range

            for (var player : Bukkit.getOnlinePlayers()) {
                render(player, panel);
            }
        }
    }

    private void render(Player player, Panel panel) {
        var down = panel.down();
        var right = panel.right();

        int rotation;

        if (down != BlockFace.DOWN) {
            if (down == BlockFace.SOUTH) {
                rotation = 0;
            } else if (down == BlockFace.WEST) {
                rotation = 1;
            } else if (down == BlockFace.NORTH) {
                rotation = 2;
            } else {
                rotation = 3;
            }
        } else {
            rotation = 0;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            var renderer = panel.renderer();
            var render = renderer.render(player);

            int id = panel.hashCode();

            for (int y = 0; y < Math.ceil(renderer.getHeight() / 128.0); ++y) {
                var location = panel.location().clone().add(down.getDirection().clone().multiply(y));

                for (int x = 0; x < Math.ceil(renderer.getWidth() / 128.0); ++x) {
                    int width = Math.min(128, renderer.getWidth() - 128 * x);
                    int height = Math.min(128, renderer.getHeight() - 128 * y);

                    byte[] slice = new byte[width * height];

                    for (int i = 0; i < height; ++i) {
                        for (int j = 0; j < width; ++j) {
                            slice[i * width + j] = render[(i + 128 * y) * renderer.getWidth() + (j + 128 * x)];
                        }
                    }

                    Util.spawnMap(location, player, id++, slice, width, height, Util.toDirection(panel.direction()), rotation);
                    location.add(right.getDirection());
                }
            }
        });
    }

    private void destroy(Player player, Panel panel) {
        var renderer = panel.renderer();
        int width = (int) Math.ceil(renderer.getWidth() / 128.0);
        int height = (int) Math.ceil(renderer.getHeight() / 128.0);
        var range = IntStream.range(panel.hashCode(), panel.hashCode() + width * height);

        var packet = new ClientboundRemoveEntitiesPacket(range.toArray());
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        var player = event.getPlayer();

        for (var panel : panels.get(event.getChunk().getChunkKey())) {
            render(player, panel);
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onChunkUnload(PlayerChunkUnloadEvent event) {
        var player = event.getPlayer();

        for (var panel : panels.get(event.getChunk().getChunkKey())) {
            destroy(player, panel);
        }
    }
}
