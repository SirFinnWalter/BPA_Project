import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
    private Set<GameObject> objects = new HashSet<GameObject>();
    private Set<GameObject> objectsBuffer = new HashSet<GameObject>();
    private Set<Player> players = new HashSet<Player>();

    private RenderHandler renderer;
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

        BufferedImage image = loadImage(new File("assets\\tilesets\\RuinsTileset.png"));
        SpriteSheet sheet = new SpriteSheet(image, 16, 16);
        Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
        map = new Tilemap(new File("assets\\maps\\RuinMap.bm"), tiles);

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

        image = loadImage(new File("assets\\sprites\\ghostygoosterwalk.png"));
        sheet = new SpriteSheet(image, 16, 16);

        AnimatedSprite playerAnimation = new AnimatedSprite(sheet, 10);
        Player player = new Player(16, 16, playerAnimation);

        objects.add(player);
        objects.forEach(object -> {
            if (object instanceof Player)
                players.add((Player) object);
        });
        objectsBuffer.addAll(objects);

    }

    public void run() {
        running = true;
        int frames = 0;
        double delta = 0;
        init();

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
            objects.removeAll(objects);
            objects.addAll(objectsBuffer);

            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(frames + " fps");
                frames = 0;
            }
        }
    }

    private void init() {
        objects.forEach(object -> {
            object.init(this);
        });
    }

    private void update() {
        objects.forEach(object -> {
            object.update(this);
        });
    }

    private void render() {
        try {
            BufferStrategy bStrategy = canvas.getBufferStrategy();
            Graphics gfx = bStrategy.getDrawGraphics();
            super.paint(gfx);

            map.render(renderer, XZOOM, YZOOM);
            objects.forEach(object -> {
                object.render(renderer, XZOOM, YZOOM);
            });
            // player.render(renderer, XZOOM, YZOOM);
            // bomb.render(renderer, XZOOM, YZOOM);
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

    public void addGameObject(GameObject object) {
        objectsBuffer.add(object);
    }

    public void removeGameObject(GameObject object) {
        objectsBuffer.remove(object);
    }

    public void checkCollision() {
        objects.forEach(object -> {
            if (object.getCollider() != null) {
                map.checkCollision(object.getCollider());
                if (!(object instanceof Player)) {
                    players.forEach(player -> {
                        object.getCollider().checkCollision(player.getCollider());
                    });
                }
            }
        });
    }

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

    public boolean isRunning() {
        return running;
    }

    public Set<Player> getPlayers() {
        return this.players;
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