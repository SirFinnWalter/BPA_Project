package bpaproject;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file Sprite.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Sprite implements Cloneable {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    protected int width, height;
    protected int[] pixels;
    protected boolean visible;

    @Override
    public Sprite clone() {
        try {
            return (Sprite) super.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw new RuntimeException(ex.getMessage());
        }

    }

    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height) {
        this.width = width;
        this.height = height;
        this.visible = true;
        pixels = new int[width * height];
        sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
    }

    public Sprite() {
    }

    public Sprite(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.visible = true;
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
        if (!visible)
            return null;

        return this.pixels;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}