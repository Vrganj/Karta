package me.vrganj.karta.api.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChunkMap<T> extends HashMap<String, Long2ObjectMap<Set<T>>> {

    public Set<T> get(String world, long chunkKey) {
        var worldValues = get(world);

        if (worldValues == null) {
            return Collections.emptySet();
        }

        var chunkValues = worldValues.get(chunkKey);

        if (chunkValues == null) {
            return Collections.emptySet();
        }

        return chunkValues;
    }

    public void add(String world, long chunkKey, T value) {
        var worldValues = computeIfAbsent(world, k -> new Long2ObjectOpenHashMap<>());
        var chunkValues = worldValues.computeIfAbsent(chunkKey, k -> new HashSet<>());
        chunkValues.add(value);
    }
}
