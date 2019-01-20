package bpa_project;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @file Sprite.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Sprite {
    public static Sprite voidSprite = new Sprite(GameWindow.loadImage(new File("assets\\tilesets\\VoidTile.png")));
    protected int width, height;
    protected int[] pixels;

    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
        sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
    }

    public Sprite() {
    }

    public Sprite(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();

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