package me.vrganj.karta.api.panel.placement;

public enum PanelRotation {
    NONE,
    CLOCKWISE_90,
    CLOCKWISE_180,
    COUNTERCLOCKWISE_90;

    public PanelRotation clockwise() {
        return switch (this) {
            case NONE -> CLOCKWISE_90;
            case CLOCKWISE_90 -> CLOCKWISE_180;
            case CLOCKWISE_180 -> COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90 -> NONE;
        };
    }
}
