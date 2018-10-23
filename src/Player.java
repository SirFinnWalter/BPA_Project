/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Player {
    Rectangle playerBox;
    Rectangle collisionBox;
    // Rectangle hitBox;
    int speed = 4;
    // int x, y;
    double mappedX, mappedY;
    int width, height;
    FacingDirection direction;

    public Player() {
        // NOTE: player sprite will be larger than collision box (also hitbox?)
        // playerRect = new Rectangle(50, 100, 28, 28);
        playerBox = new Rectangle(50, 100, 14, 14);
        // collisionBox = playerBox;
        collisionBox = new Rectangle(50, 100, 14, 14);
        playerBox.setColor(0xAAAAFF00);
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        width = playerBox.getWidth() * xZoom;
        height = playerBox.getHeight() * yZoom;

        renderer.renderRectangle(this.playerBox, xZoom, yZoom);
    }

    public void update(BombGame game) {
        KeyboardListener listener = game.getListener();
        if (listener.up()) {
            // playerBox.y -= speed;
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

        // int mapMaxX = game.getMap().getWidth() - 1;
        // int tileWidth = game.getMap().getTileWidth();
        // mappedX = (int) Math.floor((double) ((playerBox.x + (width / 2.0)) * mapMaxX)
        // / (mapMaxX * tileWidth));
        mappedX = game.getMap().mapPosition(playerBox.x);

        // int mapMaxY = game.getMap().getHeight() - 1;
        // int tileHeight = game.getMap().getTileHeight();
        // mappedY = (int) Math.floor((double) ((playerBox.y + (height / 2.0)) *
        // mapMaxY) / (mapMaxY * tileHeight));
        mappedY = game.getMap().mapPosition(playerBox.y);

        // TODO: Fix and improve collision
        if (!game.getMap().checkCollision(this, collisionBox)) {
            playerBox.x = collisionBox.x;
            playerBox.y = collisionBox.y;
        } else {
            collisionBox.x = playerBox.x;
            collisionBox.y = playerBox.y;
        }
        // if (!game.getMap().checkCollision(collisionBox)) {
        // } else {
        // // System.out.println("test");
        // }
    }

    enum FacingDirection {
        up, down, left, right
    }
}