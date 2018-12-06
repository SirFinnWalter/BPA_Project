package bpa_project;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import bpa_project.characters.*;

/**
 * @file BombGame.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */
public class BombGame extends JFrame implements Runnable {

    public static void main(String[] args) {
        // TODO: load library from assets/libs
        System.loadLibrary("xinput_java");
        BombGame game = new BombGame();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    private static final long serialVersionUID = -6739119519499662176L;
    public static final int XZOOM = 2;
    public static final int YZOOM = 2;
    private final int TICKSPERSECOND = 60;
    private final double NANOSECONDS = 1000000000.0 / TICKSPERSECOND;
    public static Tilemap MAP;

    private boolean running = false;
    private Canvas canvas = new Canvas();
    private Set<GameObject> gameObjects = new HashSet<GameObject>();
    private Set<GameObject> gameObjectsBuffer = new HashSet<GameObject>();
    private Set<Player> players = new HashSet<Player>();

    private RenderHandler renderer;
    final ByteBuffer buffer = ByteBuffer.allocateDirect(16);

    /**
     * Creates a default window with a canvas and loads game files. Uses the game
     * files to create the players and map.
     */
    public BombGame() {
        buffer.order(ByteOrder.nativeOrder());

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
        BombGame.MAP = new Tilemap(new File("assets\\maps\\DefaultMap.bm"), tiles);

        Dimension size = new Dimension(BombGame.MAP.getWidth() * 16 * XZOOM, BombGame.MAP.getHeight() * 16 * YZOOM);

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

        this.add(canvas);
        this.setVisible(true);
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        image = loadImage(new File("assets\\sprites\\tempDinosaur.png"));
        sheet = new SpriteSheet(image, 16, 21);

        try {
            AnimatedSprite playerAnimation = new AnimatedSprite(sheet, 8);
            // createPlayer(16 * 1, 16 * 1, (AnimatedSprite) playerAnimation.clone(),
            // new int[] { KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
            // KeyEvent.VK_E, 0 });

            // createPlayer(16 * 23, 16 * 1, (AnimatedSprite) playerAnimation.clone(),
            // new int[] { KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
            // KeyEvent.VK_E });

            // TODO: refine character creation
            image = loadImage(new File("assets\\sprites\\stegowalkTEMP.png"));
            sheet = new SpriteSheet(image, 16, 16);
            playerAnimation = new AnimatedSprite(sheet, 10);

            KeyboardListener listener = new KeyboardListener(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT);
            CharacterB player1 = new CharacterB(16 * 23, 16 * 1, listener);
            canvas.addKeyListener(listener);
            canvas.addFocusListener(listener);
            gameObjects.add(player1);

            KeyboardListener listener2 = new KeyboardListener(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A,
                    KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_E);
            CharacterA player = new CharacterA(16 * 1, 16 * 15, listener2);
            canvas.addKeyListener(listener2);
            canvas.addFocusListener(listener2);
            gameObjects.add(player);

            // createPlayer(16 * 1, 16 * 15, (AnimatedSprite) playerAnimation.clone(),
            // new int[] { KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_F, KeyEvent.VK_H,
            // KeyEvent.VK_Y });
            // createPlayer(16 * 23, 16 * 15, (AnimatedSprite) playerAnimation.clone(),
            // new int[] { KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_L,
            // KeyEvent.VK_O });
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        gameObjects.forEach(object -> {
            if (object instanceof Player)
                players.add((Player) object);
        });
        gameObjectsBuffer.addAll(gameObjects);
    }

    /**
     * Constructs a {@code KeyboardListener} with the specified {@code keys} then
     * uses it to construct a {@code Player} at the specified {@code (x, y)} and a
     * specified sprite.
     * <p>
     * The {@code KeyboardListener} is added to the {@code canvas} and the player is
     * added to the objects buffer.
     */
    public void createPlayer(int x, int y, AnimatedSprite sprite, final int[] keys) {
        if (Player.PLAYER_COUNT >= Player.MAX_PLAYERS)
            System.out.println("Cannot add more than 4 players!");
        if (keys != null && keys.length > 5) {
            KeyboardListener listener = new KeyboardListener(keys[0], keys[1], keys[2], keys[3], keys[4], keys[5]);
            Player player = new Player(x, y, sprite, listener);
            canvas.addKeyListener(listener);
            canvas.addFocusListener(listener);
            gameObjects.add(player);
        } else {
            System.out.println("Not enough keys! (Need up, down, left, right, and action) Could not create player.");
        }
    }

    /**
     * Main game loop - variable frame rate, fixed update rate
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

            for (GameObject object : gameObjectsBuffer) {
                if (!gameObjects.contains(object))
                    object.init(this);
            }
            gameObjects.removeAll(gameObjects);
            gameObjects.addAll(gameObjectsBuffer);

            // Outputs frames/second
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(frames + " fps");
                frames = 0;
            }

            if (players.isEmpty()) {
                update();
                render();
                update();
                render();
                JOptionPane.showMessageDialog(null, "Nobody wins!\nThanks for playing!");
                running = false;
            } else if (players.size() == 1) {
                update();
                render();
                update();
                render();
                JOptionPane.showMessageDialog(null, "Player " + players.iterator().next().getPlayerNum()
                        + " has won by surviving!\nThanks for playing!");
                running = false;
            }
        }
        System.exit(0);
    }

    /**
     * Initalizes game objects
     */
    private void init() {
        gameObjects.forEach(object -> {
            object.init(this);
        });
    }

    private void poll(Player player) {
        XInputButtons buttons = XInputNative.getInput(player.getPlayerNum());
        if (buttons != null) {
            KeyboardListener listener = player.getListener();
            for (int i = 0; i < listener.keyStates.length; i++) {
                listener.keyStates[i] = false;
            }

            if (buttons.up)
                listener.keyStates[0] = true;
            if (buttons.down)
                listener.keyStates[1] = true;
            if (buttons.left)
                listener.keyStates[2] = true;
            if (buttons.right)
                listener.keyStates[3] = true;
            if (buttons.a)
                listener.keyStates[4] = true;
        }
        // System.out.println(XInputNative.getState(0, buffer));

    }

    /**
     * Updates game objects
     */
    private void update() {
        gameObjects.forEach(object -> {
            object.update(this);
            if (object instanceof Player)
                poll((Player) object);

        });

    }

    /**
     * Renders map and game objects to the canvas
     */
    private void render() {
        try {
            BufferStrategy bStrategy = canvas.getBufferStrategy();
            Graphics gfx = bStrategy.getDrawGraphics();
            super.paint(gfx);

            BombGame.MAP.render(renderer, XZOOM, YZOOM);
            gameObjects.forEach(object -> {
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
     * @return The game object buffer
     */
    public Set<GameObject> getGameObjects() {
        return this.gameObjectsBuffer;
    }

    /**
     * Adds a new game object to the game objects buffer.
     * <p>
     * Objects in the buffer are added into the game objects pool after the current
     * update and render is finished.
     * 
     * @param object The game object to be add
     */
    public void addGameObject(GameObject object) {
        gameObjectsBuffer.add(object);
        object.init(this);
    }

    /**
     * Removes a game object from the game objects buffer if present.
     * <p>
     * Objects in the buffer are removed from the game object pool after the current
     * update and render is finished.
     * 
     * @param object The game object to be removed
     */
    public void removeGameObject(GameObject object) {
        gameObjectsBuffer.remove(object);

        if (object instanceof Player)
            players.remove(object);
    }

    /**
     * Checks a collider with the map tiles and other colliders defined in the
     * collider's collision pool.
     * 
     * @param col The collider to check
     */
    public void checkCollision(Collider col) {
        BombGame.MAP.checkCollision(col);
        gameObjects.forEach(object -> {
            if (col.getGameObjects().containsKey(object)) {
                col.checkCollision(object.getCollider());
            }
            // if (object.getCollider() != null && object.getCollider() != source) {
            // source.checkCollision(object.getCollider());
            // }
        });
    }

    // public void startCollision(Collider col) {

    // }

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

    /**
     * @return The running state
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return The set of players
     */
    public Set<Player> getPlayers() {
        return this.players;
    }

    /**
     * @return the map
     */
    // public Tilemap getMap() {
    // return map;
    // }

    /**
     * Sets the state of the game. Setting the game state to {@code false} will
     * close the game by first ending the game loop then closing the window.
     * 
     * @param running The state to set the game state
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}