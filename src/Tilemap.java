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
            // tileWidth = v.getTile().sprite.getWidth() * xZoom;
            // tileHeight = v.getTile().sprite.getHeight() * xZoom;
            tileset.renderTiles(renderer, v.tileID, v.x * width * BombGame.XZOOM, v.y * height * BombGame.YZOOM, xZoom,
                    yZoom);
        });
    }

    public Rectangle getTileCollision(Rectangle rect) {
        rect = new Rectangle(rect.x, rect.y, rect.width * BombGame.XZOOM, rect.height * BombGame.YZOOM);
        ArrayList<MappedTile> tiles = new ArrayList<MappedTile>();
        for (Point corners : rect.getCorners()) {
            Point mappedCorner = mapPointToTilemap(corners);
            MappedTile tile = getTile(mappedCorner.x, mappedCorner.y);
            if (!tiles.contains(tile)) {
                tiles.add(tile);
            }
        }

        for (MappedTile tile : tiles) {
            if (tile.isCollidable()) {
                if (rect.intersects(tile.collisionBox)) {
                    return rect.intersection(tile.collisionBox);
                }
            }
        }
        return null;
    }

    public Point mapPointToTilemap(int x, int y) {
        return mapPointToTilemap(new Point(x, y));
    }

    public Point mapPointToTilemap(Point p) {
        int x = ((p.x * (width - 1)) / ((width - 1) * 16 * BombGame.XZOOM));
        int y = ((p.y * (height - 1)) / ((height - 1) * 16 * BombGame.YZOOM));
        if (x > width)
            x = -1;
        if (y > height)
            y = -1;
        return new Point(x, y);
    }

    // public double mapPosition(int screenPos, int length) {
    // return ((screenPos + (length / 2.0)) * (width - 1)) / ((width - 1) *
    // tileWidth);
    // }
    public Point mapPointToScreen(Point p) {
        int x = p.x * 16 * BombGame.XZOOM;
        int y = p.y * 16 * BombGame.YZOOM;
        return new Point(x, y);
    }

    public MappedTile getTile(int x, int y) {
        int key = x + (y * width);

        if (mappedTiles.containsKey(key))
            return mappedTiles.get(x + (y * width));
        else if (key > 0 && key < width * height)
            return new MappedTile(fillTileID, x, y);
        else
            return new MappedTile(-1, -1, -1);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    class MappedTile {
        public int mappedTileID, tileID, x, y;
        private boolean collidable;
        private Rectangle collisionBox;

        public MappedTile(int tileID, int x, int y) {
            mappedTileID = x + (y * width);
            this.tileID = tileID;
            this.x = x;
            this.y = y;
            this.collidable = this.getTile().getCollision();
            if (collidable) {
                Point p = mapPointToScreen(new Point(this.x, this.y));
                collisionBox = new Rectangle(p.x, p.y, this.getWidth() * BombGame.XZOOM,
                        this.getHeight() * BombGame.YZOOM);
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

        public int[] getInfo() {
            return new int[] { mappedTileID, tileID, x, y };
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
    }
}
