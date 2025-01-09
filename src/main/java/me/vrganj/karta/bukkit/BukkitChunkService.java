package me.vrganj.karta.bukkit;

import me.vrganj.karta.api.ChunkService;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class BukkitChunkService implements ChunkService {

    @Override
    public Collection<UUID> getPlayersSeeingChunk(String worldName, int x, int z) {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            return Collections.emptyList();
        }

        Chunk chunk = world.getChunkAt(x, z, false);

        return chunk.getPlayersSeeingChunk().stream().map(Player::getUniqueId).toList();
    }
}
