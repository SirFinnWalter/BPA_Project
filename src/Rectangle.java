import java.awt.Point;

/**
 * @file Rectangle.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Rectangle extends java.awt.Rectangle {
    private static final long serialVersionUID = -8044325233882769813L;
    int pixels[];

    /**
     * Creates a new rectangle.
     * 
     * @param x      The x position on the screen.
     * @param y      The y position on the screen.
     * @param width  The width in pixels.
     * @param height The height in pixels.
     */
    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Creates a rectangle from an existing java Rectangle.
     * 
     * @param r The java Rectangle.
     */
    public Rectangle(java.awt.Rectangle r) {
        super(r.x, r.y, r.width, r.height);
    }

    /**
     * Creates a blank rectangle.
     */
    public Rectangle() {
        super();
    }

    /**
     * Returns the cross-section between this and another rectangle.
     * 
     * @param r The rectangle to cross with this rectangle.
     * @return the cross-section between this and another rectangle.
     */
    public Rectangle intersection(Rectangle r) {
        return new Rectangle(super.intersection(r));
    }

    /**
     * Returns the color pixel array. Will log error if no color has been set.
     * 
     * @return the color pixels array.
     */
    public int[] getPixels() {
        if (pixels != null)
            return pixels;
        else
            System.out.println("Warning: attempted to retrieve Rectangle pixels without setting a color");
        return null;
    }

    /**
     * Returns the four points of each corner of the rectangle. The first element is
     * the top-left corner point, followed by the top-right corner point, then the
     * bottom-left corner point, and finally the bottom-right corner point.
     * 
     * @return The four points of each corner of the rectangle.
     */
    public Point[] getCorners() {
        Point[] p = new Point[4];
        p[0] = new Point(this.x, this.y);
        p[1] = new Point(this.x + this.width - 1, this.y);
        p[2] = new Point(this.x, this.y + this.height - 1);
        p[3] = new Point(this.x + this.width - 1, this.y + this.height - 1);
        return p;
    }

    /**
     * Sets the color of the rectangle.
     * 
     * @param color The int color format to set (0xAARRGGBB).
     */
    public void setColor(int color) {
        pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + (y * width)] = color;
            }
        }
    }

    /**
     * Sets the border of the rectangle.
     * 
     * @param borderWidth The width of the border.
     * @param color       The int color format to set the border (0xAARRGGBB).
     */
    public void setBorder(int borderWidth, int color) {
        if (pixels == null)
            this.setColor(0x00000000);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y < borderWidth || x < borderWidth || y >= height - borderWidth || x >= width - borderWidth)
                    pixels[x + y * width] = color;
            }
        }
    }
}