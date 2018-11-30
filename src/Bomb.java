import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

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
    private static final AnimatedSprite BOMB_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\bomb.png")), 16, 16), BOMB_ANIMATION_LENGTH);
    private static final AnimatedSprite EXPLOSION_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\explosion.png")), 16, 16),
            BOMB_ANIMATION_LENGTH);
    private static final AnimatedSprite EXPLOSION_ANIMATED_SPRITE_VERTICAL = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\v_explosion.png")), 16, 16),
            BOMB_ANIMATION_LENGTH);
    private static final AnimatedSprite EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\h_explosion.png")), 16, 16),
            BOMB_ANIMATION_LENGTH);

    private ArrayList<Player> players = new ArrayList<Player>();
    private Collider collider;
    private AnimatedSprite animatedSprite;
    private Player owner;
    private int x, y;
    private int length;

    /**
     * Creates a new {@code Bomb} whose upper-left corner is specified as
     * {@code (x,y)}.
     * 
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     */
    public Bomb(Player owner, int x, int y, int length) {
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.length = length;
        try {
            animatedSprite = (AnimatedSprite) BOMB_ANIMATED_SPRITE.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Could not create the bomb! " + e.getMessage());
        }

        animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);
        collider = new Collider(this, x, y, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM);
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
    public void init(BombGame game) {
        game.getPlayers().forEach(player -> {
            player.getCollider().addGameObject(this);
            players.add(player);
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

            try {
                createSegments(game, EXPLOSION_ANIMATED_SPRITE, 1, 0, 0);
                createSegments(game, EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, length, -16 * BombGame.XZOOM, 0);
                createSegments(game, EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, length, 16 * BombGame.XZOOM, 0);
                createSegments(game, EXPLOSION_ANIMATED_SPRITE_VERTICAL, length, 0, -16 * BombGame.YZOOM);
                createSegments(game, EXPLOSION_ANIMATED_SPRITE_VERTICAL, length, 0, 16 * BombGame.YZOOM);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Could not create the explosion! " + e.getMessage());
            }

            game.removeGameObject(this);
            owner.tempCurrent -= 1;
        }
    }

    private void createSegments(BombGame game, AnimatedSprite sprite, int length, int xIncrement, int yIncrement)
            throws CloneNotSupportedException {
        for (int i = 1; i <= length; i++) {
            int x = this.x + (xIncrement * i);
            int y = this.y + (yIncrement * i);
            Point p = BombGame.MAP.mapPointToTilemap(x, y);
            if (BombGame.MAP.getTile(p.x, p.y).isBreakable()) {
                BombGame.MAP.removeTile(p.x, p.y);
                owner.tempPoints++;
                Explosion segment = new Explosion(this, (AnimatedSprite) sprite.clone(), x, y);
                game.addGameObject(segment);
                return;
            } else if (!BombGame.MAP.getTile(p.x, p.y).isCollidable()) {
                Explosion segment = new Explosion(this, (AnimatedSprite) sprite.clone(), x, y);
                game.addGameObject(segment);
            } else {
                return;
            }
        }
    }

    /**
     * @return The collider
     */
    @Override
    public Collider getCollider() {
        return collider;
    }

    class Explosion implements GameObject {
        private AnimatedSprite animatedSprite;
        private Collider collider;

        public Explosion(Bomb bomb, AnimatedSprite sprite, int x, int y) {

            collider = new Collider(this, x, y, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM);
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
        public void update(BombGame game) {
            animatedSprite.update(game);
            if (animatedSprite.isDestroyed()) {
                game.removeGameObject(this);
            }
        }

        @Override
        public void init(BombGame game) {
            game.getPlayers().forEach(player -> {
                player.getCollider().addGameObject(this);
                player.getCollider().checkCollision(collider);
            });
        }
    }
}