package me.vrganj.karta.api.panel;

import it.unimi.dsi.fastutil.ints.IntList;
import me.vrganj.karta.api.image.ImageInput;
import me.vrganj.karta.api.panel.placement.PanelPlacement;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Panel {

    UUID getOwnerId();

    PanelPlacement getPlacement();

    ImageInput getImageInput();

    CompletableFuture<Void> show(UUID playerId);

    void hide(UUID playerId);

    Map<UUID, IntList> getShowing();
}
