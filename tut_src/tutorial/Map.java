package tutorial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @file Map.java
 * @author Dakota Taylor
 * @createdOn Sunday, 07 October, 2018
 */

public class Map {
    private Tiles tileSet;
    private int fillTileID = -1;

    private ArrayList<MappedTile> mappedTiles = new ArrayList<MappedTile>();

    public Map(File mapFile, Tiles tileSet) {
        this.tileSet = tileSet;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapFile));

            String s = reader.readLine();
            while (s != null) {
                if (!s.trim().startsWith("//")) {
                    if (s.contains(":")) {
                        String[] data = s.split(":");
                        if (data[0].equalsIgnoreCase("Fill")) {
                            fillTileID = Integer.parseInt(data[1]);
                            s = reader.readLine();

                            continue;
                        }
                    }

                    String[] data = s.split(",");
                    if (data.length >= 3) {
                        MappedTile mappedTile = new MappedTile(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                                Integer.parseInt(data[2]));
                        mappedTiles.add(mappedTile);
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
            Rectangle camera = renderer.getCamera();

            for (int y = 0; y < camera.height; y++) {
                for (int x = 0; x < camera.width; x++) {

                }
            }
        }

        for (int i = 0; i < mappedTiles.size(); i++) {
            MappedTile mappedTile = mappedTiles.get(i);
            int tileWidth = mappedTile.getTile().sprite.getWidth() * xZoom;
            int tileHeight = mappedTile.getTile().sprite.getHeight() * yZoom;

            tileSet.renderTiles(mappedTile.id, renderer, mappedTile.x * tileWidth, mappedTile.y * tileHeight, xZoom,
                    yZoom);
        }
    }

    class MappedTile {
        public int id, x, y;

        public MappedTile(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public Tiles.Tile getTile() {
            return tileSet.getTile(id);
        }
    }
}