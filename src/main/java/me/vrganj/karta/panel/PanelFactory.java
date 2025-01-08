package me.vrganj.karta.panel;

import me.vrganj.karta.image.ImageKey;
import me.vrganj.karta.panel.placement.PanelPlacement;

import java.util.UUID;

public interface PanelFactory {

    Panel createPanel(UUID ownerId, ImageKey imageKey, PanelPlacement panelPlacement);
}
