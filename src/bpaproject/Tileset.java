package bpaproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

    public Tileset(File file, SpriteSheet sheet) {
        this.sheet = sheet;
        parseTileset(file);
    }

    public Tileset(SpriteSheet sheet) {
        this.sheet = sheet;
        parseTileset(DEFAULT_TILESET);
    }

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
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
            if (file == DEFAULT_TILESET)
                throw new RuntimeException("Could not load default tileset! " + ex.toString());
            parseTileset(DEFAULT_TILESET);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            throw new RuntimeException(ex.toString());
        }
    }

    public void renderVoid(RenderHandler renderer, int xPos, int yPos, int xZoom, int yZoom) {
        Rectangle voidRect = new Rectangle(xPos, yPos, 16, 16);
        voidRect.setColor(0xFF000000);
        renderer.renderRectangle(voidRect, xZoom, yZoom);
    }

    public void renderTiles(RenderHandler renderer, int tileID, int xPos, int yPos, int xZoom, int yZoom) {
        if (tileID == -1)
            renderer.renderSprite(voidTile.sprite, xPos, yPos, xZoom, yZoom);
        else if (tileID > -1 && tileID < tilesList.size())
            renderer.renderSprite(tilesList.get(tileID).sprite, xPos, yPos, xZoom, yZoom);
        else
            LOGGER.log(Level.SEVERE, "tileID of " + tileID + " is not in the of " + tilesList.size() + "!");
    }

    public Tile getTile(int tileID) {
        if (tileID > -1 && tileID < tilesList.size())
            return this.tilesList.get(tileID);
        else {
            if (tileID != -1)
                LOGGER.log(Level.SEVERE, "tileID of " + tileID + " is not in the of " + tilesList.size() + "!");
            return voidTile;
        }
    }

    public int getSize() {
        return tilesList.size();
    }

    class Tile {
        private boolean collidable;
        private boolean breakable;
        public Sprite sprite;

        public Tile(Sprite sprite, boolean collidable, boolean breakable) {
            this.sprite = sprite;
            this.collidable = collidable;
            this.breakable = breakable;
        }

        public boolean isBreakable() {
            return breakable;
        }

        public boolean getCollision() {
            return collidable;
        }
    }
}
