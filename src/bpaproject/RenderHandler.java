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

    public void render(Graphics g) {
        g.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    public void renderImage(BufferedImage image, int xPos, int yPos, int xZoom, int yZoom) {
        int[] imgPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        renderArray(imgPixels, image.getWidth(), image.getHeight(), xPos, yPos, xZoom, yZoom);
    }

    public void renderSprite(Sprite sprite, int xPos, int yPos, int xZoom, int yZoom) {
        int[] spritePixels = sprite.getPixels();
        if (spritePixels != null)
            renderArray(spritePixels, sprite.getWidth(), sprite.getHeight(), xPos, yPos, xZoom, yZoom);
    }

    public void renderRectangle(Rectangle rect, int xZoom, int yZoom) {
        int[] rectPixels = rect.getPixels();
        if (rectPixels != null)
            renderArray(rectPixels, (int) rect.getWidth(), (int) rect.getHeight(), rect.x, rect.y, xZoom, yZoom);
    }

    public void renderArray(int[] pixels, int width, int height, int xPos, int yPos, int xZoom, int yZoom) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int yZoomPos = 0; yZoomPos < xZoom; yZoomPos++) {
                    for (int xZoomPos = 0; xZoomPos < xZoom; xZoomPos++) {
                        setPixel(pixels[x + y * width], (x * xZoom) + xPos + xZoomPos, (y * yZoom) + yPos + yZoomPos);
                    }
                }
            }
        }
    }

    public int getWidth() {
        return this.WIDTH;
    }

    public int getHeight() {
        return this.HEIGHT;
    }

    private void setPixel(int pixel, int x, int y) {
        int alpha = ((pixel >> 24) & 0xFF);
        if (x >= 0 && y >= 0 && x <= WIDTH && y <= HEIGHT && alpha != 0) {
            int index = x + y * view.getWidth();
            if (pixels.length > index) {
                if (alpha == 255) {
                    pixels[x + y * WIDTH] = pixel;
                    return;
                }
                // 0xAARRGGBB
                int dstPixel = pixels[x + y * WIDTH];
                int outRed = ((dstPixel >> 16) & 0xFF)
                        - (int) ((((dstPixel >> 16) & 0xFF) - ((pixel >> 16) & 0xFF)) * (alpha / 255f));
                int outGreen = ((dstPixel >> 8) & 0xFF)
                        - (int) ((((dstPixel >> 8) & 0xFF) - ((pixel >> 8) & 0xFF)) * (alpha / 255f));
                int outBlue = ((dstPixel >> 0) & 0xFF)
                        - (int) ((((dstPixel >> 0) & 0xFF) - ((pixel >> 0) & 0xFF)) * (alpha / 255f));

                pixels[x + y * WIDTH] = (255 << 24 | outRed << 16 | outGreen << 8 | outBlue << 0);
            }

        }
    }

    public void clear(int color) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = color;
        }
    }
}