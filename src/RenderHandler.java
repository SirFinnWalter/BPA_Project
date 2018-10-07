import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * @file RenderHandler.java
 * @author Dakota Taylor
 * @createdOn Monday, 17 September, 2018
 */

public class RenderHandler {
    private BufferedImage view;
    private Rectangle camera;
    private int[] pixels;

    public RenderHandler(int width, int height) {
        view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        camera = new Rectangle(0, 0, width, height);
        // camera.x = -100;
        // camera.y = -30;

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
        renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPos, yPos, xZoom, yZoom);
    }

    public void renderRectangle(Rectangle rect, int xZoom, int yZoom) {
        int[] rectPixels = rect.getPixels();
        if (rectPixels != null)
            renderArray(rectPixels, rect.width, rect.height, rect.x, rect.y, xZoom, yZoom);

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

    /**
     * Sets the source pixel to the screen pixel
     * 
     * @param pixel The source pixel
     * @param x     The x position of the screen pixel
     * @param y     to y position of the screen pixel
     */
    private void setPixel(int pixel, int x, int y) {
        if (x >= camera.x && y >= camera.y && x <= camera.x + camera.width && y <= camera.y + camera.height) {

            int index = (x - camera.x) + (y - camera.y) * view.getWidth();
            if (pixels.length > index && pixel != BombGame.ALPHA)
                pixels[index] = pixel;
        }
    }
}