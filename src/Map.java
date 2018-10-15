import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @file Map.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Map {
    private Tiles tileset;
    private int fillTileID = -1;
    private int width, height;

    private ArrayList<MappedTile> mappedTiles = new ArrayList<MappedTile>();

    public Map(File file, Tiles tileset) {
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
                                        mappedTiles.add(mappedTile);
                                    }
                                } else {
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
                                                mappedTiles.add(mappedTile);
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
            Tiles.Tile fillTile = tileset.getTile(fillTileID);

            for (int y = 0; y < this.height * fillTile.sprite.getHeight() * yZoom; y += fillTile.sprite.getHeight()
                    * yZoom) {
                for (int x = 0; x < this.width * fillTile.sprite.getWidth() * xZoom; x += fillTile.sprite.getWidth()
                        * xZoom) {
                    tileset.renderTiles(renderer, fillTileID, x, y, xZoom, yZoom);
                }
            }
        }

        for (int i = 0; i < mappedTiles.size(); i++) {
            MappedTile mappedTile = mappedTiles.get(i);
            int tileWidth = mappedTile.getTile().sprite.getWidth() * xZoom;
            int tileHeight = mappedTile.getTile().sprite.getHeight() * yZoom;

            tileset.renderTiles(renderer, mappedTile.tileID, mappedTile.x * tileWidth, mappedTile.y * tileHeight, xZoom,
                    yZoom);
        }

    }

    class MappedTile {
        public int tileID, x, y;

        public MappedTile(int tileID, int x, int y) {
            this.tileID = tileID;
            this.x = x;
            this.y = y;
        }

        public Tiles.Tile getTile() {
            return tileset.getTile(tileID);
        }
    }
}
