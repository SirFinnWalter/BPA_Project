package bpaproject;

import java.io.File;

import bpaproject.characters.Player;
import bpaproject.framecontent.Game;

/**
 * @file Bomb.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 07 November, 2018
 */

/**
 * A {@code Bomb} is an instance of {@code GameObject} that kills players caught
 * in its explosion. The {@code Bomb} cannot be walked through
 */
public class Bomb implements GameObject {
    private static final int BOMB_ANIMATION_LENGTH = 10;
    public static final AnimatedSprite BOMB_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\bomb.png")), 16, 16),
            BOMB_ANIMATION_LENGTH);

    private Collider collider;
    private AnimatedSprite animatedSprite;
    private Player player;
    private int x, y;
    private int length;

    /**
     * Creates a new {@code Bomb} whose upper-left corner is specified as
     * {@code (x,y)}.
     * 
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public Bomb(Player player, int x, int y, int length) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.length = length;
        animatedSprite = BOMB_ANIMATED_SPRITE.clone();

        animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);
        collider = new Collider(this, x, y, 16 * GameWindow.ZOOM, 16 * GameWindow.ZOOM);
        collider.setBorder(1, 0xFFFF0000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        // animatedSprite.render(renderer, xZoom, yZoom);
        renderer.renderSprite(animatedSprite, collider.x, collider.y, xZoom, yZoom);
        // renderer.renderRectangle(collider, 1, 1);
    }

    /**
     * Adds the bomb's {@code collider} to every player in the game except for
     * whoever created the {@code Bomb}. The owner's {@code collider} will be added
     * when they leave the tile that contains the bomb.
     */
    @Override
    public void init(Game game) {
        game.getPlayers().forEach(player -> {
            player.getCollider().addGameObject(this);
        });
    }

    /**
     * Updates {@code animatedSprite} and checks if the {@code Bomb} has been
     * destroyed. If so, then it creates an explosion and removes the {@code Bomb}
     * from the game.
     * 
     * @param game The game info and state
     */
    @Override
    public void update(Game game) {
        animatedSprite.update(game);

        if (animatedSprite.isDestroyed()) {
            Explosion.createExplosion(game, length, this.x, this.y);
            game.removeGameObject(this);
            this.player.bombCount -= 1;
        }
    }

    /**
     * @return The collider
     */
    @Override
    public Collider getCollider() {
        return collider;
    }
}