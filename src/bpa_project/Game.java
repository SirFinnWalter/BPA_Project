package bpa_project;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.FlowLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import bpa_project.characters.*;
import bpa_project.characters.Player;

/**
 * @file Game.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public class Game extends WindowContent {

    private Canvas canvas;
    private RenderHandler renderer;
    private Tilemap map;
    Set<Player> players;
    Set<GameObject> gameObjects;
    Set<GameObject> gameObjectsBuffer;

    public Canvas getCanvas() {
        return canvas;
    }

    public Game(RenderHandler renderer) {
        this.renderer = renderer;
        this.canvas = new Canvas();
        this.add(canvas);

        players = new HashSet<>();
        gameObjects = new HashSet<>();
        gameObjectsBuffer = new HashSet<>();

        // FIXME: set the size based on the map
        this.setPreferredSize(new Dimension(800, 600));
        canvas.setPreferredSize(new Dimension(800, 600));
        ((FlowLayout) this.getLayout()).setVgap(0);
    }

    @Override
    public void init() {
        super.init();
        gameObjects.forEach(object -> {
            object.init(this);
        });

        ((FlowLayout) this.getLayout()).setVgap(0);

        try {
            KeyboardListener listener1 = new KeyboardListener(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, KeyEvent.VK_SHIFT);
            CharacterC player1 = new CharacterC(16 * 23, 16 * 1, listener1);
            canvas.addKeyListener(listener1);
            canvas.addFocusListener(listener1);
            this.addGameObject(player1);

            KeyboardListener listener2 = new KeyboardListener(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A,
                    KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_E);
            CharacterA player2 = new CharacterA(16 * 1, 16 * 15, listener2);
            canvas.addKeyListener(listener2);
            canvas.addFocusListener(listener2);
            this.addGameObject(player2);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        canvas.createBufferStrategy(3);
        canvas.requestFocus();
    }

    @Override
    public void update() {
        if (isRunning()) {
            gameObjects.forEach(object -> {
                object.update(this);
                if (object instanceof Player)

                    poll((Player) object);
            });

            for (GameObject object : gameObjectsBuffer) {
                if (!gameObjects.contains(object)) {
                    object.init(this);

                    if (object instanceof Player) {
                        players.add((Player) object);
                    }
                }
            }
            gameObjects.removeAll(gameObjects);
            gameObjects.addAll(gameObjectsBuffer);

            if (players.isEmpty()) {
                render();
                JOptionPane.showMessageDialog(null, "Nobody wins!\nThanks for playing!");
                setRunning(false);

            } else if (players.size() == 1) {
                render();
                JOptionPane.showMessageDialog(null, "Player " + players.iterator().next().getPlayerNum()
                        + " has won by surviving!\nThanks for playing!");
                setRunning(false);
            }
        }
    }

    @Override
    public void render() {
        try {
            BufferStrategy bStrategy = canvas.getBufferStrategy();
            Graphics gfx = bStrategy.getDrawGraphics();
            // super.paint(gfx);

            map.render(renderer, GameWindow.ZOOM, GameWindow.ZOOM);
            gameObjects.forEach(object -> {
                object.render(renderer, GameWindow.ZOOM, GameWindow.ZOOM);
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
            if (buttons.b)
                listener.keyStates[5] = true;
        }
        // System.out.println(XInputNative.getState(0, buffer));

    }

    /**
     * @param map the map to set
     */
    public void setMap(Tilemap map) {
        this.map = map;

        // this.setPreferredSize(
        // new Dimension(map.getWidth() * GameWindow.ZOOM * 16, map.getHeight() *
        // GameWindow.ZOOM * 16));
        // canvas.setPreferredSize(
        // new Dimension(map.getWidth() * GameWindow.ZOOM * 16, map.getHeight() *
        // GameWindow.ZOOM * 16));

        System.out.println("Setting map!");
    }

    public Tilemap getMap() {
        return this.map;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

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
        map.checkCollision(col);
        gameObjects.forEach(object -> {
            if (col.getGameObjects().containsKey(object)) {
                col.checkCollision(object.getCollider());
            }
            // if (object.getCollider() != null && object.getCollider() != source) {
            // source.checkCollision(object.getCollider());
            // }
        });
    }

}