import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @file BombGame.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class BombGame extends JFrame implements Runnable {

    public static void main(String[] args) {
        BombGame game = new BombGame();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    private static final long serialVersionUID = -6739119519499662176L;
    public static final int XZOOM = 2;
    public static final int YZOOM = 2;
    private final int TICKSPERSECOND = 60;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;

    private boolean running = false;
    private Canvas canvas = new Canvas();
    private KeyboardListener listener = new KeyboardListener();

    private RenderHandler renderer;
    private SpriteSheet sheet;
    private Player player;
    // private Player player2;
    // private Player player3;
    private Tileset tiles;
    private Tilemap map;

    public BombGame() {
        this.setTitle("DynoMite");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                System.exit(0);
            }
        });

        BufferedImage image = loadImage(new File("assets\\sprites\\ethanTileset1.png"));
        sheet = new SpriteSheet(image);
        sheet.loadSprites(16, 16);
        tiles = new Tileset(new File("assets\\maps\\ethanTileset1.bt"), sheet);

        BufferedImage image2 = loadImage(new File("assets\\sprites\\bomb.png"));
        SpriteSheet sheet2 = new SpriteSheet(image2);
        sheet2.loadSprites(16, 16);

        AnimatedSprite playerAnimation = new AnimatedSprite(sheet2, 15);
        map = new Tilemap(new File("assets\\maps\\ethanMap1.bm"), tiles);
        player = new Player(18, 18, playerAnimation);
        // player2 = new Player(18, 18, playerAnimation);
        // player2.speedX = 1.5 * BombGame.XZOOM;
        // player2.speedY = 1.5 * BombGame.YZOOM;
        // player3 = new Player(18, 18, playerAnimation);
        // player3.speedX = 2 * BombGame.XZOOM;
        // player3.speedY = 2 * BombGame.YZOOM;
        Dimension size = new Dimension(map.getWidth() * 16 * XZOOM, map.getHeight() * 16 * YZOOM);

        this.setBounds(0, 0, size.width, size.height);
        this.setPreferredSize(size);
        this.pack();
        size.width = size.width + (getWidth() - getContentPane().getWidth());
        size.height = size.height + (getHeight() - getContentPane().getHeight());

        this.setPreferredSize(size);
        this.pack();

        this.setLocationRelativeTo(null);

        this.add(canvas);
        this.setVisible(true);
        canvas.createBufferStrategy(3);
        canvas.addKeyListener(listener);
        canvas.addFocusListener(listener);

        renderer = new RenderHandler(getWidth(), getHeight());
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
                // System.out.println(frames);
                frames = 0;
            }
        }
    }

    private void update() {
        player.update(this);
        // player2.update(this);
        // player3.update(this);
    }

    private void render() {
        try {
            BufferStrategy bStrategy = canvas.getBufferStrategy();
            Graphics gfx = bStrategy.getDrawGraphics();
            super.paint(gfx);

            map.render(renderer, XZOOM, YZOOM);
            player.render(renderer, XZOOM, YZOOM);
            // player2.render(renderer, XZOOM, YZOOM);
            // player3.render(renderer, XZOOM, YZOOM);
            renderer.render(gfx);
            gfx.dispose();
            bStrategy.show();
            renderer.clear(0xFF0000FF);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "Game preformed an illegal operation.\nClosing...", "Uh oh!",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
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

    public boolean isRunning() {
        return running;
    }

    public KeyboardListener getListener() {
        return this.listener;
    }

    public Tilemap getMap() {
        return map;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}