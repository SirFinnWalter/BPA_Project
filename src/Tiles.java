import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @file Tiles.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */
public class Tiles {
    private SpriteSheet sheet;
    private ArrayList<Tile> tilesList = new ArrayList<Tile>();

    public Tiles(File file, SpriteSheet sheet) {
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
                        int collisionType = parseCollision(data[2]);
                        boolean breakable = Boolean.parseBoolean(data[3]);
                        Tile tile = new Tile(this.sheet.getSprite(x, y), collisionType, breakable);
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

    public void renderTiles(RenderHandler renderer, int tileID, int xPos, int yPos, int xZoom, int yZoom) {
        if (tileID < tilesList.size())
            renderer.renderSprite(tilesList.get(tileID).sprite, xPos, yPos, xZoom, yZoom);
        else
            System.out.println("Warning: TileID of " + tileID + " is not in the range of " + tilesList.size() + ".");
    }

    public Tile getTile(int tileID) {
        return this.tilesList.get(tileID);
    }

    public static int parseCollision(String s) {
        if (!s.contains("|")) {
            switch (s.trim().toUpperCase()) {
            case "LEFT":
                return CollisionConstants.LEFT;
            case "RIGHT":
                return CollisionConstants.RIGHT;
            case "TOP":
                return CollisionConstants.TOP;
            case "BOTTOM":
                return CollisionConstants.BOTTOM;
            case "ALL":
                return CollisionConstants.ALL;
            case "NONE":
                return CollisionConstants.NONE;
            default:
                return 0;
            }
        } else {
            int bitwise = 0;
            for (String c : s.split("\\|")) {
                bitwise = bitwise | parseCollision(c);
            }
            return bitwise;
        }
    }

    class Tile {
        private int collisionType;
        private boolean breakable;
        public Sprite sprite;

        public Tile(Sprite sprite, int collisionType, boolean breakable) {
            this.sprite = sprite;
            this.collisionType = collisionType;
            this.breakable = breakable;
        }

    }

    public final class CollisionConstants {
        public static final int LEFT = 0b0001;
        public static final int RIGHT = 0b0010;
        public static final int TOP = 0b0100;
        public static final int BOTTOM = 0b1000;
        public static final int ALL = 0b1111;
        public static final int NONE = 0b0000;
    }
}
