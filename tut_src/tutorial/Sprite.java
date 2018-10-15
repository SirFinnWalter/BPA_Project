package tutorial;

import java.awt.image.BufferedImage;

/**
 * @file Sprite.java
 * @author Dakota Taylor
 * @created on Saturday, 06 October, 2018
 */

public class Sprite {
    private int width, height;
    private int[] pixels;

    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
        sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
    }

    public Sprite(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getPixels() {
        return this.pixels;
    }
}