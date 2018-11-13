import java.util.HashMap;
import java.util.Map;

/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Player implements GameObject, CollisionListener {
    private Rectangle playerBox;
    private Collider collider;
    double speedX = 1 * BombGame.XZOOM;
    double speedY = 1 * BombGame.YZOOM;
    FacingDirection currentFD, newFD;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;
    private KeyboardListener listener = null;

    public Player(int x, int y, Sprite sprite, KeyboardListener listener) {
        this.sprite = sprite;
        if (sprite != null && sprite instanceof AnimatedSprite)
            this.animatedSprite = (AnimatedSprite) sprite;
        currentFD = FacingDirection.up;
        newFD = FacingDirection.up;
        updateDirection();
        playerBox = new Rectangle(x * BombGame.XZOOM, y * BombGame.YZOOM, 16, 16);
        collider = new Collider(this, x * BombGame.XZOOM, y * BombGame.YZOOM, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM);
        playerBox.setColor(0x88FFFFFF);
        collider.setBorder(1, 0xFF0000FF);

        this.listener = listener;
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (animatedSprite != null)
            renderer.renderSprite(animatedSprite, playerBox.x, playerBox.y, xZoom, yZoom);
        else if (sprite != null)
            renderer.renderSprite(sprite, playerBox.x, playerBox.y, xZoom, yZoom);
        // renderer.renderRectangle(collider, 1, 1);
        // renderer.renderRectangle(playerBox, xZoom, yZoom);
    }

    private void updateDirection() {
        if (animatedSprite != null) {
            animatedSprite.setAnimationRange(currentFD.getValue() * 7, currentFD.getValue() * 7 + 6);
        }
    }

    public void init(BombGame game) {
        game.getMap().mappedTiles.forEach((k, v) -> {
            if (v.collider != null)
                v.collider.addGameObject(this);
        });
        game.getPlayers().forEach(object -> {
            if (object != this) {
                // object.getCollider().addCollisionListener(this);
                collider.addGameObject((Player) object);
            }
        });
    }

    public synchronized void update(BombGame game) {
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

        playerBox.x = collider.x;
        playerBox.y = collider.y;

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
            System.out.println("bomb has been planted");
            // TODO: Create bomb on map tile
            game.addGameObject(new Bomb(this, collider.x, collider.y));

        }

    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public synchronized void onCollision(CollisionEvent e) {
        // System.out.println("c");
        // if (e.getTrigger() == this.collider) {
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
        // }
    }

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