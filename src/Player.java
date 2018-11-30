import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

/**
 * A {@code Player} is instance of {@code GameObject} and
 * {@code CollisionListener} that the user controls. The contructor allows you
 * to set the keys to control the player once.
 * <p>
 * The {@code Player} can take in an instance of {@code Sprite} to render in
 * place of the player box. If no instance of {@code Sprite} is passed, then the
 * renderer will default to rendering the {@code Player} player box. The
 * {@code Player} will not allow movement on collision with its collider.
 */
public class Player implements GameObject, CollisionListener {
    private Rectangle playerBox;
    private Collider collider;
    double speedX = 1 * BombGame.XZOOM;
    double speedY = 1 * BombGame.YZOOM;
    FacingDirection currentFD, newFD;
    private Sprite sprite;
    private boolean destroyed;
    private AnimatedSprite animatedSprite = null;
    private KeyboardListener listener = null;

    private String name;

    /**
     * Constructs a {@code Player} at the upper-left bound of {@code (x,y)} and
     * whose sprite and listener is specified by the argument with the same name.
     * <p>
     * If the sprite passed is an instance of {@code AnimatedSprite}, then sets the
     * animatedSprite of the {@code Player} to the sprite and renders the
     * animatedSprite instead. If the sprite passed is {@code null}, renders a
     * transparent white box at the upper-left bound of {@code (x,y)}.
     * 
     * @param x      The specified X coordinate
     * @param y      The specified Y coordinate
     * @param sprite The sprite render at the {@code Player{@code  location @param
     *               listener The listener to control the player movement
     */
    public Player(String name, int x, int y, Sprite sprite, KeyboardListener listener) {
        this.name = name;
        this.sprite = sprite;
        if (sprite != null && sprite instanceof AnimatedSprite) {
            this.animatedSprite = (AnimatedSprite) sprite;
        }
        playerBox = new Rectangle(x * BombGame.XZOOM, y * BombGame.YZOOM, sprite.getWidth(), sprite.getHeight());
        collider = new Collider(this, x * BombGame.XZOOM, y * BombGame.YZOOM, 14 * BombGame.XZOOM, 14 * BombGame.YZOOM);

        currentFD = FacingDirection.up;
        newFD = FacingDirection.up;
        updateDirection();
        // playerBox = new Rectangle(x * BombGame.XZOOM, y * BombGame.YZOOM, 16, 21);
        playerBox.setColor(0x88FFFFFF);
        collider.setBorder(1, 0xFF0000FF);

        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (animatedSprite != null)
            renderer.renderSprite(animatedSprite, playerBox.x, playerBox.y, xZoom, yZoom);
        else if (sprite != null)
            renderer.renderSprite(sprite, playerBox.x, playerBox.y, xZoom, yZoom);
        else
            renderer.renderRectangle(playerBox, xZoom, yZoom);
        renderer.renderRectangle(collider, 1, 1);
    }

    /**
     * If the {@code Player} has an {@code AnimatedSprite}, then updates what frames
     * to loop through for the direction the {@code Player} is facing.
     */
    private void updateDirection() {
        if (animatedSprite != null) {
            int framesLength = (animatedSprite.getLength() / 4);
            animatedSprite.setAnimationRange(currentFD.getValue() * framesLength,
                    currentFD.getValue() * framesLength + (framesLength - 1));
        }
    }

    /**
     * Adds every player's {@code collider} in every player in the game except for
     * itself.
     * 
     * @param game The game info and state
     */
    @Override
    public void init(BombGame game) {
        // game.getM
        game.getPlayers().forEach(player -> {
            if (player != this) {
                collider.addGameObject((player));
            }
        });
    }

    int tempCount = 0;
    int tempMax = 1;
    int tempCurrent = 0;
    int tempLength = 1;
    int tempPoints = 0;

