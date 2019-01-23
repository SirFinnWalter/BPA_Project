package bpaproject.framecontent;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import bpaproject.characters.Player;

import bpaproject.*;

/**
 * @file Game.java
 * @author Dakota Taylor
 * @createdOn Thursday, 13 December, 2018
 */

public class Game extends FrameContent {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final long serialVersionUID = 3570399387338681022L;

    private Canvas canvas;
    private RenderHandler renderer;
    private Tilemap map;
    private boolean linkSuccessful;
    Set<Player> players;
    Set<GameObject> gameObjects;
    Set<GameObject> gameObjectsBuffer;

    public Canvas getCanvas() {
        return canvas;
    }

    public Game(GameWindow gw) {
        super(gw);
        this.canvas = new Canvas();
        this.add(canvas);

        try {
            XInputNative.getInput(0);
            linkSuccessful = true;
        } catch (UnsatisfiedLinkError ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        }

        // System.out.println(renderer.getWidth() + ", " + renderer.getHeight());
        players = new HashSet<>();
        gameObjects = new HashSet<>();
        gameObjectsBuffer = new HashSet<>();
        Player.PLAYER_COUNT = 0;
    }

    @Override
    public void init() {
        super.init();

        gameObjects.forEach(object -> {
            object.init(this);
        });

        LOGGER.log(Level.FINER, "Game canvas creating buffer strategy.");
        canvas.createBufferStrategy(3);
        canvas.requestFocus();
    }

    @Override
    public void update() {
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
            LOGGER.log(Level.INFO, "Game ended with nobody winning.");
            MainMenu mm = new MainMenu(getGameWindow());
            getGameWindow().setWindowContent(mm);

        } else if (players.size() == 1) {
            render();
            int playerNum = players.iterator().next().getPlayerNum();
            JOptionPane.showMessageDialog(null, "Player " + playerNum + " has won!\nThanks for playing!");
            LOGGER.log(Level.INFO, "Game ended with " + "player #" + playerNum + " winning.");
            MainMenu mm = new MainMenu(getGameWindow());
            getGameWindow().setWindowContent(mm);

        }
    }

    @Override
    public void render() {
        try {
            BufferStrategy bStrategy = canvas.getBufferStrategy();
            if (bStrategy == null)
                return;

            Graphics gfx = bStrategy.getDrawGraphics();

            map.render(renderer, GameWindow.ZOOM, GameWindow.ZOOM);
            gameObjects.forEach(object -> {
                object.render(renderer, GameWindow.ZOOM, GameWindow.ZOOM);
            });
            renderer.render(gfx);
            gfx.dispose();
            bStrategy.show();
            renderer.clear(0xFF000000);
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw ex;
        } catch (NullPointerException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
        }
    }

    private void poll(Player player) {
        if (linkSuccessful) {
            XInputButtons buttons = XInputNative.getInput(player.getPlayerNum() - 1);
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
        }
    }

    /**
     * @param map the map to set
     */
    public void setMap(Tilemap map) {
        LOGGER.log(Level.FINE, "Setting game map.");

        this.map = map;
        canvas.setPreferredSize(
                new Dimension(map.getWidth() * GameWindow.ZOOM * 16, map.getHeight() * GameWindow.ZOOM * 16));
        renderer = new RenderHandler(map.getWidth() * GameWindow.ZOOM * 16, map.getHeight() * GameWindow.ZOOM * 16);

        map.mappedTiles.forEach((v, k) -> {
            if (k.hasPowerup()) {
                gameObjectsBuffer.add(k.getPowerup());
            }
        });

        ((FlowLayout) this.getLayout()).setVgap(0);
        ((FlowLayout) this.getLayout()).setHgap(0);
        this.getGameWindow().pack();
    }

    public Tilemap getMap() {
        return this.map;
    }

    public void addPlayer(Player character) {
        LOGGER.log(Level.FINE, "Adding player #" + Player.PLAYER_COUNT + " as " + character.getClass().getSimpleName());

        this.players.add(character);
        this.addGameObject(character);
        canvas.addKeyListener(character.getListener());
        canvas.addFocusListener(character.getListener());
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

        if (object instanceof Player) {
            LOGGER.log(Level.FINE, "Removing player #" + ((Player) object).getPlayerNum() + " from game.");

            getGameWindow().getAudioPlayer().playAudio(new File("assets\\audio\\Death.wav"));
            players.remove(object);
        }
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