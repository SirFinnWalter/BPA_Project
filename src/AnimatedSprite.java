import java.awt.image.BufferedImage;

/**
 * @file AnimatedSprite.java
 * @author Dakota Taylor
 * @createdOn Saturday, 03 November, 2018
 */

public class AnimatedSprite extends Sprite {
    private Sprite[] sprites;
    private int currentSprite = 0;
    private int counter = 0;
    private int flipFrame;
    private int start, end;
    private AnimationType animationType = AnimationType.looping;

    public AnimatedSprite(SpriteSheet sheet, Rectangle[] range, int flipFrame) {
        sprites = new Sprite[range.length];
        this.flipFrame = flipFrame;
        this.start = 0;
        this.end = range.length - 1;
        for (int i = 0; i < range.length; i++) {
            sprites[i] = new Sprite(sheet, range[i].x, range[i].y, range[i].width, range[i].height);
        }
    }

    /**
     * 
     * @param images
     * @param flipFrame the number of frames until sprites changes
     */
    public AnimatedSprite(BufferedImage[] images, int flipFrame) {
        this.sprites = new Sprite[images.length];
        this.flipFrame = flipFrame;
        this.start = 0;
        this.end = images.length - 1;

        for (int i = 0; i < images.length; i++) {
            sprites[i] = new Sprite(images[i]);
        }
    }

    public AnimatedSprite(SpriteSheet sheet, int flipFrame) {
        this.sprites = sheet.getSprites();
        this.flipFrame = flipFrame;
        this.start = 0;
        this.end = this.sprites.length - 1;
    }

    public void setAnimationRange(int start, int end) {
        this.start = start;
        this.end = end;
        reset();
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {

    }

    public void update(BombGame game) {
        counter++;
        if (counter >= flipFrame) {
            counter = 0;
            incrementSprite();
        }
    }

    public void reset() {
        counter = 0;
        currentSprite = start;
    }

    public void incrementSprite() {
        currentSprite++;
        if (currentSprite > end) {
            switch (animationType) {
            case pause: {
                currentSprite = end;
            }
                break;
            case looping:
            default: {
                currentSprite = start;
            }
                break;
            }
        }

    }

    @Override
    public int getWidth() {
        return sprites[currentSprite].getWidth();
    }

    @Override
    public int getHeight() {
        return sprites[currentSprite].getHeight();
    }

    @Override
    public int[] getPixels() {
        return sprites[currentSprite].getPixels();
    }

    public enum AnimationType {
        looping, pause;
    }
}