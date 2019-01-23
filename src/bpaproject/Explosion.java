package bpaproject;

import java.awt.Point;
import java.io.File;
import bpaproject.framecontent.Game;

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
    public static final File EXPLOSION_AUDIO = new File("assets\\audio\\Explosion.wav");
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
    }

    /**
     * Creates an explosion at the center {@code (x, y)} until it reaches the
     * {@code length}
     * 
     * @param game
     * @param length
     * @param x
     * @param y
     */
    public static void createExplosion(Game game, int length, int x, int y) {
        createSegments(game, EXPLOSION_ANIMATED_SPRITE, 1, x, y, 0, 0);
        createSegments(game, EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, length, x, y, -16 * GameWindow.ZOOM, 0);
        createSegments(game, EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, length, x, y, 16 * GameWindow.ZOOM, 0);
        createSegments(game, EXPLOSION_ANIMATED_SPRITE_VERTICAL, length, x, y, 0, -16 * GameWindow.ZOOM);
        createSegments(game, EXPLOSION_ANIMATED_SPRITE_VERTICAL, length, x, y, 0, 16 * GameWindow.ZOOM);

        game.getGameWindow().getAudioPlayer().playAudio(EXPLOSION_AUDIO);
    }

    private static void createSegments(Game game, AnimatedSprite sprite, int length, int xPos, int yPos, int xIncrement,
            int yIncrement) {
        for (int i = 1; i <= length; i++) {
            int x = xPos + (xIncrement * i);
            int y = yPos + (yIncrement * i);
            Point p = game.getMap().mapPointToTilemap(x, y);
            if (game.getMap().getTile(p.x, p.y).isBreakable()) {
                game.getMap().removeTile(p.x, p.y);
                Explosion segment = new Explosion(sprite.clone(), x, y);
                game.addGameObject(segment);
                return;
            } else if (!game.getMap().getTile(p.x, p.y).isCollidable()) {
                Explosion segment = new Explosion(sprite.clone(), x, y);
                game.addGameObject(segment);
            } else {
                return;
            }
        }
    }
}