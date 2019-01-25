package bpaproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file Tileset.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Tileset {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final File DEFAULT_TILESET = new File("assets\\maps\\tileset");

    private SpriteSheet sheet;
    private ArrayList<Tile> tilesList = new ArrayList<Tile>();
    public final Tile voidTile = new Tile(new Sprite(GameWindow.loadImage(new File("assets\\tilesets\\VoidTile.png"))),
            false, false);

    /**
     * Constructs a new {@code Tileset} from the {@code file} using the
     * {@code sheet} sprite images.
     * 
     * @param file
     * @param sheet
     */
    public Tileset(File file, SpriteSheet sheet) {
        this.sheet = sheet;
        parseTileset(file);
    }

    /**
     * Constructs a new {@code Tileset} using the default tileset and the
     * {@code sheet} sprite images.
     * 
     * @param file
     * @param sheet
     */
    public Tileset(SpriteSheet sheet) {
        this.sheet = sheet;
        parseTileset(DEFAULT_TILESET);
    }

    /**
     * Parses the {@code file} into tiles
     * 
     * @param file The file
     */
    private void parseTileset(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s = reader.readLine();
            while (s != null) {
                if (!s.trim().startsWith("//")) {
                    String[] data = s.split(",");
                    if (data.length >= 4) {
                        int x = Integer.parseInt(data[0]);
                        int y = Integer.parseInt(data[1]);
                        boolean collision = Boolean.parseBoolean(data[2]);
                        boolean breakable = Boolean.parseBoolean(data[3]);
                        Tile tile = new Tile(this.sheet.getSprite(x, y), collision, breakable);
                        tilesList.add(tile);
                    }
                }
                s = reader.readLine();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
            if (file == DEFAULT_TILESET)
                GameWindow.crash("Could not load default tileset!", ex);

            parseTileset(DEFAULT_TILESET);
        }
    }

    /**
     * Renders a tile {@code tileID} at {@code x, y} or renders a void tile if the
     * {@code tileID} is not in range
     * 
     * @param renderer The renderer to handle rendering
     * @param tileID   The ID of the tile
     * @param x        The x position
     * @param y        The y position
     * @param zoom     The scale factor
     */
    public void renderTiles(RenderHandler renderer, int tileID, int x, int y, int zoom) {
        if (tileID > -1 && tileID < tilesList.size())
            renderer.renderSprite(tilesList.get(tileID).sprite, x, y, zoom);
        else {
            if (tileID != -1)
                LOGGER.log(Level.WARNING, "tileID of " + tileID + " is not in the of " + tilesList.size() + "!");

            renderer.renderSprite(voidTile.sprite, x, y, zoom);
        }
    }

    /**
     * @param tileID The ID of the tile
     * @return The tile that associated with the {@code tileID}
     */
    public Tile getTile(int tileID) {
        if (tileID > -1 && tileID < tilesList.size())
            return this.tilesList.get(tileID);
        else {
            return voidTile;
        }
    }

    /**
     * @return The size of the list of tiles.
     */
    public int getSize() {
        return tilesList.size();
    }

    /**
     * The class {@code Tile} holds the sprite of a tile and if it is collidable or
     * breakable.
     */
    public class Tile {
        private boolean collidable;
        private boolean breakable;
        public Sprite sprite;

        /**
         * Contructs a new {@code Tile} with the {@code sprite} and if it is
         * {@code collidable} and/or {@code breakble}
         * 
         * @param sprite     The sprite
         * @param collidable If it cannot be walked on
         * @param breakable  If bombs destroys it
         */
        public Tile(Sprite sprite, boolean collidable, boolean breakable) {
            this.sprite = sprite;
            this.collidable = collidable;
            this.breakable = breakable;
        }

        public boolean isBreakable() {
            return breakable;
        }

        public boolean isCollidable() {
            return collidable;
        }
    }
}
