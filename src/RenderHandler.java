
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

    public RenderHandler(int width, int height) {
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();

        // for (int y = 0; y < height; y++) {
        // int color = (int) (Math.random() * 0xFFFFFF);
        // for (int x = y * width; x < (y + 1) * width; x++) {
        // pixels[x] = color;
        // }
        // }

    }

    public void render(Graphics g) {
        g.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }

    public void renderImage(BufferedImage image, int xPos, int yPos) {
        int[] imgPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                pixels[(x + xPos) + (y + yPos) * view.getWidth()] = imgPixels[x + y * image.getWidth()];
            }
        }
    }
}