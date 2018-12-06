package bpa_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @file Tileset.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Tileset {
    private SpriteSheet sheet;
    private ArrayList<Tile> tilesList = new ArrayList<Tile>();
    public final Tile voidTile = new Tile(null, true, false);

    public Tileset(File file, SpriteSheet sheet) {
        this.sheet = sheet;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));

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
            reader.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // test
    public void renderTiles(RenderHandler renderer, int tileID, int xPos, int yPos, int xZoom, int yZoom) {
        if (tileID < tilesList.size() && tileID != -1)
            renderer.renderSprite(tilesList.get(tileID).sprite, xPos, yPos, xZoom, yZoom);
        else
            System.out.println("Warning: TileID of " + tileID + " is not in the range of " + tilesList.size() + ".");
    }

    public Tile getTile(int tileID) {
        if (tileID != -1)
            return this.tilesList.get(tileID);
        else {
            return voidTile;
        }
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
