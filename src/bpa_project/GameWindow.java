package bpa_project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @file GameWindow.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public class GameWindow extends JFrame implements Runnable {
    public static void main(String[] args) {
        System.loadLibrary("xinput_java");
        try {
            FileHandler fileHandler = new FileHandler(new File("latest.log").getAbsolutePath());
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.setLevel(Level.FINE);
            LOGGER.addHandler(fileHandler);

        } catch (Exception e) {
            // TODO: handle exception
        }

        GameWindow gw = new GameWindow();
        Thread gwThread = new Thread(gw);
        gwThread.start();
    }

    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private static final long serialVersionUID = -2334127122097540833L;
    public static final int ZOOM = 2;
    private static final BufferedImage ERROR_IMAGE = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
    private final int TICKSPERSECOND = 60;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;
    private boolean running;
    private boolean hasContent;
    private WindowContent wc;
    private AudioPlayer player;
    private final KeyboardListener[] listeners = new KeyboardListener[] {
            new KeyboardListener(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE,
                    KeyEvent.VK_E),
            new KeyboardListener(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                    KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT),
            new KeyboardListener(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H, KeyEvent.VK_N,
                    KeyEvent.VK_Y),
            new KeyboardListener(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_PERIOD,
                    KeyEvent.VK_O) };

    public GameWindow() {
        this.setTitle("DynoMite!");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.log(Level.INFO, "Closing game.");
                running = false;
                System.exit(0);
            }
        });

        Graphics g = ERROR_IMAGE.getGraphics();
        int squareWidth = 8;
        for (int y = 0; y < ERROR_IMAGE.getHeight(); y += squareWidth) {
            for (int x = 0; x < ERROR_IMAGE.getWidth(); x += squareWidth) {
                g.setColor(((x + y) / squareWidth) % 2 == 0 ? Color.MAGENTA : Color.BLACK);
                g.fillRect(x, y, squareWidth, squareWidth);
            }
        }

        // TEST CODE BEGIN:
        player = new AudioPlayer();
        MainMenu mm = new MainMenu(this);
        setWindowContent(mm);

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
        LOGGER.log(Level.FINE, "Changing window content to " + wc.getClass().getSimpleName());
        this.hasContent = false;

        this.wc = wc;
        this.setContentPane(wc);
        wc.init();
        this.hasContent = true;

        this.pack();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Formats a file into a BufferedImage of type TYPE_INT_ARGB
     *
     * @param file The image to format
     * @return The formatted image
     */
    public static BufferedImage loadImage(File file, int scale) {
        try {
            if (!file.exists())
                throw new FileNotFoundException("Could not find file: " + file.getAbsolutePath());

            Image loadedImage = ImageIO.read(file);
            loadedImage = loadedImage.getScaledInstance(loadedImage.getWidth(null) * scale,
                    loadedImage.getHeight(null) * scale, Image.SCALE_SMOOTH);
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(null), loadedImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);
            return formattedImage;
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
            return ERROR_IMAGE;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static BufferedImage loadImage(File file) {
        return loadImage(file, 1);
    }

    public KeyboardListener getListener(int playerNum) {
        if (playerNum > listeners.length || playerNum < 0) {
            LOGGER.log(Level.WARNING, "Attempted get get controls for player #" + playerNum);
            return null;
        }
        return listeners[playerNum];
    }

    public AudioPlayer getAudioPlayer() {
        return this.player;
    }
}