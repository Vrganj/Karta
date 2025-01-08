package me.vrganj.karta.api;

import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.panel.Panel;
import me.vrganj.karta.api.panel.placement.PanelPlacement;

import java.util.UUID;

public interface PanelManager {

    void addPanel(Panel panel);

    void addDefaultPanel(UUID ownerId, ImageKey imageKey, PanelPlacement placement);

    void removePanel(Panel panel);
}
