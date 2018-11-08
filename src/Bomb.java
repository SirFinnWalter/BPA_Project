import java.io.File;

/**
 * @file Bomb.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 07 November, 2018
 */

public class Bomb implements GameObject {
    static int tickFrame = 5;
    private static final AnimatedSprite BOMB_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\bomb.png")), 16, 16), tickFrame);

    Rectangle collisionBox;

    // private Sprite sprite;
    private AnimatedSprite animatedSprite;

    public Bomb() {
        animatedSprite = BOMB_ANIMATED_SPRITE;
        animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);

        collisionBox = new Rectangle(64 + 32, 64 + 32, 16, 16);
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        // animatedSprite.render(renderer, xZoom, yZoom);
        renderer.renderSprite(animatedSprite, collisionBox.x, collisionBox.y, xZoom, yZoom);
    }

    public void update(BombGame game) {
        animatedSprite.update(game);
    }
}