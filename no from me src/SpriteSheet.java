/**
 * @file SpriteSheet.java
 * @author Dakota Taylor
 * @createdOn Thursday, 01 November, 2018
 */

public class SpriteSheet extends Rectangle {
    public Sprite[] sprites = null;

    public void loadSprites(int width, int height) {
        loadSprites(width, height, 0, 0);
    }

    public void loadSprites(int width, int height, int xPad, int yPad) {
        sprites = new Sprite[(this.width / width) * (this.height / height)];

        int spriteID = 0;
        for (int y = 0; y < this.height; y += height + yPad) {
            for (int x = 0; x < this.width; x += width + xPad) {
                sprites[spriteID++] = new Sprite(this, x, y, width, height);
            }
        }
    }
}