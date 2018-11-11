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
    FacingDirection fd1, fd2;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;

    public Player(int x, int y, Sprite sprite) {
        this.sprite = sprite;
        if (sprite != null && sprite instanceof AnimatedSprite)
            this.animatedSprite = (AnimatedSprite) sprite;
        fd1 = FacingDirection.up;
        fd2 = FacingDirection.up;
        updateDirection();
        playerBox = new Rectangle(x * BombGame.XZOOM, y * BombGame.YZOOM, 16, 16);
        collider = new Collider(x * BombGame.XZOOM, y * BombGame.YZOOM, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM);
        playerBox.setColor(0x88FFFFFF);
        collider.setBorder(1, 0xFF0000FF);

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
            animatedSprite.setAnimationRange(fd1.getValue() * 7, fd1.getValue() * 7 + 6);
        }
    }

    public void init(BombGame game) {
        game.getMap().mappedTiles.forEach((k, v) -> {
            if (v.collider != null)
                v.collider.addCollisionListener(this);
        });
    }

    public synchronized void update(BombGame game) {
        KeyboardListener listener = game.getListener();
        boolean moving = false;

        if (listener.left()) {
            collider.x -= speedX;
            fd2 = FacingDirection.left;
            moving = true;
            game.checkCollision();
        }
        if (listener.right()) {
            collider.x += speedX;
            fd2 = FacingDirection.right;
            moving = true;
            game.checkCollision();
        }
        if (listener.up()) {
            collider.y -= speedY;
            fd2 = FacingDirection.up;
            moving = true;
            game.checkCollision();
        }
        if (listener.down()) {
            collider.y += speedY;
            fd2 = FacingDirection.down;
            moving = true;
            game.checkCollision();
        }

        playerBox.x = collider.x;
        playerBox.y = collider.y;

        if (this.fd1 != fd2) {
            this.fd1 = fd2;
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
        switch (fd2) {
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