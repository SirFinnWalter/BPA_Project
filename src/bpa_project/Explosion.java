package bpa_project;

import java.io.File;

/**
 * @file Explosion.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 05 December, 2018
 */

public class Explosion implements GameObject {
    private static final int EXPLOSION_ANIMATION_LENGTH = 10;
    public static final AnimatedSprite EXPLOSION_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\explosion.png")), 16, 16),
            EXPLOSION_ANIMATION_LENGTH);
    public static final AnimatedSprite EXPLOSION_ANIMATED_SPRITE_VERTICAL = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\v_explosion.png")), 16, 16),
            EXPLOSION_ANIMATION_LENGTH);
    public static final AnimatedSprite EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\h_explosion.png")), 16, 16),
            EXPLOSION_ANIMATION_LENGTH);
    private final static File EXPLOSION_AUDIO = new File("assets\\audio\\Explosion.wav");
    private AnimatedSprite animatedSprite;
    private Collider collider;

    public Explosion(AnimatedSprite sprite, int x, int y) {

        collider = new Collider(this, x, y, 16 * GameWindow.ZOOM, 16 * GameWindow.ZOOM);
        collider.setBorder(1, 0xFFFF0000);
        this.animatedSprite = sprite;
        this.animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);
    }

    @Override
    public Collider getCollider() {
        return this.collider;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(animatedSprite, collider.x, collider.y, xZoom, yZoom);
        // renderer.renderRectangle(collider, 1, 1);
    }

    @Override
    public void update(Game game) {
        animatedSprite.update(game);
        if (animatedSprite.isDestroyed()) {
            game.removeGameObject(this);
        }
    }

    @Override
    public void init(Game game) {
        game.getPlayers().forEach(player -> {
            player.getCollider().addGameObject(this);
            player.getCollider().checkCollision(collider);
        });

        game.getGameWindow().getAudioPlayer().playAudio(EXPLOSION_AUDIO);
    }
}