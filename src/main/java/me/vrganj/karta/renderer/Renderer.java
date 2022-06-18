package me.vrganj.karta.renderer;

import org.bukkit.entity.Player;

public interface Renderer {
    byte[] render(Player player);
    int getWidth();
    int getHeight();
}
