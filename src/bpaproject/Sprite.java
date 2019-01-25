package bpaproject;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file Sprite.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

/**
 * The class {@code Sprite} is an image converted into a integer pixel array.
 */
public class Sprite implements Cloneable {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private final int WIDTH, HEIGHT;
    private final int[] PIXELS;
    protected boolean visible;

    /**
     * Create a sprite from the {@code sheet} image at {@code x, y} with a size of
     * {@code width} and {@code height}.
     * 
     * @param sheet  The sheet
     * @param x      The x position
     * @param y      the y position
     * @param width  The width
     * @param height The height
     */
    public Sprite(SpriteSheet sheet, int x, int y, int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.visible = true;
        PIXELS = new int[width * height];
        sheet.getImage().getRGB(x, y, width, height, PIXELS, 0, width);
    }

    /**
     * Create a sprite based of the {@code image}. The width and height are copied
     * over from the {@code image}
     * 
     * @param image The image
     */
    public Sprite(BufferedImage image) {
        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();
        this.visible = true;
        PIXELS = new int[WIDTH * HEIGHT];
        image.getRGB(0, 0, WIDTH, HEIGHT, PIXELS, 0, WIDTH);
    }

    /**
     * Creates an empty Sprite with a width and height of 0
     */
    public Sprite() {
        this.WIDTH = 0;
        this.HEIGHT = 0;
        this.PIXELS = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sprite clone() {
        try {
            return (Sprite) super.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            GameWindow.crash("Could not clone sprite.");
            return null;
        }

    }

    /**
     * @return The width of the sprite
     */
    public int getWidth() {
        return this.WIDTH;
    }

    /**
     * @return The height of the sprite
     */
    public int getHeight() {
        return this.HEIGHT;
    }

    /**
     * @return The pixels of the sprite or {@code null} if the sprite is not visible
     */
    public int[] getPixels() {
        if (!visible)
            return null;

        return this.PIXELS;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}