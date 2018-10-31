import java.awt.*;
import java.awt.image.*;

public class Rectangle extends java.awt.Rectangle {
    public BufferedImage image;

    public Rectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setColor(int color) {
        Color c = new Color(color);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx = image.createGraphics();
        gfx.setColor(c);
        gfx.fillRect(0, 0, image.getWidth(), image.getHeight());
        gfx.dispose();
    }

    public void setBorder(int width, int color) {
        if (image == null)
            setColor(0xFF000000);

    }
}