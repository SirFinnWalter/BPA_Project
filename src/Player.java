/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Player {
    Rectangle playerRect;
    int speed = 4;
    // int x, y;
    FacingDirection direction;
    int mappedX, mappedY;

    public Player() {
        // playerRect = new Rectangle(50, 100, 28, 28);
        playerRect = new Rectangle(50, 100, 14, 14);
        playerRect.setColor(0xAAAAFF00);
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderRectangle(this.playerRect, xZoom, yZoom);
    }

    public void update(BombGame game) {
        KeyboardListener listener = game.getListener();
        if (listener.up()) {
            playerRect.y -= speed;
            direction = FacingDirection.up;
        }
        if (listener.down()) {
            playerRect.y += speed;
            direction = FacingDirection.down;
        }
        if (listener.left()) {
            playerRect.x -= speed;
            direction = FacingDirection.left;
        }
        if (listener.right()) {
            playerRect.x += speed;
            direction = FacingDirection.right;
        }
        // TODO: Map player's location based on center instead of top-right
        int mapMaxX = game.getMap().getWidth() - 1;
        int tileWidth = game.getMap().getTileWidth();
        mappedX = (int) Math.floor((double) (playerRect.x * mapMaxX) / (mapMaxX * tileWidth));

        int mapMaxY = game.getMap().getHeight() - 1;
        int tileHeight = game.getMap().getTileHeight();
        mappedY = (int) Math.floor((double) (playerRect.y * mapMaxY) / (mapMaxY * tileHeight));
    }

    enum FacingDirection {
        up, down, left, right
    }
}