package tutorial;

/**
 * @file Rectangle.java
 * @author Dakota Taylor
 * @createdOn Saturday, 06 October, 2018
 */

public class Rectangle {
    public int x, y, width, height;
    private int[] pixels;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public void generateGraphics(int color) {
        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + y * width] = color;
            }
        }
    }

    public void generateGraphics(int borderWidth, int color) {
        // int[] prevPixels;
        if (pixels == null)
            this.generateGraphics(BombGame.ALPHA);
        // prevPixels = this.getPixels();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y < borderWidth || x < borderWidth || y >= height - borderWidth || x >= width - borderWidth)
                    pixels[x + y * width] = color;
                // else
                // pixels[x + y * width] = prevPixels[x + y * width];
            }
        }
    }

    public int[] getPixels() {
        if (pixels != null)
            return this.pixels;
        else
            System.out.println("Warning: attempted to retrieve Rectangle pixels without generated graphics");
        return null;
    }
}