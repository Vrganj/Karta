package me.vrganj.karta.bukkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.vrganj.karta.api.PanelManager;
import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.panel.Panel;
import me.vrganj.karta.api.panel.PanelAdapter;
import me.vrganj.karta.api.panel.PanelFactory;
import me.vrganj.karta.api.panel.placement.PanelPlacement;
import me.vrganj.karta.api.util.ChunkMap;
import org.bukkit.Bukkit;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BukkitPanelManager implements PanelManager {

    private final File dataFolder;
    private final Logger logger;
    private final PanelFactory panelFactory;
    private final Gson gson;

    private final ChunkMap<Panel> panels = new ChunkMap<>();

    public BukkitPanelManager(Karta plugin, Logger logger, PanelFactory panelFactory) {
        this.dataFolder = plugin.getDataFolder();
        this.logger = logger;
        this.panelFactory = panelFactory;

        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Panel.class, new PanelAdapter(panelFactory))
                .setPrettyPrinting()
                .create();
    }

    @Override
    public ChunkMap<Panel> getPanels() {
        return panels;
    }

    @Override
    public void addDefaultPanel(UUID ownerId, ImageKey imageKey, PanelPlacement placement) {
        addPanel(panelFactory.createPanel(ownerId, imageKey, placement));
    }

    @Override
    public void addPanel(Panel panel) {
        var location = panel.getPlacement().location();
        int x = location.x() >> 4;
        int z = location.z() >> 4;
        var chunk = Bukkit.getWorld(location.world()).getChunkAt(x, z, false);

        panels.add(chunk.getWorld().getName(), chunk.getChunkKey(), panel);

        for (var player : chunk.getPlayersSeeingChunk()) {
            player.sendRichMessage("<green>Showing <yellow>" + panel);
            panel.show(player.getUniqueId());
        }
    }

    @Override
    public void removePanel(Panel panel) {
        var location = panel.getPlacement().location();
        int x = location.x() >> 4;
        int z = location.z() >> 4;
        var chunk = Bukkit.getWorld(location.world()).getChunkAt(x, z, false);

        Set<Panel> set = panels.get(chunk.getWorld().getName(), chunk.getChunkKey());

        if (!set.isEmpty() && set.remove(panel)) {
            for (var player : chunk.getPlayersSeeingChunk()) {
                panel.hide(player.getUniqueId());
            }
        }
    }

    public void load() {
        var file = new File(dataFolder, "panels.json");

        try (var reader = new FileReader(file)) {
            Set<Panel> set = gson.fromJson(reader, new TypeToken<Set<Panel>>() {}.getType());

            for (var panel : set) {
                addPanel(panel);
            }
        } catch (IOException e) {
            logger.error("Failed to load panels", e);
        }
    }

    public void save() {
        var file = new File(dataFolder, "panels.json");

        Set<Panel> set = new HashSet<>();

        for (var world : panels.values()) {
            for (var chunk : world.values()) {
                set.addAll(chunk);
            }
        }

        try (var writer = new FileWriter(file)) {
            gson.toJson(set, writer);
        } catch (IOException e) {
            logger.error("Failed to save panels", e);
        }
    }
}
