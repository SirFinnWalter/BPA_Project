package bpa_project;

import java.awt.image.BufferedImage;

/**
 * @file AnimatedSprite.java
 * @author Dakota Taylor
 * @createdOn Saturday, 03 November, 2018
 */

public class AnimatedSprite extends Sprite implements Cloneable {
    private Sprite[] sprites;
    private int currentSprite = 0;
    private int counter = 0;
    private int flipFrame;
    private int start, end;
    private int length;
    private boolean visible;
    private AnimationType animationType = AnimationType.looping;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AnimatedSprite(SpriteSheet sheet, Rectangle[] range, int flipFrame) {
        sprites = new Sprite[range.length];
        this.flipFrame = flipFrame;
        this.start = 0;
        this.end = range.length - 1;
        this.length = range.length;
        this.visible = true;
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
        this.length = images.length;
        this.visible = true;

        for (int i = 0; i < images.length; i++) {
            sprites[i] = new Sprite(images[i]);
        }
    }

    public AnimatedSprite(SpriteSheet sheet, int flipFrame) {
        this.sprites = sheet.getSprites();
        this.flipFrame = flipFrame;
        this.start = 0;
        this.end = this.sprites.length - 1;
        this.length = sprites.length;
        this.visible = true;
    }

    public void setAnimationRange(int start, int end) {
        int relativeSprite = this.currentSprite - this.start;
        this.start = start;
        this.end = end;
        this.currentSprite = this.start + relativeSprite;
        // reset();
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        System.out.println("DONT CALL THIS - AnimatedSprite.render");
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
        if (currentSprite >= 0)
            currentSprite++;
        if (currentSprite > end) {
            switch (animationType) {
            case pause: {
                currentSprite = end;
            }
                break;
            case destroy: {
                currentSprite = -1;
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
        if (currentSprite >= 0)
            return sprites[currentSprite].getWidth();
        return 0;
    }

    @Override
    public int getHeight() {
        if (currentSprite >= 0)
            return sprites[currentSprite].getHeight();
        return 0;
    }

    @Override
    public int[] getPixels() {
        if (!visible)
            return null;

        if (currentSprite >= 0)
            return sprites[currentSprite].getPixels();

        return null;
    }

    public int getCurrentSprite() {
        return this.currentSprite;
    }

    public int getLength() {
        return this.length;
    }

    public boolean isDestroyed() {
        return (currentSprite == -1);
    }

    public void setDestoyed(boolean destroyed) {
        if (destroyed)
            currentSprite = -1;
        else
            currentSprite = 0;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public enum AnimationType {
        looping, pause, destroy;
    }
}