package bpaproject;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file SpriteSheet.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class SpriteSheet {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    public final int WIDTH, HEIGHT;
    private BufferedImage image;
    private int[] pixels;

    private int spriteWidth;
    private Sprite[] sprites = null;

    public SpriteSheet(BufferedImage image) {
        this.image = image;
        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        pixels = new int[WIDTH * HEIGHT];
        // pixels = image.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        image.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
    }

    public SpriteSheet(BufferedImage image, int spriteWidth, int spriteHeight) {
        this(image);
        loadSprites(spriteWidth, spriteHeight);
    }

    public void loadSprites(int width, int height) {
        loadSprites(width, height, 0, 0);
    }

    public void loadSprites(int width, int height, int xPad, int yPad) {
        if (width > 0 && height > 0) {
            this.spriteWidth = width;
            sprites = new Sprite[(WIDTH / width) * (HEIGHT / height)];
            int spriteID = 0;
            for (int y = 0; y < HEIGHT; y += height + yPad) {
                for (int x = 0; x < WIDTH; x += width + xPad) {
                    sprites[spriteID++] = new Sprite(this, x, y, width, height);
                }
            }
        } else {
            if (width <= 0)
                LOGGER.log(Level.WARNING, "Attempted to load sprites with a width less than or equal to zero!");

            if (height <= 0)
                LOGGER.log(Level.WARNING, "Attempted to load sprites with a height less than or equal to zero!");
        }
    }

    public Sprite getSprite(int x, int y) {
        return getSprite(x + (y * WIDTH / spriteWidth));
    }

    public Sprite getSprite(int spriteID) {
        if (sprites != null) {
            if (spriteID < sprites.length)
                return sprites[spriteID];
            else
                LOGGER.log(Level.WARNING,
                        "SpriteID of " + spriteID + " is not in the range of " + sprites.length + "!");
        } else
            LOGGER.log(Level.WARNING, "Attempted to retrieve sprites before loading sprites");

        return null;
    }

    public Sprite[] getSprites() {
        if (sprites != null)
            return this.sprites;
        else
            LOGGER.log(Level.WARNING, "Attempted to retrieve sprites before loading sprites");
        return null;
    }

    public BufferedImage getImage() {
        return this.image;
    }

}