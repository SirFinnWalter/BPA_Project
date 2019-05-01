package bpaproject;

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
import javax.swing.JOptionPane;

import bpaproject.Options.Setting;
import bpaproject.framecontent.*;

/**
 * @file GameWindow.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

/**
 * The class {@code GameWindow} handles the game loop and holds values used
 * throughout the code. Upon closing, it writes the configs to a file and
 * disposes the window.
 */
public class GameWindow extends JFrame implements Runnable {
    public static void main(String[] args) {
        FileHandler fileHandler = null;
        try {
            if (!LOG_FILE.exists())
                LOG_FILE.createNewFile();

            fileHandler = new FileHandler(LOG_FILE.getAbsolutePath());
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOGGER.setLevel(Level.parse(Options.getValue(Setting.LOGGING_LEVEL)));
            LOGGER.addHandler(fileHandler);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            GameWindow.crash("Could not create or open log.");
        }

        new Thread(new GameWindow()).start();
    }

    private static final long serialVersionUID = -2334127122097540833L;

    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final File LOG_FILE = new File("latest.log");
    public static final int ZOOM = Integer.parseInt(Options.getValue(Options.Setting.SCALE).trim());
    private final int TICKSPERSECOND = 60;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;
    private static final BufferedImage ERROR_IMAGE;
    static {
        ERROR_IMAGE = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = ERROR_IMAGE.getGraphics();
        int squareWidth = 8;
        for (int y = 0; y < ERROR_IMAGE.getHeight(); y += squareWidth) {
            for (int x = 0; x < ERROR_IMAGE.getWidth(); x += squareWidth) {
                g.setColor(((x + y) / squareWidth) % 2 == 0 ? Color.MAGENTA : Color.BLACK);
                g.fillRect(x, y, squareWidth, squareWidth);
            }
        }
    }

    private AudioPlayer player = new AudioPlayer();
    private FrameContent fc = new MainMenu(this);

    private volatile boolean running;
    private volatile boolean hasContent;

    /**
     * Controls for 4 players on a keyboard
     */
    private final KeyboardListener[] listeners = new KeyboardListener[] {
            new KeyboardListener(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE,
                    KeyEvent.VK_E),
            new KeyboardListener(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                    KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT),
            new KeyboardListener(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H, KeyEvent.VK_N,
                    KeyEvent.VK_Y),
            new KeyboardListener(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_PERIOD,
                    KeyEvent.VK_O) };

    /**
     * Sets the defaults of the window and sets the content pane to the frame
     * content.
     */
    public GameWindow() {
        try {
            System.loadLibrary("xinput_java");

            this.setTitle("DynoMite!");
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    LOGGER.log(Level.INFO, "Closing game.");
                    hasContent = false;
                    running = false;
                    setVisible(false);
                    try {
                        Options.writeToFile();
                        player.endLoop();

                        synchronized (fc.getGameWindow()) {
                            fc.getGameWindow().wait(2000);
                        }
                    } catch (InterruptedException ex) {
                        LOGGER.log(Level.WARNING, ex.toString(), ex);
                    } finally {
                        dispose();
                    }
                }
            });

            this.hasContent = true;
            this.setContentPane(fc);
            this.pack();
            this.setVisible(true);
            this.toFront();

        } catch (Exception ex) {
            crash(ex);
        }
    }

    /**
     * The main loop. Runs the update() and render() functions of the frame content
     * while it's is running and there is content. Outputs FPS to console.
     */
    public void run() {
        try {
            running = true;
            int frames = 0;
            double delta = 0;
            fc.init();

            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            while (running) {
                while (hasContent) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / NANOSECONDS;
                    lastTime = now;
                    while (delta >= 1) {
                        fc.update();
                        delta--;
                    }
                    fc.render();
                    frames++;
                    if (System.currentTimeMillis() - timer > 1000) {
                        timer += 1000;
                        System.out.println(frames + " fps");
                        frames = 0;
                    }
                }
            }
            synchronized (this) {
                this.notify();
            }
        } catch (Exception ex) {
            crash(ex);
        }
    }

    /**
     * @return The audio player
     */
    public AudioPlayer getAudioPlayer() {
        return this.player;
    }

    /**
     * Gets the keyboard controls for {@code playerNum}. Returns {@code null} if
     * there are no controls for {@code playerNum} or if {@code playerNum} is less
     * than 0
     * 
     * @param playerNum The player number
     * @return The controls for the player number or null if none exist
     */
    public KeyboardListener getListener(int playerNum) {
        if (playerNum > listeners.length || playerNum < 0) {
            LOGGER.log(Level.WARNING, "Attempted to get controls for player #" + playerNum);
            return null;
        }
        return listeners[playerNum];
    }

    public void setFrameContent(FrameContent fc) {
        LOGGER.log(Level.FINE, "Changing frame content to " + fc.getClass().getSimpleName());
        this.hasContent = false;

        this.fc = fc;
        this.setContentPane(fc);
        fc.init();
        this.hasContent = true;

        this.pack();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Formats a file into a BufferedImage of type TYPE_INT_ARGB and scales it by
     * {@code scale}. Returns an error image on any IOException.
     *
     * @param file  The image to format
     * @param scale The scale
     * @return The formatted image or error image on IOException
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
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
            return ERROR_IMAGE;
        }
    }

    /**
     * Formats a file into a BufferedImage of type TYPE_INT_ARGB. Returns an error
     * image on any IOException.
     *
     * @param file The image to format
     * @return The formatted image or error image on IOException
     */
    public static BufferedImage loadImage(File file) {
        return loadImage(file, 1);
    }

    /**
     * Logs the exception, save any options changes, then shows the players the
     * exception message before exiting with error code 1.
     * 
     * @param ex The exception
     */
    public static void crash(Exception ex) {
        LOGGER.log(Level.SEVERE, ex.toString(), ex);

        if (ex.getLocalizedMessage() != null)
            crash(ex.getLocalizedMessage());
        else
            crash(ex.toString());
    }

    /**
     * Logs the exception, save any options changes, then shows the players the
     * message before exiting with error code 1.
     * 
     * @param message The message
     * @param ex      The exception
     */
    public static void crash(String message, Exception ex) {
        LOGGER.log(Level.SEVERE, ex.toString(), ex);
        crash(message);
    }

    /**
     * Logs the message, save any options changes, then shows the players the
     * message before exiting with error code 1.
     * 
     * @param message The message
     */
    public static void crash(String message) {
        LOGGER.log(Level.SEVERE, message);
        Options.writeToFile();
        int response = JOptionPane.showConfirmDialog(null,
                message + "\nReport the log to reports@cognitivethoughtmedia.org?", "Uh oh!", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            // TODO: send log to an email
        }
        System.exit(1);
    }
}