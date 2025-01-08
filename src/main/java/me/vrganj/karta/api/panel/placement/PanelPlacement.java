package me.vrganj.karta.api.panel.placement;

public record PanelPlacement(PanelLocation location, PanelFace face, PanelRotation rotation, PanelDimensions dimensions) {

    public PanelFace down() {
        if (face != PanelFace.UP && face != PanelFace.DOWN) {
            return switch (rotation) {
                case NONE -> PanelFace.DOWN;
                case CLOCKWISE_90 -> face.clockwise();
                case CLOCKWISE_180 -> PanelFace.UP;
                case COUNTERCLOCKWISE_90 -> face.counterclockwise();
            };
        }

        if (face == PanelFace.UP) {
            return switch (rotation) {
                case NONE -> PanelFace.SOUTH;
                case CLOCKWISE_90 -> PanelFace.WEST;
                case CLOCKWISE_180 -> PanelFace.NORTH;
                case COUNTERCLOCKWISE_90 -> PanelFace.EAST;
            };
        }

        return switch (rotation) {
            case NONE -> PanelFace.NORTH;
            case CLOCKWISE_90 -> PanelFace.WEST;
            case CLOCKWISE_180 -> PanelFace.SOUTH;
            case COUNTERCLOCKWISE_90 -> PanelFace.EAST;
        };
    }

    public PanelFace right() {
        if (face != PanelFace.UP && face != PanelFace.DOWN) {
            return switch (rotation) {
                case NONE -> face.counterclockwise();
                case CLOCKWISE_90 -> PanelFace.DOWN;
                case CLOCKWISE_180 -> face.clockwise();
                case COUNTERCLOCKWISE_90 -> PanelFace.UP;
            };
        }

        if (face == PanelFace.UP) {
            return switch (rotation) {
                case NONE -> PanelFace.EAST;
                case CLOCKWISE_90 -> PanelFace.SOUTH;
                case CLOCKWISE_180 -> PanelFace.WEST;
                case COUNTERCLOCKWISE_90 -> PanelFace.NORTH;
            };
        }

        return switch (rotation) {
            case NONE -> PanelFace.EAST;
            case CLOCKWISE_90 -> PanelFace.NORTH;
            case CLOCKWISE_180 -> PanelFace.WEST;
            case COUNTERCLOCKWISE_90 -> PanelFace.SOUTH;
        };
    }
}
