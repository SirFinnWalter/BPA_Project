import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Canvas;

import java.lang.Runnable;
import java.lang.Thread;

import javax.swing.JFrame;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

/**
 * @file BombGame.java
 * @author Dakota Taylor
 * @createdOn Friday, 14 September, 2018
 */

// TODO: 3:09:03
public class BombGame extends JFrame implements Runnable {

    // NOTE: Any pixel that equal 0xFF00DC will show up as
    // transparent when rendering, even in images
    public static final int ALPHA = 0xFFFF00DC;

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    private SpriteSheet sheet;

    private Rectangle testRect = new Rectangle(10, 0, 350, 100);

    private Tiles tiles;
    private Map map;

    private GameObject[] objects;
    private KeyboardListener kListener = new KeyboardListener();

    private Player player;

    public BombGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, 1000, 800);
        setLocationRelativeTo(null);

        add(canvas);
        setVisible(true);

        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        // testRect.generateGraphics(0xFF0000);
        BufferedImage sheetImage = loadImage(new File("assets\\sprites\\placeholder.png").getAbsolutePath());
        sheet = new SpriteSheet(sheetImage);
        sheet.loadSprites(16, 16);

        tiles = new Tiles(new File("assets\\maps\\TestTiles.txt"), sheet);
        map = new Map(new File("assets\\maps\\TestMap.txt"), tiles);
        // testImage = loadImage(new
        // File("assets\\sprites\\pikachu.png").getAbsolutePath());
        // testSprite = sheet.getSprite(0, 0);

        testRect.generateGraphics(0xFF9900);

        objects = new GameObject[1];
        player = new Player();
        objects[0] = player;

        canvas.addKeyListener(kListener);
        canvas.addFocusListener(kListener);
    }

    public void update() {
        for (int i = 0; i < objects.length; i++) {
            objects[i].update(this);
        }
    }

    private BufferedImage loadImage(String filepath) {
        try {
            BufferedImage loadedImage = ImageIO.read(new File(filepath));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void render() {
        BufferStrategy bStrategy = canvas.getBufferStrategy();
        Graphics gfx = bStrategy.getDrawGraphics();

        super.paint(gfx);
        renderer.renderRectangle(testRect, 2, 2);

        map.render(renderer, 2, 2);
        for (int i = 0; i < objects.length; i++) {
            objects[i].render(renderer, 2, 2);
        }

        renderer.render(gfx);
        gfx.dispose();
        bStrategy.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60; // 60 fps
        double changeInSeconds = 0;
        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now = lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1) {
                update();
                changeInSeconds = 0;
            }
            render();
            lastTime = now;
        }

    }

    public static void main(String[] args) {
        BombGame game = new BombGame();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public KeyboardListener getKeyListener() {
        return this.kListener;
    }
}
