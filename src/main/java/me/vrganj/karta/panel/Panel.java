package me.vrganj.karta.panel;

import me.vrganj.karta.image.ImageInput;
import me.vrganj.karta.panel.placement.PanelPlacement;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface Panel {

    PanelPlacement getPlacement();

    ImageInput getImageInput();

    CompletableFuture<Void> show(Player player);

    void hide(Player player);
}
