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
    // private BufferedImage testImage;
    // private Sprite testSprite;
    private SpriteSheet sheet;
    private Tiles tiles;
    private Map map;
    private Rectangle testRect = new Rectangle(20, 20, 200, 300);

    public BombGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, 1000, 800);
        setLocationRelativeTo(null);

        add(canvas);
        setVisible(true);

        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        // testRect.generateGraphics(0xFF0000);
        BufferedImage sheetImage = loadImage(new File("assets\\sprites\\bonzi_buddy.png").getAbsolutePath());
        sheet = new SpriteSheet(sheetImage);
        sheet.loadSprites(92, 100);

        tiles = new Tiles(new File("assets\\maps\\SpriteTiles.txt"), sheet);
        map = new Map(new File("assets\\maps\\SpriteMap.txt"), tiles);
        // testImage = loadImage(new
        // File("assets\\sprites\\pikachu.png").getAbsolutePath());
        // testSprite = sheet.getSprite(0, 0);

        testRect.generateGraphics(10, 0xFF9900);
    }

    public void update() {
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

        // renderer.renderImage(testImage, 0, 0, 1, 1);
        renderer.renderRectangle(testRect, 1, 1);
        // renderer.renderSprite(testSprite, 0, 0, 1, 1);
        // tiles.renderTiles(2, renderer, 0, 0, 1, 1);
        map.renderMap(renderer, 1, 1);

        renderer.render(gfx);

        gfx.dispose();
        bStrategy.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60;
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
}
