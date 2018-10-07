import java.awt.image.BufferedImage;

/**
 * @file SpriteSheet.java
 * @author Dakota Taylor
 * @createdOn Saturday, 06 October, 2018
 */

public class SpriteSheet {
    private int[] pixels;
    private BufferedImage image;
    public final int SIZEX;
    public final int SIZEY;
    private Sprite[] loadedSprites = null;
    private boolean spritesLoaded = false;
    private int spriteSizeX;

    public SpriteSheet(BufferedImage image) {
        this.image = image;
        SIZEX = image.getWidth();
        SIZEY = image.getHeight();

        pixels = new int[SIZEX * SIZEY];
        pixels = image.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);
    }

    public void loadSprites(int spriteSizeX, int spriteSizeY) {
        this.spriteSizeX = spriteSizeX;
        loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];

        int spriteID = 0;
        for (int y = 0; y < SIZEY; y += spriteSizeY) {
            for (int x = 0; x < SIZEX; x += spriteSizeX) {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        }
        spritesLoaded = true;
    }

    public void loadSprites(int spriteSizeX, int spriteSizeY, int spritePadX, int spritePadY) {
        this.spriteSizeX = spriteSizeX;
        loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];

        int spriteID = 0;
        for (int y = 0; y < SIZEY; y += spriteSizeY + spritePadY) {
            for (int x = 0; x < SIZEX; x += spriteSizeX + spritePadX) {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        }
        spritesLoaded = true;
    }

    public Sprite getSprite(int x, int y) {
        if (spritesLoaded) {
            // int spriteID = x + y * (SIZEX / spriteSizeX);
            int spriteID = x + (y * SIZEX / spriteSizeX);

            if (spriteID < loadedSprites.length)
                return loadedSprites[spriteID];
            else
                System.out.println(
                        "Warning: SpriteID of " + spriteID + " is not in the range of " + loadedSprites.length + ".");
        } else
            System.out.println("Warning: SpriteSheet attempted to retrieve sprites before loading sprites");

        return null;
    }

    // public Sprite getSprite(int spriteID) {
    // if (spritesLoaded) {
    // if (spriteID < loadedSprites.length)
    // return loadedSprites[spriteID];
    // else
    // System.out.println(
    // "Warning: SpriteID of " + spriteID + " is not in the range of " +
    // loadedSprites.length + ".");
    // } else
    // System.out.println("Warning: SpriteSheet attempted to retrieve sprites before
    // loading sprites");

    // return null;
    // }

    public int[] getPixels() {
        return this.pixels;
    }

    public BufferedImage getImage() {
        return this.image;
    }

}