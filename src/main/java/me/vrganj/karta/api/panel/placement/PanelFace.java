package me.vrganj.karta.api.panel.placement;

public enum PanelFace {
    UP,
    DOWN,
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public PanelFace clockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }

    public PanelFace counterclockwise() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }
}
