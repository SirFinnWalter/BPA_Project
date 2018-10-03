
/*
 @file RenderHandler.java
 @author Dakota Taylor
 @created on Monday, 17 September, 2018
*/

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class RenderHandler {
    private BufferedImage view;
    private int[] pixels;

    /**
     * Handles renders to the screens
     * 
     * @param width  The width of the screen area
     * @param height The height of the screen area
     */
    public RenderHandler(int width, int height) {
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    /**
     * Draws graphics onto screen
     * 
     * @param g The graphics
     */
    public void render(Graphics g) {
        g.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    /**
     * Renders the image onto the screen with an specified position
     * 
     * @param image The image to render
     * @param xPos  The x position of where to render on screen
     * @param yPos  The y position of where to render on screen
     */
    public void renderImage(BufferedImage image, int xPos, int yPos) {
        int[] imgPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                setPixel(imgPixels[x + y * image.getWidth()], x + xPos, y + yPos);
            }
        }
    }

    /**
     * Renders the image onto the screen with an specified position and specified
     * stretch
     * 
     * @param image The image to render
     * @param xPos  The x position of where to render on screen
     * @param yPos  The y position of where to render on screen
     * @param xZoom The factor to stretch image horizontally
     * @param yZoom The factor to stretch image vertically
     */
    public void renderImage(BufferedImage image, int xPos, int yPos, int xZoom, int yZoom) {
        int[] imgPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int yZoomPos = 0; yZoomPos < xZoom; yZoomPos++) {
                    for (int xZoomPos = 0; xZoomPos < xZoom; xZoomPos++) {
                        setPixel(imgPixels[x + y * image.getWidth()], (x * xZoom) + xPos + xZoomPos,
                                (y * yZoom) + yPos + yZoomPos);
                    }
                }
            }
        }
    }

    /**
     * Sets the source pixel to the screen pixel
     * 
     * @param pixel The source pixel
     * @param x     The x position of the screen pixel
     * @param y     to y position of the screen pixel
     */
    private void setPixel(int pixel, int x, int y) {
        int index = x + y * view.getWidth();
        if (pixels.length > index)
            pixels[index] = pixel;

    }
}