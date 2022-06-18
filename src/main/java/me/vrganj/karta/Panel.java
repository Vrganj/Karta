package me.vrganj.karta;

import me.vrganj.karta.renderer.Renderer;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public record Panel(Location location, Renderer renderer, BlockFace down, BlockFace right, BlockFace direction) {
}
