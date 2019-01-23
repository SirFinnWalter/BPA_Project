package bpa_project;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import bpa_project.characters.Player;
import bpa_project.powerups.*;

/**
 * @file Tilemap.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Tilemap {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private Tileset tileset;
    private int fillTileID = -1;
    private int width, height;
    private boolean valid;
    public Map<Integer, Point> playerPositions = new HashMap<Integer, Point>();
    public Map<Integer, MappedTile> mappedTiles = new HashMap<Integer, MappedTile>() {
        private static final long serialVersionUID = 7952020856602712011L;

        @Override
        public MappedTile put(Integer key, MappedTile value) {
            if (containsKey(key) && get(key).hasPowerup() && value.isBreakable()) {
                value.addPowerup(get(key).getPowerup());
            } else if (value.isBreakable()) {
                if (RANDOM.nextInt(101) <= 20) {
                    if (RANDOM.nextBoolean())
                        value.addPowerup(new BlastUp(value.x * 16, value.y * 16));
                    else
                        value.addPowerup(new BombUp(value.x * 16, value.y * 16));
                }
            }
            return super.put(key, value);
        }
    };

    public Tilemap(File file, Tileset tileset) {
        this.tileset = tileset;
        this.width = 0;
        this.height = 0;

        LOGGER.log(Level.FINE, "Parsing tilemap file: " + file.getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s = reader.readLine();
            while (s != null) {
                if (!s.trim().startsWith("//")) {
                    String[] data = s.trim().split(":");
                    switch (data[0]) {
                    case "Width": {
                        LOGGER.log(Level.FINER, "Parsing width.");
                        this.width = Integer.parseInt(data[1].trim());

                        if (this.width <= 0) {
                            throw new RuntimeException("Tilemap width cannot be less than or equal to 0!");
                        }
                        break;
                    }
                    case "Height": {
                        LOGGER.log(Level.FINER, "Parsing height.");
                        this.height = Integer.parseInt(data[1].trim());

                        if (this.height <= 0) {
                            throw new RuntimeException("Tilemap height cannot be less than or equal to 0!");
                        }
                        break;
                    }
                    case "Fill": {
                        LOGGER.log(Level.FINER, "Parsing fill.");
                        this.fillTileID = Integer.parseInt(data[1].trim());

                        if (this.fillTileID < -1 || this.fillTileID > tileset.getSize()) {
                            LOGGER.log(Level.WARNING, "Fill TileID of " + this.fillTileID + " is not in the range of "
                                    + tileset.getSize() + "!");
                            this.fillTileID = -1;
                        }
                        break;
                    }
                    case "Positions": {
                        LOGGER.log(Level.FINER, "Parsing positions.");
                        s = reader.readLine();

                        while (!s.equals("]") && s != null) {
                            if (!s.trim().startsWith("//")) {
                                String[] line = s.trim().split("[:,]");
                                int playerNum = Integer.parseInt(line[0].trim());
                                Point position = new Point(Integer.parseInt(line[1].trim()),
                                        Integer.parseInt(line[2].trim()));

                                playerPositions.put(playerNum, null);
                                if (position.x < 0)
                                    LOGGER.log(Level.WARNING,
                                            "Player #" + playerNum + " x position cannot be less than 0!");
                                else if (position.x > width)
                                    LOGGER.log(Level.WARNING, "Player #" + playerNum
                                            + " x position cannot be greater than the width of " + width + "!");
                                else if (position.y < 0)
                                    LOGGER.log(Level.WARNING,
                                            "Player #" + playerNum + " y position cannot be less than 0!");
                                else if (position.y > height)
                                    LOGGER.log(Level.WARNING, "Player #" + playerNum
                                            + " y position cannot be greater than the height of " + height + "!");
                                else
                                    playerPositions.put(playerNum, position);
                            }
                            s = reader.readLine();
                        }
                        break;
                    }

                    case "Tiles": {
                        LOGGER.log(Level.FINER, "Parsing tiles.");
                        s = reader.readLine();
                        while (!s.equals("]") && s != null) {
                            if (!s.trim().startsWith("//")) {
                                String[] line = s.trim().split(":");
                                int tileID = Integer.parseInt(line[0].trim());

                                if (tileID < -1 || tileID > tileset.getSize()) {
                                    LOGGER.log(Level.WARNING, "TileID of " + tileID + " is not in the range of "
                                            + tileset.getSize() + "!");
                                    tileID = -1;
                                }

                                String[] coordsSet = line[1].split(",(?=(?:[^\\}]*\\{[^\\}]*\\})*[^\\}]*$)");
                                for (String c : coordsSet) {
                                    if (!c.contains("-")) {
                                        String[] coords = c.replaceAll("[^,\\d]", "").split(",");
                                        int x = Integer.parseInt(coords[0].trim());
                                        int y = Integer.parseInt(coords[1].trim());
                                        addTile(tileID, x, y, x, y, 1, 1, false);
                                    } else if (!c.contains("%")) {
                                        boolean diagonal = c.contains("-d");
                                        c.replaceAll("-d", "-");
                                        String[] coords = c.replaceAll("[^,\\-\\d]", "").split("[,-]");
                                        int x1 = Integer.parseInt(coords[0].trim());
                                        int y1 = Integer.parseInt(coords[1].trim());
                                        int x2 = Integer.parseInt(coords[2].trim());
                                        int y2 = Integer.parseInt(coords[3].trim());
                                        addTile(tileID, x1, y1, x2, y2, 1, 1, diagonal);
                                    } else {
                                        boolean diagonal = c.contains("-d");
                                        c.replaceAll("-d", "-");
                                        String[] coords = c.replaceAll("[^,\\-\\d\\%]", "").split("[%,-]");
                                        int x1 = Integer.parseInt(coords[0].trim());
                                        int y1 = Integer.parseInt(coords[1].trim());
                                        int x2 = Integer.parseInt(coords[2].trim());
                                        int y2 = Integer.parseInt(coords[3].trim());
                                        int xMod = Integer.parseInt(coords[4].trim());
                                        int yMod = coords.length >= 6 ? Integer.parseInt(coords[5].trim()) : xMod;
                                        addTile(tileID, x1, y1, x2, y2, xMod, yMod, diagonal);
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
            if (width <= 0)
                throw new RuntimeException("Tilemap must have a width!");
            if (height <= 0)
                throw new RuntimeException("Tilemap must have a height!");
            if (playerPositions.isEmpty())
                throw new RuntimeException("Tilemap must have specify player positions!");
            if (mappedTiles.isEmpty())
                throw new RuntimeException("Tilemap must have a tiles!");

            valid = true;

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, ex.toString(), ex);
            valid = false;
        } catch (RuntimeException | IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
            valid = false;
            throw new RuntimeException(ex.getMessage());
        }

    }

    public void addTile(int tileID, int x1, int y1, int x2, int y2, int xMod, int yMod, boolean diagonal) {
        if (!diagonal) {
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (int y = y1; y <= y2; y += yMod) {
                for (int x = x1; x <= x2; x += xMod) {
                    if (x + 1 > width || y + 1 > height) {
                        LOGGER.log(Level.WARNING, "Tile at {" + x + ", " + y + "} is outside the map size of [" + width
                                + ", " + height + "]");
                    } else {
                        MappedTile mappedTile = new MappedTile(tileID, x, y);
                        mappedTiles.put(mappedTile.getID(), mappedTile);
                    }
                }
            }
        } else {
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;

                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            if (y1 > y2) {
                int y = y1;
                for (int x = x1; x <= x2; x += xMod) {
                    if (x + 1 > width || y + 1 > height) {
                        LOGGER.log(Level.WARNING, "Tile at {" + x + ", " + y + "} is outside the map size of [" + width
                                + ", " + height + "]");
                    } else {
                        MappedTile mappedTile = new MappedTile(tileID, x, y);
                        mappedTiles.put(mappedTile.getID(), mappedTile);
                        y -= yMod;
                        if (y < y2)
                            break;
                    }
                }
            } else {
                int y = y1;
                for (int x = x1; x <= x2; x += xMod) {
                    if (x + 1 > width || y + 1 > height) {
                        LOGGER.log(Level.WARNING, "Tile at {" + x + ", " + y + "} is outside the map size of [" + width
                                + ", " + height + "]");
                    } else {
                        MappedTile mappedTile = new MappedTile(tileID, x, y);
                        mappedTiles.put(mappedTile.getID(), mappedTile);
                        y += yMod;
                        if (y > y2)
                            break;
                    }
                }
            }
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

        if (mappedTiles.containsKey(key)) {
            mappedTiles.get(key).destroy();
            mappedTiles.remove(key);
        }
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

    public Point getPlayerPosition(int playerNum) {
        if (playerNum > Player.MAX_PLAYERS || playerNum < 0) {
            LOGGER.log(Level.WARNING, "Player #" + playerNum + " is not a valid player number!");
            return null;
        }
        Point position = playerPositions.get(playerNum);
        if (position == null) {
            LOGGER.log(Level.WARNING, "No position for player #" + playerNum + " in tilemap!");
        }
        return position;
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * A {@code Tile} that has an (x, y) location.
     */
    public class MappedTile {
        public int mappedTileID, tileID, x, y;
        private boolean collidable;
        public Collider collider;
        private Powerup powerup;
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

        public void destroy() {
            if (powerup != null)
                powerup.setVisible(true);
        }

        public void addPowerup(Powerup powerup) {
            if (this.powerup == null)
                this.powerup = powerup;
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

        public Powerup getPowerup() {
            if (this.powerup == null)
                LOGGER.log(Level.WARNING, "Returning a null power!");
            return this.powerup;
        }

        public boolean hasPowerup() {
            return (this.powerup != null);
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
