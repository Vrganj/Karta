package me.vrganj.karta.panel.placement;

public record PanelDimensions(int width, int height) {

    public PanelDimensions {
        if (width < 1) {
            throw new IllegalArgumentException("Width must be positive!");
        }

        if (height < 1) {
            throw new IllegalArgumentException("Height must be positive!");
        }
    }
}
