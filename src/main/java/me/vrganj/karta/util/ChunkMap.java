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
        var worldValues = get(key.getWorld().getName());

        if (worldValues == null) {
            return Collections.emptySet();
        }

        var chunkValues = worldValues.get(key.getChunkKey());

        if (chunkValues == null) {
            return Collections.emptySet();
        }

        return chunkValues;
    }

    public void add(Chunk key, T value) {
        var worldValues = computeIfAbsent(key.getWorld().getName(), k -> new Long2ObjectOpenHashMap<>());
        var chunkValues = worldValues.computeIfAbsent(key.getChunkKey(), k -> new HashSet<>());
        chunkValues.add(value);
    }
}
