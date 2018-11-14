import java.io.File;

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
    private static final AnimatedSprite BOMB_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\bomb.png")), 16, 16), 5);

    Collider collider;

    private AnimatedSprite animatedSprite;
    private Player owner;
    private boolean init;

    /**
     * Creates a new {@code Bomb} whose upper-left corner is specified as
     * {@code (x,y)}.
     * 
     * @param player The player that created the bomb
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     */
    public Bomb(Player player, int x, int y) {
        try {
            animatedSprite = (AnimatedSprite) BOMB_ANIMATED_SPRITE.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating bomb.");
        }

        animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);
        collider = new Collider(this, x, y, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM);
        collider.setBorder(1, 0xFFFF0000);
        this.owner = player;
        init = true;
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
    public void init(BombGame game) {
        game.getPlayers().forEach(player -> {
            if (player != owner)
                player.getCollider().addGameObject(this);

        });
    }

    /**
     * Updates {@code animatedSprite} and checks if the {@code Bomb} has been
     * destroyed. If so, then removes the {@code Bomb} from the game. If the owner
     * leaves the bomb's {@code collider}, add the bomb's {@code collider} to the
     * owner.
     * 
     * @param game The game info and state
     */
    @Override
    public void update(BombGame game) {
        animatedSprite.update(game);

        if (animatedSprite.isDestroyed()) {
            game.removeGameObject(this);
        }

        if (init && !owner.getCollider().intersects(this.collider)) {
            owner.getCollider().addGameObject(this);
            init = false;
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