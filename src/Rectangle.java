/**
 * @file Rectangle.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */
public class Rectangle {
    private int[] pixels;
    private int width, height;
    public int x, y;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        if (pixels != null)
            return pixels;
        else
            System.out.println("Warning: attempted to retrieve Rectangle pixels without setting a color");
        return null;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setColor(int color) {
        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + (y * width)] = color;
            }
        }
    }

    public void setBorder(int borderWidth, int color) {
        // int[] prevPixels;
        if (pixels == null)
            this.setColor(0x00000000);
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
}