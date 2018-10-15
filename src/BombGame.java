import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * @file BombGame.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class BombGame extends JFrame implements Runnable {
    private static final long serialVersionUID = 1L;
    private final int TICKSPERSECOND = 30;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;

    private boolean running = false;
    private RenderHandler renderer;
    private Canvas canvas = new Canvas();
    private Player player = new Player();
    private KeyboardListener listener = new KeyboardListener();

    private SpriteSheet sheet;
    private Tiles tiles;
    private Map map;

    public BombGame() {
        // TODO: add a custom exit function (close running)
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setBounds(0, 0, 800, 600);
        this.setLocationRelativeTo(null);

        this.add(canvas);
        this.setVisible(true);

        BufferedImage image = loadImage(new File("assets\\sprites\\tileset1.png"));
        sheet = new SpriteSheet(image);
        sheet.loadSprites(32, 32);

        tiles = new Tiles(new File("assets\\maps\\tileset1.bt"), sheet);
        map = new Map(new File("assets\\maps\\map1.bm"), tiles);

        renderer = new RenderHandler(getWidth(), getHeight());
        canvas.createBufferStrategy(3);
        canvas.addKeyListener(listener);
        canvas.addFocusListener(listener);
    }

    public void run() {

        running = true;
        // int frames = 0;
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

            // frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                // System.out.println(frames);
                // frames = 0;
            }
        }
    }

    private void update() {
        player.update(this);
    }

    private void render() {
        BufferStrategy bStrategy = canvas.getBufferStrategy();
        Graphics gfx = bStrategy.getDrawGraphics();
        super.paint(gfx);
        map.render(renderer, 1, 1);
        // renderer.renderImage(testImage, 300, 100, 1, 1);
        player.render(renderer);
        renderer.render(gfx);
        gfx.dispose();
        bStrategy.show();
        renderer.clear(0xFF0000FF);
    }

    // private BufferedImage loadImage(String filepath) {
    // return loadImage(new File(filepath));
    // }

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

    public boolean isRunning() {
        return running;
    }

    public KeyboardListener getListener() {
        return this.listener;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public static void main(String[] args) {
        BombGame game = new BombGame();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

}