    /**
     * Updates the location of the {@code collider} then checks for collision on
     * each movement before actually moving the {@code Player}. Only updates the
     * direction the {@code Player} is facing if it is a new direction.
     * <p>
     * If the {@code Player} is moving, the begin looping through the
     * {@code animatedSprite}. If not, then stay static on the first frame of the
     * {@code animatedSprite}.
     * <p>
     * Also listens for when the {@code Player} wants to place a {@code Bomb}.
     * 
     * @param game The game info and state
     */
    @Override
    public void update(BombGame game) {
        boolean moving = false;

        if (listener.left()) {
            collider.x -= speedX;
            newFD = FacingDirection.left;
            moving = true;
            game.checkCollision(collider);
        }
        if (listener.right()) {
            collider.x += speedX;
            newFD = FacingDirection.right;
            moving = true;
            game.checkCollision(collider);
        }
        if (listener.up()) {
            collider.y -= speedY;
            newFD = FacingDirection.up;
            moving = true;
            game.checkCollision(collider);
        }
        if (listener.down()) {
            collider.y += speedY;
            newFD = FacingDirection.down;
            moving = true;
            game.checkCollision(collider);
        }

        // playerBox.x = collider.x - 0;
        // // playerBox.y = collider.y - 10;
        // playerBox.y = collider.y - 10;

        // playerBox.x = collider.x - ((playerBox.width - collider.width) *
        // BombGame.XZOOM);
        playerBox.x = collider.x - ((playerBox.width * BombGame.XZOOM - collider.width) / 2);
        playerBox.y = collider.y - ((playerBox.height * BombGame.YZOOM - collider.height));
        // playerBox.y = collider.y - (5 * 2);

        if (this.currentFD != newFD) {
            this.currentFD = newFD;
            updateDirection();
        }
        if (moving) {
            animatedSprite.update(game);
        } else {
            animatedSprite.reset();
        }
        if (listener.action()) {
            // Tilemap map = game.getMap();
            if (tempCurrent < tempMax) {
                Point mapPoint = BombGame.MAP.mapPointToTilemap(collider.x + collider.width / 2,
                        collider.y + collider.height / 2);
                // Tilemap.MappedTile tile = map.getTile(mapPoint.x, mapPoint.y);
                Point screenPoint = BombGame.MAP.mapPointToScreen(mapPoint);
                Bomb bomb = new Bomb(this, screenPoint.x, screenPoint.y, tempLength);
                game.addGameObject(bomb);
                tempCurrent++;
            }
        }
        if (destroyed)
            game.removeGameObject(this);
        tempCount++;
        if (tempCount > 650) {
            tempCount = 0;
            if (Math.random() < 0.5) {
                tempLength++;
                System.out.println(name + ": Bomb length is now " + tempLength + "!");
            } else {
                tempMax++;
                System.out.println(name + ": Max # of bombs is now " + tempMax + "!");
            }

            System.out.println(name + "'s score: " + tempPoints);
        }
    }

    /**
     * @param destroyed the destroyed to set
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * @return The collider
     */
    @Override
    public Collider getCollider() {
        return this.collider;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Auto-adjust the location of the {@code collider} based on the intersection of
     * the {@code collider} and the source collider.
     */
    @Override
    public void onCollisionEnter(CollisionEvent e) {
        switch (newFD) {
        case up:
            collider.y += e.intersection(collider).height;
            break;
        case down:
            collider.y -= e.intersection(collider).height;
            break;
        case left:
            collider.x += e.intersection(collider).width;
            break;
        case right:
            collider.x -= e.intersection(collider).width;
            break;
        default:
            collider.x = playerBox.x;
            collider.y = playerBox.y;
            break;
        }
        Object source = e.getSource().getObject();
        if (source instanceof Bomb.Explosion) {
            this.destroyed = true;
        } else if (source instanceof GameObject) {
            this.collider.checkCollision(((GameObject) source).getCollider());
        }
    }

    @Override
    public void onCollisionLeave(CollisionEvent e) {
    }

    @Override
    public void onCollisionStay(CollisionEvent e) {
        Object source = e.getSource().getObject();
        if (source instanceof Bomb.Explosion) {
            this.destroyed = true;
        }
    }

    /**
     * Directions the {@code Player} can be facing. Can be refered by name or
     * integer value.
     */
    public enum FacingDirection {
        up(0), down(1), left(2), right(3);

        private int value;
        private static Map<Integer, FacingDirection> map = new HashMap<>();

        private FacingDirection(int value) {
            this.value = value;
        }

        static {
            for (FacingDirection fd : FacingDirection.values()) {
                map.put(fd.value, fd);
            }
        }

        public static FacingDirection valueOf(int fd) {
            return (FacingDirection) map.get(fd);
        }

        public int getValue() {
            return value;
        }
    }
}