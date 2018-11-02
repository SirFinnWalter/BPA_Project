import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @file DynoMite.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 31 October, 2018
 */

public class DynoMite extends JFrame implements Runnable {
    public static void main(String[] args) {
        DynoMite game = new DynoMite();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    private final int TICKSPERSECOND = 30;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;
    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    public boolean running;

    public Rectangle r;
    public Rectangle r2;

    public DynoMite() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                System.exit(0);
            }
        });

        this.setBounds(0, 0, 800, 600);
        this.setLocationRelativeTo(null);

        this.add(canvas);
        this.setVisible(true);

        renderer = new RenderHandler(getWidth(), getHeight());
        canvas.createBufferStrategy(3);
        r = new Rectangle(10, 20, 150, 120);
        BufferedImage image = loadImage(new File("assets\\sprites\\ethanexplosion1.png"));
        r.setImage(image);
        r2 = new Rectangle(300, 20, 150, 120);
        // r.setBorder(5, new Color(255, 255, 255, 180));
        r2.setColor(new Color(0, 255, 0, 140));
        // r.setColor(Color.CYAN);
        // canvas.addKeyListener(listener);
        // canvas.addFocusListener(listener);
    }

    private BufferedImage loadImage(File file) {
        try {
            BufferedImage loadedImage = ImageIO.read(file);
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void run() {
        running = true;
        int frames = 0;
        double delta = 0;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NANOSECONDS;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            render();

            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(frames);
                frames = 0;
            }
        }
    }

    public void render() {
        BufferStrategy bStrategy = canvas.getBufferStrategy();
        Graphics gfx = bStrategy.getDrawGraphics();
        super.paint(gfx);
        renderer.renderRectangle(r, 1, 1);
        renderer.renderRectangle(r2, 1, 1);
        renderer.render(gfx);
        gfx.dispose();
        bStrategy.show();
        renderer.clear(0xFFFF0000);
    }

    public void update() {

    }
}