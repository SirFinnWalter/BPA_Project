import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/*
 * @file RenderHandler.java
 * @author Dakota Taylor
 * @created on 09/17/2018 2:15:20 pm
 * 
 * @updated on 09/20/2018 1:29:31 pm
 * @modified by Dakota Taylor
 */

public class RenderHandler {
    private BufferedImage view;
    private int[] pixels;

    public RenderHandler(int width, int height) {
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
    }

    public void render(Graphics g) {
        // for (int i = 0; i < pixels.length; i++) {
        // pixels[i] = (int) (Math.random() * 0xFFFFFF);
        // }

        g.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
    }
}