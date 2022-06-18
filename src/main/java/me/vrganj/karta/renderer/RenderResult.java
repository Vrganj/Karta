package me.vrganj.karta.renderer;

public class RenderResult {
    private final byte[] buffer;
    private final int width, height;

    public RenderResult(int width, int height, byte[] buffer) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getBuffer() {
        return buffer;
    }
}
