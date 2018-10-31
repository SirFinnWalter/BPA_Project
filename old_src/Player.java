
/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Player {
    Rectangle playerBox;
    Rectangle collisionBox;
    // Rectangle hitBox;
    int speed = 1;
    // int x, y;
    double mappedX, mappedY;
    int width, height;
    FacingDirection direction;

    public Player(int x, int y) {
        // NOTE: player sprite will be larger than collision box (also hitbox?)
        // playerRect = new Rectangle(50, 100, 28, 28);
        playerBox = new Rectangle(x, y, 14, 14);
        // collisionBox = playerBox;
        collisionBox = new Rectangle(x, y, 14, 14);
        playerBox.setColor(0xAAAA88FF);
        collisionBox.setBorder(1, 0xFF000000);
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        width = (int) playerBox.getWidth() * xZoom;
        height = (int) playerBox.getHeight() * yZoom;

        renderer.renderRectangle(this.playerBox, xZoom, yZoom);
        renderer.renderRectangle(this.collisionBox, xZoom, yZoom);
    }

    public void update(BombGame game) {
        KeyboardListener listener = game.getListener();

        // TODO: Fix collision checks when player is inside a wall
        if (listener.up()) {
            collisionBox.y -= speed;
            direction = FacingDirection.up;
        }
        if (listener.down()) {
            collisionBox.y += speed;
            direction = FacingDirection.down;
        }
        if (listener.left()) {
            collisionBox.x -= speed;
            direction = FacingDirection.left;
        }
        if (listener.right()) {
            collisionBox.x += speed;
            direction = FacingDirection.right;
        }
        game.getMap().checkCollision(this, collisionBox);
        playerBox.x = collisionBox.x;
        playerBox.y = collisionBox.y;
    }

    enum FacingDirection {
        up, down, left, right
    }
}