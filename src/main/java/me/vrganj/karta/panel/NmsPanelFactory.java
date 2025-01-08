package me.vrganj.karta.panel;

import me.vrganj.karta.image.ImageInput;
import me.vrganj.karta.image.ImageKey;
import me.vrganj.karta.image.renderer.Renderer;
import me.vrganj.karta.image.source.ImageSource;
import me.vrganj.karta.panel.placement.PanelPlacement;
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
