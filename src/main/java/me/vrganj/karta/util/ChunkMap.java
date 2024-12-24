package me.vrganj.karta.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.Chunk;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChunkMap<T> extends HashMap<String, Long2ObjectMap<Set<T>>> {

    public Set<T> get(Chunk key) {
        var worldPanels = get(key.getWorld().getName());

        if (worldPanels == null) {
            return Collections.emptySet();
        }

        var chunkPanels = worldPanels.get(key.getChunkKey());

        if (chunkPanels == null) {
            return Collections.emptySet();
        }

        return chunkPanels;
    }

    public void add(Chunk key, T value) {
        var worldPanels = computeIfAbsent(key.getWorld().getName(), k -> new Long2ObjectOpenHashMap<>());
        var chunkPanels = worldPanels.computeIfAbsent(key.getChunkKey(), k -> new HashSet<>());
        chunkPanels.add(value);
    }
}
