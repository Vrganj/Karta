package me.vrganj.karta.bukkit.panel;

import me.vrganj.karta.api.image.ImageInput;
import me.vrganj.karta.api.image.ImageKey;
import me.vrganj.karta.api.panel.Panel;
import me.vrganj.karta.api.panel.PanelFactory;
import me.vrganj.karta.api.renderer.Renderer;
import me.vrganj.karta.api.image.source.ImageSource;
import me.vrganj.karta.api.panel.placement.PanelPlacement;
import org.slf4j.Logger;

import java.util.UUID;

public class NmsPanelFactory implements PanelFactory {

    private final Logger logger;
    private final ImageSource imageSource;
    private final Renderer renderer;

    public NmsPanelFactory(Logger logger, ImageSource imageSource, Renderer renderer) {
        this.logger = logger;
        this.imageSource = imageSource;
        this.renderer = renderer;
    }

    @Override
    public Panel createPanel(UUID ownerId, ImageKey imageKey, PanelPlacement panelPlacement) {
        return new NmsPanel(logger, ownerId, new ImageInput(imageKey, imageSource, renderer), panelPlacement);
    }
}
