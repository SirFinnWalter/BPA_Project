/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public class Player {
    Rectangle playerRect;
    int speed = 5;

    public Player() {
        playerRect = new Rectangle(50, 100, 99, 55);
        playerRect.setColor(0xAAAAFF00);
    }

    public void render(RenderHandler renderer) {
        renderer.renderRectangle(this.playerRect);
    }

    public void update(BombGame game) {
        KeyboardListener listener = game.getListener();
        if (listener.up()) {
            playerRect.y -= speed;
        }
        if (listener.down()) {
            playerRect.y += speed;
        }
        if (listener.left()) {
            playerRect.x -= speed;
        }
        if (listener.right()) {
            playerRect.x += speed;
        }
    }
}