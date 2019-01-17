package bpa_project;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;

/**
 * @file GameWindow.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public class GameWindow extends JFrame implements Runnable {
    public static void main(String[] args) {
        System.loadLibrary("xinput_java");
        GameWindow gw = new GameWindow();
        Thread gwThread = new Thread(gw);
        gwThread.start();
    }

    private static final long serialVersionUID = -2334127122097540833L;

    public static final int ZOOM = 2;
    private final int TICKSPERSECOND = 60;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;
    private boolean running;
    private boolean hasContent;
    private RenderHandler renderer;
    private WindowContent wc;

    public GameWindow() {
        this.setTitle("DynoMite!");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                System.exit(0);
            }
        });

        // TEST CODE BEGIN:

        MainMenu mm = new MainMenu(this);
        this.setVisible(true);
        this.hasContent = true;
        wc = mm;
        renderer = new RenderHandler(this.getWidth(), this.getHeight());
        // renderer = new RenderHandler(this.getWidth(), this.getHeight());

        this.add(mm);

        // BufferedImage image = loadImage(new
        // File("assets\\tilesets\\RuinsTileset.png"));
        // SpriteSheet sheet = new SpriteSheet(image, 16, 16);
        // Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"),
        // sheet);
        // Tilemap map = new Tilemap(new File("assets\\maps\\DefaultMap.bm"), tiles);
        // setSize(map.getWidth() * 16, map.getHeight() * 16);

        // Game game = new Game(this, renderer);
        // this.add(game);
        // game.setMap(map);

        // this.setVisible(true);
        // wc = game;
        this.pack();

        // TEST CODE END

        this.toFront();
    }

    public void run() {
        running = true;
        int frames = 0;
        double delta = 0;

        wc.init();

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        while (running) {
            while (hasContent) {
                long now = System.nanoTime();
                delta += (now - lastTime) / NANOSECONDS;
                lastTime = now;
                while (delta >= 1) {
                    wc.update();
                    delta--;
                }
                wc.render();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    System.out.println(frames + " fps");
                    frames = 0;
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
        }
        System.exit(0);
    }

    public void setWindowContent(WindowContent wc) {
        this.hasContent = false;

        this.wc.setRunning(false);
        this.remove(this.wc);
        this.wc = wc;

        this.add(this.wc);
        this.wc.init();

        this.hasContent = true;
        SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    public void setSize(int width, int height) {
        Dimension size = new Dimension(width, height);
        setSize(size);
    }

    @Override
    public void setSize(Dimension size) {
        System.out.println(size.width + ", " + size.height);

        size.width *= ZOOM;
        size.height *= ZOOM;

        this.pack();

        this.setBounds(0, 0, size.width, size.height);
        this.setPreferredSize(size);
        this.pack();

        // Sets the size of the window again because the content pane/title bar takes up
        // space from the size. We want the contents of the window to be exactly the
        // width and height.
        size.width = size.width + (getWidth() - getContentPane().getWidth());
        size.height = size.height + (getHeight() - getContentPane().getHeight());
        this.setPreferredSize(size);
        this.pack();
        // this.setLocationRelativeTo(null);

        renderer = new RenderHandler(this.getWidth(), this.getHeight());
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public RenderHandler getRenderHandler() {
        return this.renderer;
    }

    /**
     * Formats a file into a BufferedImage of type TYPE_INT_ARGB
     * 
     * @param file The image to format
     * @return The formatted image
     */
    public static BufferedImage loadImage(File file) {
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

}