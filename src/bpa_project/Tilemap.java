package bpa_project;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @file Tilemap.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Tilemap {
    private Tileset tileset;
    private int fillTileID = -1;
    private int width, height;
    public Map<Integer, MappedTile> mappedTiles = new HashMap<Integer, MappedTile>();

    public Tilemap(File file, Tileset tileset) {
        this.tileset = tileset;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String s = reader.readLine();
            while (s != null) {
                if (!s.trim().startsWith("//")) {
                    String[] data = s.trim().split(":");
                    switch (data[0]) {
                    case "Width": {
                        this.width = Integer.parseInt(data[1]);
                        break;
                    }
                    case "Height": {
                        this.height = Integer.parseInt(data[1]);
                        break;
                    }
                    case "Fill": {
                        this.fillTileID = Integer.parseInt(data[1]);
                        break;
                    }
                    case "Tiles": {
                        s = reader.readLine();
                        // TODO: extract into methods
                        while (!s.equals("]") && s != null) {
                            if (!s.trim().startsWith("//")) {
                                String[] line = s.trim().split(":");
                                int tileID = Integer.parseInt(line[0]);
                                String[] coordsSet = line[1].split(",(?=(?:[^\\}]*\\{[^\\}]*\\})*[^\\}]*$)");
                                for (String c : coordsSet) {
                                    // parseCoords(c);
                                    if (!c.contains("-")) {
                                        String[] coords = c.replaceAll("[^,\\d]", "").split(",");
                                        int x = Integer.parseInt(coords[0]);
                                        int y = Integer.parseInt(coords[1]);

                                        if (x + 1 > width || y + 1 > height) {
                                            System.out.println("Warning: tile at {" + x + ", " + y
                                                    + "} is outside the map size of [" + width + ", " + height + "]");
                                        } else {
                                            MappedTile mappedTile = new MappedTile(tileID, x, y);
                                            mappedTiles.put(mappedTile.getID(), mappedTile);
                                        }
                                    } else if (!c.contains("%")) {
                                        String[] coords = c.replaceAll("[^,\\-\\d]", "").split("[,-]");
                                        int x1 = Integer.parseInt(coords[0]);
                                        int y1 = Integer.parseInt(coords[1]);
                                        int x2 = Integer.parseInt(coords[2]);
                                        int y2 = Integer.parseInt(coords[3]);
                                        for (int y = y1; y <= y2; y++) {
                                            for (int x = x1; x <= x2; x++) {
                                                if (x + 1 > width || y + 1 > height) {
                                                    System.out.println("Warning: tile at {" + x + ", " + y
                                                            + "} is outside the map size of [" + width + ", " + height
                                                            + "]");

                                                } else {
                                                    MappedTile mappedTile = new MappedTile(tileID, x, y);
                                                    mappedTiles.put(mappedTile.getID(), mappedTile);

                                                }
                                            }
                                        }
                                    } else {
                                        String[] coords = c.replaceAll("[^,\\-\\d\\%]", "").split("[%,-]");
                                        int x1 = Integer.parseInt(coords[0]);
                                        int y1 = Integer.parseInt(coords[1]);
                                        int x2 = Integer.parseInt(coords[2]);
                                        int y2 = Integer.parseInt(coords[3]);
                                        int xMod = Integer.parseInt(coords[4]);
                                        int yMod;
                                        if (coords.length >= 6)
                                            yMod = Integer.parseInt(coords[5]);
                                        else
                                            yMod = xMod;
                                        for (int y = y1; y <= y2; y += yMod) {
                                            for (int x = x1; x <= x2; x += xMod) {
                                                if (x + 1 > width || y + 1 > height) {
                                                    System.out.println("Warning: tile at {" + x + ", " + y
                                                            + "} is outside the map size of [" + width + ", " + height
                                                            + "]");
                                                } else {
                                                    MappedTile mappedTile = new MappedTile(tileID, x, y);
                                                    mappedTiles.put(mappedTile.getID(), mappedTile);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            s = reader.readLine();
                        }
                        break;
                    }
                    default:
                        break;
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

    /**
     * Renders the background tiles based off of the {@code fillTileID} then renders
     * the tiles the map contains.
     * 
     * @param renderer The renderer to handle rendering
     * @param xZoom    The zoom stretch the hortizontal plane
     * @param yZoom    The zoom stretch the verical plane
     */
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (fillTileID > -1) {
            Tileset.Tile fillTile = tileset.getTile(fillTileID);
            for (int y = 0; y < this.height * fillTile.sprite.getHeight() * yZoom; y += fillTile.sprite.getHeight()
                    * yZoom) {
                for (int x = 0; x < this.width * fillTile.sprite.getWidth() * xZoom; x += fillTile.sprite.getWidth()
                        * xZoom) {
                    tileset.renderTiles(renderer, fillTileID, x, y, xZoom, yZoom);
                }
            }
        }
        mappedTiles.forEach((k, v) -> {
            int width = v.getTile().sprite.getWidth();
            int height = v.getTile().sprite.getHeight();
            tileset.renderTiles(renderer, v.tileID, v.x * width * GameWindow.ZOOM, v.y * height * GameWindow.ZOOM,
                    xZoom, yZoom);
        });
    }

    /**
     * Checks the specified collider for collision on any tile with a collider.
     * 
     * @param col The specifed collider
     */
    public void checkCollision(Collider col) {
        ArrayList<MappedTile> tiles = new ArrayList<MappedTile>();
        for (Point corners : col.getCorners()) {
            Point mappedCorner = mapPointToTilemap(corners);
            MappedTile tile = getTile(mappedCorner.x, mappedCorner.y);
            if (!tiles.contains(tile) && tile.isCollidable()) {
                tiles.add(tile);
            }
        }
        for (MappedTile tile : tiles) {
            // tile.collider.checkCollision(col);
            col.checkCollision(tile.getCollider());
        }
    }

    /**
     * Maps the upper-left corner at the specified {@code (x, y)} to a relative
     * location on the {@code Tilemap}. Returns the relative (x, y) tile location in
     * a {@code Point}.
     * 
     * @param x The specified X coordinate
     * @param y The specified Y coordinate
     * @return The tile location {@code Point}
     */
    public Point mapPointToTilemap(int x, int y) {
        return mapPointToTilemap(new Point(x, y));
    }

    /**
     * Maps the pixel (x, y) in the specified {@code Point} to a relative location
     * on the {@code Tilemap}. Returns the relative (x, y) tile location in a
     * {@code Point}.
     * 
     * @param p The specified {@code Point}
     * @return The tile location {@code Point}
     */
    public Point mapPointToTilemap(Point p) {
        int x = ((p.x * (width - 1)) / ((width - 1) * 16 * GameWindow.ZOOM));
        int y = ((p.y * (height - 1)) / ((height - 1) * 16 * GameWindow.ZOOM));
        if (x > width)
            x = -1;
        if (y > height)
            y = -1;
        return new Point(x, y);
    }

    /**
     * Maps the relative location at the specified {@code (x, y)} to an exact pixel
     * location on the screen. Returns the exact (x, y) pixel location in a
     * {@code Point}.
     * 
     * @param x The specified X coordinate
     * @param y The specified Y coordinate
     * @return The exact pixel {@code location}
     */
    public Point mapPointToScreen(int x, int y) {
        return mapPointToScreen(new Point(x, y));
    }

    /**
     * Maps the tile (x, y) in the specified {@code Point} to an exact pixel
     * location on the screen. Returns the exact (x, y) pixel location in a
     * {@code Point}.
     * 
     * @param x The specified X coordinate
     * @param y The specified Y coordinate
     * @return The exact pixel {@code location}
     */
    public Point mapPointToScreen(Point p) {
        int x = p.x * 16 * GameWindow.ZOOM;
        int y = p.y * 16 * GameWindow.ZOOM;
        return new Point(x, y);
    }

    /**
     * Returns the tile at the relative location specified as {@code (x, y)}. If the
     * location is outside the tilemap's {@code width} or {@code height}, returns a
     * void tile.
     * 
     * @param x The specified X coordinate
     * @param y The specified Y coordinate
     * @return The tile at {@code (x, y)}; or a void tile if outside the tilemap's
     *         range.
     */
    public MappedTile getTile(int x, int y) {
        int key = x + (y * width);

        if (mappedTiles.containsKey(key))
            return mappedTiles.get(key);
        else if (key > 0 && key < width * height)
            return new MappedTile(fillTileID, x, y);
        else
            return new MappedTile(-1, -1, -1);

    }

    public void removeTile(int x, int y) {
        int key = x + (y * width);

        if (mappedTiles.containsKey(key))
            mappedTiles.remove(key);
    }

    /**
     * @return The width of the {@code Tilemap}
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the {@code Tilemap}
     */
    public int getHeight() {
        return height;
    }

    /**
     * A {@code Tile} that has an (x, y) location.
     */
    class MappedTile {
        public int mappedTileID, tileID, x, y;
        private boolean collidable;
        public Collider collider;
        // private Rectangle collisionBox;

        public MappedTile(int tileID, int x, int y) {
            mappedTileID = x + (y * width);
            this.tileID = tileID;
            this.x = x;
            this.y = y;
            this.collidable = this.getTile().getCollision();
            if (collidable) {
                Point p = mapPointToScreen(x, y);
                collider = new Collider(this, p.x, p.y, this.getWidth() * GameWindow.ZOOM,
                        this.getHeight() * GameWindow.ZOOM);
            }
        }

        public Tileset.Tile getTile() {
            return tileset.getTile(tileID);
        }

        public int getID() {
            return this.mappedTileID;
        }

        public boolean isCollidable() {
            return this.collidable;
        }

        public boolean isBreakable() {
            return this.getTile().isBreakable();
        }

        public int getWidth() {
            if (this.getTile().sprite != null)
                return this.getTile().sprite.getWidth();
            return 0;
        }

        public int getHeight() {
            if (this.getTile().sprite != null)
                return this.getTile().sprite.getHeight();
            return 0;
        }

        public MappedTile getTileAbove() {
            return Tilemap.this.getTile(x, y - 1);
        }

        public MappedTile getTileBelow() {
            return Tilemap.this.getTile(x, y + 1);
        }

        public MappedTile getTileLeft() {
            return Tilemap.this.getTile(x - 1, y);
        }

        public MappedTile getTileRight() {
            return Tilemap.this.getTile(x + 1, y);
        }

        public Collider getCollider() {
            return this.collider;
        }
    }
}
