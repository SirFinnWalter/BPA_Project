package bpaproject;

import java.awt.image.BufferedImage;

/**
 * @file AnimatedSprite.java
 * @author Dakota Taylor
 * @createdOn Saturday, 03 November, 2018
 */

/**
 * The class {@code AnimatedSprite} is a collection of sprites but only returns
 * the active sprite. How often it changes depends on the {@code flipFrame}.
 */
public class AnimatedSprite extends Sprite {

    private Sprite[] sprites;
    private int currentSprite = 0; // The current sprite index
    private int counter = 0;
    private int flipFrame;
    private int start, end; // The start and end index
    private int length;
    private AnimationType animationType = AnimationType.LOOPING;

    /**
     * Creates the sprite collection from the {@code images} and flips the sprites
     * after it plays for {@code flipFrame} frames.
     * 
     * @param images    The image array
     * @param flipFrame The number of frames until sprites changes
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

    /**
     * Copies the {@code sheet} sprites into the sprite collection and flips the
     * sprites after it plays for {@code flipFrame} frames.
     * 
     * @param sheet
     * @param flipFrame
     */
    public AnimatedSprite(SpriteSheet sheet, int flipFrame) {
        this.sprites = sheet.getSprites();
        this.flipFrame = flipFrame;
        this.start = 0;
        this.end = sprites.length - 1;
        this.length = sprites.length;
        this.visible = true;
    }

    /**
     * Sets the range the AnimatedSprite should be playing from {@code start} to
     * {@code end}. The current sprite frame position carries over to the new range.
     * 
     * @param start The start
     * @param end   The end
     */
    public void setAnimationRange(int start, int end) {
        int relativeSprite = this.currentSprite - this.start;
        this.start = start;
        this.end = end;
        this.currentSprite = this.start + relativeSprite;
    }

    /**
     * Increments a counter. When the counter exceeds the flipFrame, then set the
     * counter to 0 and runs {@link #incrementSprite()}
     */
    public void update() {
        if (++counter >= flipFrame) {
            counter = 0;
            incrementSprite();
        }
    }

    /**
     * Sets the current sprite index to the start index
     */
    public void reset() {
        counter = 0;
        currentSprite = start;
    }

    /**
     * Increments the current sprite index. Upon reaching the end, sets the current
     * sprite index based on the AnimationType:
     * <ul>
     * <li>LOOPING will set the current sprite index to the start index</li>
     * <li>PAUSE will set the current sprite index to the end index</li>
     * <li>DESTROY will set the current sprite index to -1</li>
     * </ul>
     */
    public void incrementSprite() {
        if (currentSprite >= 0)
            currentSprite++;
        if (currentSprite > end) {
            switch (animationType) {
            case PAUSE:
                currentSprite = end;
                break;
            case DESTROY:
                currentSprite = -1;
                break;
            case LOOPING:
            default:
                currentSprite = start;
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnimatedSprite clone() {
        return (AnimatedSprite) super.clone();
    }

    /**
     * Sets the {@code animationType}.
     * <ul>
     * <li>LOOPING means the sprite will reset back to starting sprite upon reaching
     * the last sprite</li>
     * <li>PAUSE means the sprite will stay on the last sprite upon reaching it</li>
     * <li>DESTROY means there will be no sprite after reaching the last sprite</li>
     * </ul>
     * 
     * @param animationType The animationType
     */

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    /**
     * @return the animationType
     */
    public AnimationType getAnimationType() {
        return this.animationType;
    }

    /**
     * @return if the sprite has ended and AnimatedType is DESTROY
     */
    public boolean isDestroyed() {
        return (currentSprite == -1);
    }

    /**
     * @return The width of the current sprite or 0 if the AnimatedSprite is
     *         destroyed
     */
    @Override
    public int getWidth() {
        if (currentSprite >= 0)
            return sprites[currentSprite].getWidth();
        return 0;
    }

    /**
     * @return The height of the current sprite or 0 if the AnimatedSprite is
     *         destroyed
     */
    @Override
    public int getHeight() {
        if (currentSprite >= 0)
            return sprites[currentSprite].getHeight();
        return 0;
    }

    /**
     * @return The current sprite's pixels or {@code null} if the AnimatedSprite is
     *         destroyed
     */
    @Override
    public int[] getPixels() {
        if (currentSprite >= 0)
            return sprites[currentSprite].getPixels();

        return null;
    }

    /**
     * @return The index of the current sprite
     */
    public int getCurrentSprite() {
        return this.currentSprite;
    }

    /**
     * @return The number of sprites
     */
    public int getLength() {
        return this.length;
    }

    /**
     * <ul>
     * <li>LOOPING means the sprite will reset back to starting sprite upon reaching
     * the last sprite</li>
     * <li>PAUSE means the sprite will stay on the last sprite upon reaching it</li>
     * <li>DESTROY means there will be no sprite after reaching the last sprite</li>
     * </ul>
     */
    public enum AnimationType {
        LOOPING, PAUSE, DESTROY;
    }
}