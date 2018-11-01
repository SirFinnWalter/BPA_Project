import java.awt.*;
import java.awt.image.*;

/**
 * @file Rectangle.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 31 October, 2018
 */

public class Rectangle extends java.awt.Rectangle {
    public BufferedImage image;
    private int borderWidth = 0;

    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Point[] getCorners() {
        Point[] corners = new Point[4];
        corners[0] = new Point(x, y);
        corners[1] = new Point(x + width, y);
        corners[2] = new Point(x, y + height);
        corners[3] = new Point(x + width, y + height);
        return corners;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public void setImage(BufferedImage image) {
        if (this.image == null)
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics gfx = this.image.createGraphics();
        gfx.setClip(0, 0, this.image.getWidth(), this.image.getHeight());
        gfx.drawImage(image, 0, 0, null);
        gfx.dispose();
    }

    public void setColor(Color c) {
        // Color c = new Color(color);
        if (image == null)
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx = image.createGraphics();
        gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, c.getAlpha() / 255.0f));
        gfx.setColor(c);
        gfx.fillRect(borderWidth, borderWidth, image.getWidth() - 2 * borderWidth, image.getHeight() - 2 * borderWidth);
        gfx.dispose();
    }

    public void setBorder(int width, Color c) {
        if (image == null)
            setColor(new Color(255, 255, 255, 0));
        borderWidth = width;
        // Color c = new Color(color);
        Graphics2D gfx = image.createGraphics();
        gfx.setColor(c);
        gfx.fillRect(0, width, width, image.getHeight() - 2 * width);
        gfx.fillRect(0, 0, image.getWidth(), width);
        gfx.fillRect(image.getWidth() - width, width, width, image.getHeight() - 2 * width);
        gfx.fillRect(0, image.getHeight() - width, image.getWidth(), width);
        gfx.dispose();
    }
}