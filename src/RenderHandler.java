
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

        // pixels[0] = 0xFF0000;
        // pixels[1] = 0x00FF00;
        // pixels[2] = 0x0000FF;

        pixels[width] = 0x00000;

        // pixels[height] = 0xFF0000;
        for (int i = 0; i < width; i++) {
            pixels[i] = 0xFF0000;
        }

        for (int i = width; i < width * 2; i++) {
            pixels[i] = 0x00FF00;
        }

        for (int i = (height - 1) * width; i < (height - 1) * width + width; i++) {
            pixels[i] = 0x00FF00;
        }
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
}