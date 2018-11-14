import java.awt.Point;

/**
 * @file Rectangle.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

/**
 * A {@code java.awt.Rectangle} that may have a color, border or both, as well
 * as provides an array of each corner location in a {@code (x,y)} format using
 * {@code java.awt.Point}.
 */
public class Rectangle extends java.awt.Rectangle {
    private static final long serialVersionUID = -8044325233882769813L;
    int pixels[];

    /**
     * Constructs a new {@code Rectangle} whose upper-left corner is specified as
     * {@code (x,y)} and whose width and height are specified by the arguments of
     * the same name.
     * 
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     * @param width  the width of the {@code Rectangle}
     * @param height the height of the {@code Rectangle}
     */
    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Constructs a new {@code Rectangle} from an existing
     * {@code java.awt.Rectangle}.
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
     * Returns a new {@code Rectangle} from the intersection between this
     * {@code Rectangle} and the specified {@code Rectangle}.
     * 
     * @param r The specified {@code Rectangle}
     * @return A {@code Rectangle} from the intersection; or an empty
     *         {@code Rectangle} if the rectangles do not intersect
     */
    public Rectangle intersection(Rectangle r) {
        return new Rectangle(super.intersection(r));
    }

    /**
     * Returns the color pixel. Logs error if no color has been set.
     * 
     * @return the color pixels
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
     * the top-left corner {@code Point} followed by the top-right corner
     * {@code Point} then the bottom-left corner {@code Point} and finally the
     * bottom-right corner {@code Point}.
     * 
     * @return The four points of each corner of the rectangle
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
     * Sets every pixel in {@code pixels} to the specified color in the format of
     * {@code 0xAARRGGBB} where:
     * <ul>
     * <li>{@code AA} is Alpha from 00 to FF</li>
     * <li>{@code RR} is Red from 00 to FF</li>
     * <li>{@code BB} is Blue from 00 to FF</li>
     * <li>{@code GG} is Green from 00 to FF</li>
     * </ul>
     * 
     * @param color The specified color
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
     * Gives the {@code Rectangle} a border with the specified width and sets the
     * {@code pixels} in the range of the border to the specified color in the
     * format of {@code 0xAARRGGBB} where:
     * <ul>
     * <li>{@code AA} is Alpha from 00 to FF</li>
     * <li>{@code RR} is Red from 00 to FF</li>
     * <li>{@code BB} is Blue from 00 to FF</li>
     * <li>{@code GG} is Green from 00 to FF</li>
     * </ul>
     * 
     * @param borderWidth The specified width
     * @param color       The specified color
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