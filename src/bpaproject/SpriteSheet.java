package bpaproject;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file SpriteSheet.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

/**
 * The class {@code SpriteSheet} is an image that can be sliced into a
 * {@code Sprite} array.
 * <p>
 * The {@code SpriteSheet} can retrieve any sprite in the array by a index or an
 * {@code x, y} position from the sliced grid.
 */
public class SpriteSheet {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    public final int WIDTH, HEIGHT;
    private BufferedImage image;
    // private int[] pixels;

    private int spriteWidth;
    private Sprite[] sprites = null;

    /**
     * Constructs a new {@code SpriteSheet} from the {@code image}. The width and
     * height are set to the image width and height.
     * 
     * @param image The image
     */
    public SpriteSheet(BufferedImage image) {
        this.image = image;
        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();
    }

    /**
     * Constructs a new {@code SpriteSheet} from the {@code image}. The width and
     * height are set to the image width and height and the pixel array is retrieved
     * the image RGB pixels.
     * <p>
     * Then loads the sprites in with the size of {@code spriteWidth} and
     * {@code spriteHeight}.
     * 
     * @param image        The image
     * @param spriteWidth  The width of each sprite
     * @param spriteHeight The height of each sprite
     */
    public SpriteSheet(BufferedImage image, int spriteWidth, int spriteHeight) {
        this(image);
        loadSprites(spriteWidth, spriteHeight);
    }

    /**
     * Constructs the sprite array from the image. The image is sliced into a grid
     * of {@code width} columns and {@code height} rows with no padding between each
     * grid. The sprites are constructed from each image in the grid.
     * <p>
     * Both the {@code width} and {@code height} must greater than 0 to load
     * sprites.
     * 
     * @param width  The width of the sprite
     * @param height The height of the sprite
     * @param xPad   The width padding between images
     * @param yPad   The height padding between images
     */
    public void loadSprites(int width, int height) {
        loadSprites(width, height, 0, 0);
    }

    /**
     * Constructs the sprite array from the image. The image is sliced into a grid
     * of {@code width} columns and {@code height} rows with {@code xPad} width
     * padding and {@code yPad} height padding between each grid. The sprites are
     * constructed from each image in the grid.
     * <p>
     * Both the {@code width} and {@code height} must greater than 0 to load
     * sprites.
     * 
     * @param width  The width of the sprite
     * @param height The height of the sprite
     * @param xPad   The width padding between images
     * @param yPad   The height padding between images
     */
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

    /**
     * @param x The x grid coordinate
     * @param y The y grid coordinate
     * @return The sprite at {@code x, y} if sprites are loaded and if it exist
     */
    public Sprite getSprite(int x, int y) {
        return getSprite(x + (y * WIDTH / spriteWidth));
    }

    /**
     * @param spriteID The sprite index
     * @return The sprite at the sprite index if sprites are loaded and if it exist
     */
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

    /**
     * @return The sprite array if sprites are loaded
     */
    public Sprite[] getSprites() {
        if (sprites != null)
            return this.sprites;
        else
            LOGGER.log(Level.WARNING, "Attempted to retrieve sprites before loading sprites");
        return null;
    }

    /**
     * @return The image
     */
    public BufferedImage getImage() {
        return this.image;
    }

}