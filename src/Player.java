import java.util.HashMap;
import java.util.Map;

/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Player {
    Rectangle playerBox;
    Rectangle collisionBox;
    int speedX = 1 * BombGame.XZOOM;
    int speedY = 1 * BombGame.YZOOM;
    double mappedX, mappedY;
    FacingDirection fd = FacingDirection.up;
    private Sprite sprite;
    private AnimatedSprite animatedSprite = null;

    public Player(int x, int y, Sprite sprite) {
        this.sprite = sprite;
        if (sprite != null && sprite instanceof AnimatedSprite)
            this.animatedSprite = (AnimatedSprite) sprite;

        updateDirection();
        playerBox = new Rectangle(x * BombGame.XZOOM, y * BombGame.YZOOM, 16, 32);
        collisionBox = new Rectangle(x * BombGame.XZOOM, y * BombGame.YZOOM, 16, 16);
        playerBox.setColor(0x88FFFFFF);
        collisionBox.setBorder(1, 0xFFFF0000);
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (animatedSprite != null)
            renderer.renderSprite(animatedSprite, playerBox.x, playerBox.y, xZoom, yZoom);
        else if (sprite != null)
            renderer.renderSprite(sprite, playerBox.x, playerBox.y, xZoom, yZoom);
        // else
        // renderer.renderRectangle(this.playerBox, xZoom, yZoom);

        // renderer.renderRectangle(this.playerBox, xZoom, yZoom);
        // renderer.renderRectangle(this.collisionBox, xZoom, yZoom);
    }

    private void updateDirection() {
        if (animatedSprite != null) {
            animatedSprite.setAnimationRange(fd.getValue() * 4, fd.getValue() * 4 + 3);
        }
    }

    public void update(BombGame game) {
        KeyboardListener listener = game.getListener();
        boolean moving = false;
        FacingDirection fd = this.fd;
        Rectangle collision = null;

        if (listener.left()) {
            collisionBox.x -= speedX;
            collision = game.getMap().getTileCollision(collisionBox);
            if (collision != null) {
                collisionBox.x += collision.width;
            }
            fd = FacingDirection.left;
            moving = true;
        }
        if (listener.right()) {
            collisionBox.x += speedX;
            collision = game.getMap().getTileCollision(collisionBox);
            if (collision != null) {
                collisionBox.x -= collision.width;
            }
            fd = FacingDirection.right;
            moving = true;
        }
        if (listener.up()) {
            collisionBox.y -= speedY;
            collision = game.getMap().getTileCollision(collisionBox);
            if (collision != null) {
                collisionBox.y += collision.height;
            }
            fd = FacingDirection.up;
            moving = true;
        }
        if (listener.down()) {
            collisionBox.y += speedY;
            collision = game.getMap().getTileCollision(collisionBox);
            if (collision != null) {
                collisionBox.y -= collision.height;
            }
            fd = FacingDirection.down;
            moving = true;
        }
        playerBox.x = collisionBox.x - ((playerBox.width - collisionBox.width) * BombGame.XZOOM);
        playerBox.y = collisionBox.y - ((playerBox.height - collisionBox.height) * BombGame.YZOOM);

        if (this.fd != fd) {
            this.fd = fd;
            updateDirection();
        }
        if (moving) {
            animatedSprite.update(game);
        } else {
            animatedSprite.reset();
        }
        if (listener.action())
            System.out.println("bomb has been planted");

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