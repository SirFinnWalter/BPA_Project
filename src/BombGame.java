import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
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
    private Set<GameObject> objects = new HashSet<GameObject>();
    private Set<GameObject> objectsBuffer = new HashSet<GameObject>();
    private Set<Player> players = new HashSet<Player>();

    private RenderHandler renderer;
    private Tilemap map;

    /**
     * Creates a default window with a canvas then loads the remaining game files.
     */
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

        renderer = new RenderHandler(getWidth(), getHeight());

        image = loadImage(new File("assets\\sprites\\ghostygoosterwalk.png"));
        sheet = new SpriteSheet(image, 16, 16);

        AnimatedSprite playerAnimation = new AnimatedSprite(sheet, 10);

        KeyboardListener listener = new KeyboardListener();
        Player player = new Player(16 * 8, 32, playerAnimation, listener);
        canvas.addKeyListener(listener);
        canvas.addFocusListener(listener);

        KeyboardListener listener2 = new KeyboardListener(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        KeyboardListener listener3 = new KeyboardListener(KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H,
                KeyEvent.VK_C);
        KeyboardListener listener4 = new KeyboardListener(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L,
                KeyEvent.VK_PERIOD);
        Player player2 = null;
        Player player3 = null;
        Player player4 = null;
        try {
            player2 = new Player(16 * 16, 32, (AnimatedSprite) playerAnimation.clone(), listener2);
            player3 = new Player(16 * 8, 16 * 14, (AnimatedSprite) playerAnimation.clone(), listener3);
            player4 = new Player(16 * 16, 16 * 14, (AnimatedSprite) playerAnimation.clone(), listener4);
        } catch (Exception e) {
        }

        canvas.addKeyListener(listener2);
        canvas.addFocusListener(listener2);
        canvas.addKeyListener(listener3);
        canvas.addFocusListener(listener3);
        canvas.addKeyListener(listener4);
        canvas.addFocusListener(listener4);
        objects.add(player2);
        objects.add(player3);
        objects.add(player4);

        objects.add(player);
        objects.forEach(object -> {
            if (object instanceof Player)
                players.add((Player) object);
        });
        objectsBuffer.addAll(objects);
    }

    /**
     * Game loop - variable frame rate, fixed update rate
     */
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

    /**
     * Initalizes every game object
     */
    private void init() {
        objects.forEach(object -> {
            object.init(this);
        });
    }

    /**
     * Updates every game object state
     */
    private void update() {
        objects.forEach(object -> {
            object.update(this);
        });
    }

    /**
     * Renders map and game objects to screen
     */
    private void render() {
        try {
            BufferStrategy bStrategy = canvas.getBufferStrategy();
            Graphics gfx = bStrategy.getDrawGraphics();
            super.paint(gfx);

            map.render(renderer, XZOOM, YZOOM);
            objects.forEach(object -> {
                object.render(renderer, XZOOM, YZOOM);
            });
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

    /**
     * Returns the game objects buffer
     */
    public Set<GameObject> getGameObjects() {
        return this.objectsBuffer;
    }

    /**
     * Adds a new game object to the game objects buffer. Objects in the buffer are
     * added into the game objects pool after the current update and render is
     * finished.
     * 
     * @param object The game object to be add
     */
    public void addGameObject(GameObject object) {
        objectsBuffer.add(object);
        object.init(this);
    }

    /**
     * Removes a game object from the game objects buffer if present. Objects in the
     * buffer are removed from the game object pool after the current update and
     * render is finished.
     * 
     * @param object The game object to be removed
     */
    public void removeGameObject(GameObject object) {
        objectsBuffer.remove(object);
    }

    /**
     * Checks a collider with the map tiles and other colliders defined in the
     * collider game object pool.
     * 
     * @param source The collider to check.
     */
    public void checkCollision(Collider source) {
        map.checkCollision(source);
        objects.forEach(object -> {
            if (source.getGameObjects().contains(object)) {
                source.checkCollision(object.getCollider());
            }
            // if (object.getCollider() != null && object.getCollider() != source) {
            // source.checkCollision(object.getCollider());
            // }
        });
    }

    /**
     * Formats a file into a BufferedImage TYPE_INT_ARGB.
     * 
     * @param file The image to format.
     * @return The formatted image.
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

    /**
     * Returns the running state.
     * 
     * @return The running state.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the set of players.
     * 
     * @return The set of players.
     */
    public Set<Player> getPlayers() {
        return this.players;
    }

    /**
     * Returns the map.
     * 
     * @return the map.
     */
    public Tilemap getMap() {
        return map;
    }

    /**
     * Sets the state of the game. Setting the game state to false will close the
     * game.
     * 
     * @param running The state to set the game to.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}