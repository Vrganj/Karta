package me.vrganj.karta.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.IntList;
import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.panel.Panel;
import me.vrganj.karta.api.panel.PanelAdapter;
import me.vrganj.karta.api.panel.PanelFactory;
import me.vrganj.karta.api.panel.placement.PanelPlacement;
import me.vrganj.karta.api.util.ChunkMap;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PanelManager {

    private final File dataFolder;
    private final Logger logger;
    private final PanelFactory panelFactory;
    private final ChunkService chunkService;
    private final Gson gson;

    private final ChunkMap<Panel> panels = new ChunkMap<>();

    public PanelManager(File dataFolder, Logger logger, PanelFactory panelFactory, ChunkService chunkService) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.panelFactory = panelFactory;
        this.chunkService = chunkService;

        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Panel.class, new PanelAdapter(panelFactory))
                .setPrettyPrinting()
                .create();
    }

    public ChunkMap<Panel> getPanels() {
        return panels;
    }

    public void addDefaultPanel(UUID ownerId, ImageKey imageKey, PanelPlacement placement) {
        addPanel(panelFactory.createPanel(ownerId, imageKey, placement));
    }

    public void addPanel(Panel panel) {
        var location = panel.getPlacement().location();
        int x = location.x() >> 4;
        int z = location.z() >> 4;

        panels.add(location.world(), ChunkMap.getChunkKey(x, z), panel);
        chunkService.getPlayersSeeingChunk(location.world(), x, z).forEach(panel::show);
    }

    public void removePanel(Panel panel) {
        var location = panel.getPlacement().location();
        int x = location.x() >> 4;
        int z = location.z() >> 4;

        Set<Panel> set = panels.get(location.world(), ChunkMap.getChunkKey(x, z));

        if (!set.isEmpty() && set.remove(panel)) {
            chunkService.getPlayersSeeingChunk(location.world(), x, z).forEach(panel::hide);
        }
    }

    public Panel getPanel(UUID playerId, String world, int entityId) {
        for (var chunk : panels.get(world).values()) {
            for (var panel : chunk) {
                IntList showing = panel.getShowing().get(playerId);

                if (showing != null && showing.contains(entityId)) {
                    return panel;
                }
            }
        }

        return null;
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
