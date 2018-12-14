package bpa_project;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import bpa_project.characters.*;

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
    private Canvas canvas;
    private RenderHandler renderer;
    private WindowContent wc;

    public GameWindow() {
        this.setTitle("DinoMite!");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                System.exit(0);
            }
        });

        canvas = new Canvas();

        // TEST CODE BEGIN:

        BufferedImage image = loadImage(new File("assets\\tilesets\\RuinsTileset.png"));
        SpriteSheet sheet = new SpriteSheet(image, 16, 16);
        Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
        Tilemap map = new Tilemap(new File("assets\\maps\\DefaultMap.bm"), tiles);
        setSize(map.getWidth() * 16, map.getHeight() * 16);
        this.add(canvas);
        this.setVisible(true);
        canvas.createBufferStrategy(3);
        Game game = new Game(canvas, renderer);
        game.setMap(map);

        try {
            KeyboardListener listener1 = new KeyboardListener(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT);
            CharacterB player1 = new CharacterB(16 * 23, 16 * 1, listener1);
            canvas.addKeyListener(listener1);
            canvas.addFocusListener(listener1);
            game.addGameObject(player1);

            KeyboardListener listener2 = new KeyboardListener(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A,
                    KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_E);
            CharacterA player2 = new CharacterA(16 * 1, 16 * 15, listener2);
            canvas.addKeyListener(listener2);
            canvas.addFocusListener(listener2);
            game.addGameObject(player2);

            wc = game;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // TEST CODE END
        this.toFront();
        canvas.requestFocus();
    }

    public void run() {
        running = true;
        int frames = 0;
        double delta = 0;
        wc.init();

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        while (running) {
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
        System.exit(0);
    }

    @Override
    public void setSize(int width, int height) {
        Dimension size = new Dimension(width, height);
        setSize(size);
    }

    @Override
    public void setSize(Dimension size) {
        size.width *= ZOOM;
        size.height *= ZOOM;

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
        this.setLocationRelativeTo(null);

        renderer = new RenderHandler(this.getWidth(), this.getHeight());
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