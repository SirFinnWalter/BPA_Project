import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * @file RenderHandler.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class RenderHandler {
    private BufferedImage view;
    private int width, height;
    private int[] pixels;

    public RenderHandler(int width, int height) {
        this.width = width;
        this.height = height;

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

    public void renderRectangle(Rectangle rect, int xZoom, int yZoom) {
        renderImage(rect.image, rect.x, rect.y, xZoom, yZoom);
    }
    // public void renderSprite(Sprite sprite, int xPos, int yPos, int xZoom, int
    // yZoom) {
    // renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPos,
    // yPos, xZoom, yZoom);
    // }

    // public void renderRectangle(Rectangle rect, int xZoom, int yZoom) {
    // int[] rectPixels = rect.getPixels();
    // if (rectPixels != null)
    // renderArray(rectPixels, (int) rect.getWidth(), (int) rect.getHeight(),
    // rect.x, rect.y, xZoom, yZoom);
    // }

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
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private void setPixel(int pixel, int x, int y) {
        int alpha = ((pixel >> 24) & 0xFF);
        if (x >= 0 && y >= 0 && x <= width && y <= height && alpha != 0) {
            int index = x + y * view.getWidth();
            if (pixels.length > index) {
                if (alpha == 255) {
                    pixels[x + y * width] = pixel;
                    return;
                }
                // 0xAARRGGBB
                int dstPixel = pixels[x + y * width];
                int outRed = ((dstPixel >> 16) & 0xFF)
                        - (int) ((((dstPixel >> 16) & 0xFF) - ((pixel >> 16) & 0xFF)) * (alpha / 255f));
                int outGreen = ((dstPixel >> 8) & 0xFF)
                        - (int) ((((dstPixel >> 8) & 0xFF) - ((pixel >> 8) & 0xFF)) * (alpha / 255f));
                int outBlue = ((dstPixel >> 0) & 0xFF)
                        - (int) ((((dstPixel >> 0) & 0xFF) - ((pixel >> 0) & 0xFF)) * (alpha / 255f));

                pixels[x + y * width] = (255 << 24 | outRed << 16 | outGreen << 8 | outBlue << 0);
            }

        }
    }

    public void clear(int color) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = color;
        }
    }
}