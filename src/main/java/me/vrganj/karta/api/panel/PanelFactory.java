package me.vrganj.karta.api.panel;

import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.panel.placement.PanelPlacement;

import java.util.UUID;

public interface PanelFactory {

    Panel createPanel(UUID ownerId, ImageKey imageKey, PanelPlacement panelPlacement);
}
