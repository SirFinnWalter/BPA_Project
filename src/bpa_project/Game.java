package bpa_project;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import bpa_project.characters.Player;

/**
 * @file Game.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public class Game implements WindowContent {

    private Canvas canvas;
    private RenderHandler renderer;
    private Tilemap map;
    Set<Player> players;
    Set<GameObject> gameObjects;
    Set<GameObject> gameObjectsBuffer;
    private boolean running;

    public Game(Canvas canvas, RenderHandler renderer) {
        this.canvas = canvas;
        this.renderer = renderer;
        this.running = true;

        players = new HashSet<>();
        gameObjects = new HashSet<>();
        gameObjectsBuffer = new HashSet<>();
    }

    @Override
    public void init() {
        gameObjects.forEach(object -> {
            object.init(this);
        });
    }

    @Override
    public void update() {
        if (running) {
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
                running = false;

            } else if (players.size() == 1) {
                render();
                JOptionPane.showMessageDialog(null, "Player " + players.iterator().next().getPlayerNum()
                        + " has won by surviving!\nThanks for playing!");
                running = false;
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