import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @file Tiles.java
 * @author Dakota Taylor
 * @createdOn Saturday, 06 October, 2018
 */

public class Tiles {

    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tilesList = new ArrayList<Tile>();

    // Sprites in spritesheet must be loaded
    public Tiles(File tilesFile, SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(tilesFile));

            String s = reader.readLine();
            while (s != null) {
                if (!s.trim().startsWith("//")) {
                    String[] data = s.split(",");
                    if (data.length >= 3) {
                        String tileName = data[0];
                        Integer spriteX = Integer.parseInt(data[1]);
                        Integer spriteY = Integer.parseInt(data[2]);

                        Tile tile = new Tile(tileName, this.spriteSheet.getSprite(spriteX, spriteY));
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

    public void renderTiles(int tileID, RenderHandler renderer, int xPos, int yPos, int xZoom, int yZoom) {
        if (tileID < tilesList.size())
            renderer.renderSprite(tilesList.get(tileID).sprite, xPos, yPos, xZoom, yZoom);
        else
            System.out.println("Warning: TileID of " + tileID + " is not in the range of " + tilesList.size() + ".");

    }

    public Tile getTile(int tileID) {
        return this.tilesList.get(tileID);
    }

    class Tile {
        public String tileName;
        public Sprite sprite;

        public Tile(String tileName, Sprite sprite) {
            this.tileName = tileName;
            this.sprite = sprite;
        }
    }
}