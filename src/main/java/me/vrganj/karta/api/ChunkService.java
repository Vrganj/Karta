package me.vrganj.karta.api;

import java.util.Collection;
import java.util.UUID;

public interface ChunkService {

    Collection<UUID> getPlayersSeeingChunk(String world, int x, int z);
}
