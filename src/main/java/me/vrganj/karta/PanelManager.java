package me.vrganj.karta;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.vrganj.karta.image.ImageInput;
import me.vrganj.karta.image.ImageKey;
import me.vrganj.karta.image.renderer.Renderer;
import me.vrganj.karta.image.source.ImageSource;
import me.vrganj.karta.panel.NmsPanel;
import me.vrganj.karta.panel.Panel;
import me.vrganj.karta.panel.placement.PanelPlacement;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class PanelManager implements Listener {

    private final Logger logger;
    private final ImageSource imageSource;
    private final Renderer renderer;

    private final Multimap<Long, Panel> panels = ArrayListMultimap.create();

    public PanelManager(Karta plugin, Logger logger, ImageSource imageSource, Renderer renderer) {
        this.logger = logger;
        this.imageSource = imageSource;
        this.renderer = renderer;

        Bukkit.getPluginManager().registerEvents(new PanelListener(panels), plugin);
    }

    public void addDefaultPanel(PanelPlacement placement, ImageKey imageKey) {
        addPanel(new NmsPanel(logger, new ImageInput(imageKey, imageSource, renderer), placement));
    }

    public void addPanel(Panel panel) {
        var location = panel.getPlacement().location();

        int x = location.x() >> 4;
        int z = location.z() >> 4;

        panels.put(Chunk.getChunkKey(x, z), panel);

        // FIXME: WORLD
        var world = Bukkit.getWorlds().getFirst();

        for (var player : world.getPlayersSeeingChunk(x, z)) {
            player.sendRichMessage("<green>Showing <yellow>" + panel);
            panel.show(player);
        }
    }

    /*private void render(Player player, Panel panel) {
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
    }*/
}
