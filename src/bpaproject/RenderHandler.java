package bpaproject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file RenderHandler.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class RenderHandler {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private final int WIDTH, HEIGHT;
    private BufferedImage view;
    private int[] pixels;

    /**
     * Constructs a RenderHandler with the {@code width} and {@code height} then
     * create a BufferedImage view based off the size. If the {@code width} or
     * {@code height} is less than or equal to 0, the set its value to 1.
     * 
     * @param width  The width
     * @param height The height
     */
    public RenderHandler(int width, int height) {
        if (width > 0)
            this.WIDTH = width;
        else {
            LOGGER.log(Level.WARNING, "RenderHandler width cannot be less than or equal to 0!");
            this.WIDTH = 1;
        }

        if (height > 0)
            this.HEIGHT = height;
        else {
            LOGGER.log(Level.WARNING, "RenderHandler height cannot be less than or equal to 0!");
            this.HEIGHT = 1;
        }

        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    /**
     * Render {@code graphics} to the view
     * 
     * @param g The graphics
     */
    public void render(Graphics g) {
        g.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    /**
     * Renders the {@code image} at {@code x, y} with a scale of {@code zoom}. The
     * image should be in TYPE_INT_ARGB format to render properly.
     * 
     * @param image The image in TYPE_INT_ARGB format
     * @param x     The left-most x coordinate
     * @param y     The top-most y coordinate
     * @param zoom  The scale factor
     */
    public void renderImage(BufferedImage image, int x, int y, int zoom) {
        int[] imgPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        renderArray(imgPixels, image.getWidth(), image.getHeight(), x, y, zoom);
    }

    /**
     * Renders the {@code sprite} at {@code x, y} with a scale of {@code zoom}.
     * 
     * @param sprite The sprite
     * @param x      The left-most x coordinate
     * @param y      The top-most y coordinate
     * @param zoom   The scale factor
     */
    public void renderSprite(Sprite sprite, int x, int y, int zoom) {
        int[] spritePixels = sprite.getPixels();
        if (spritePixels != null)
            renderArray(spritePixels, sprite.getWidth(), sprite.getHeight(), x, y, zoom);
    }

    /**
     * Renders the {@code rect} with a scale of {@code zoom} if it has pixels to
     * render.
     * 
     * @param rect The rect with pixel
     * @param zoom The scale factor
     */
    public void renderRectangle(Rectangle rect, int zoom) {
        int[] rectPixels = rect.getPixels();
        if (rectPixels != null)
            renderArray(rectPixels, (int) rect.getWidth(), (int) rect.getHeight(), rect.x, rect.y, zoom);
    }

    /**
     * Renders the {@code pixels}.
     * <p>
     * Begins rendering at {@code x, y} and moves to the next row after
     * {@code width} pixels. The array is finished rendering after moving rows
     * {@code height} times. Renders each pixel {@code zoom} times.
     * 
     * @param pixels The pixel array
     * @param width  The width
     * @param height The height
     * @param x      The left-most x coordinate
     * @param y      The top-most y coordinate
     * @param zoom   The scale factor
     */
    public void renderArray(int[] pixels, int width, int height, int x, int y, int zoom) {
        for (int yPos = 0; yPos < height; yPos++) {
            for (int xPos = 0; xPos < width; xPos++) {
                for (int yZoom = 0; yZoom < zoom; yZoom++) {
                    for (int xZoom = 0; xZoom < zoom; xZoom++) {
                        setPixel(pixels[xPos + yPos * width], (xPos * zoom) + x + xZoom, (yPos * zoom) + y + yZoom);
                    }
                }
            }
        }
    }

    /**
     * Sets the pixel at {@code x, y} to {@code pixel} color.
     * <p>
     * Gets the alpha of the pixel. If the alpha is 0 then don't set any pixel, if
     * the alpha is 255 then sets the pixel {@code x, y} to the color. Otherwise
     * calculate the combined RGB of the destination pixel and the color and sets
     * the pixels to that out color.
     * 
     * @param color
     * @param x
     * @param y
     */
    private void setPixel(int color, int x, int y) {
        int alpha = ((color >> 24) & 0xFF);
        if (x >= 0 && y >= 0 && x <= WIDTH && y <= HEIGHT && alpha != 0) {
            int index = x + y * view.getWidth();
            if (pixels.length > index) {
                if (alpha == 255) {
                    pixels[x + y * WIDTH] = color;
                    return;
                }
                // 0xAARRGGBB
                int dstPixel = pixels[x + y * WIDTH];
                int outRed = ((dstPixel >> 16) & 0xFF)
                        - (int) ((((dstPixel >> 16) & 0xFF) - ((color >> 16) & 0xFF)) * (alpha / 255f));
                int outGreen = ((dstPixel >> 8) & 0xFF)
                        - (int) ((((dstPixel >> 8) & 0xFF) - ((color >> 8) & 0xFF)) * (alpha / 255f));
                int outBlue = ((dstPixel >> 0) & 0xFF)
                        - (int) ((((dstPixel >> 0) & 0xFF) - ((color >> 0) & 0xFF)) * (alpha / 255f));

                pixels[x + y * WIDTH] = (255 << 24 | outRed << 16 | outGreen << 8 | outBlue << 0);
            }

        }
    }

    /**
     * Loops through the pixels and sets every pixel to the {@code color}.
     * 
     * @param color the color
     */
    public void clear(int color) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = color;
        }
    }
}