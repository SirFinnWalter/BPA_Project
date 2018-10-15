package tutorial;

/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 09 October, 2018
 */
public class Player implements GameObject {

    Rectangle playerRect;
    int speed = 1;

    public Player() {
        playerRect = new Rectangle(0, 0, 16, 16);
        playerRect.generateGraphics(3, 0xFFFF0000);
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderRectangle(playerRect, xZoom, yZoom);
    }

    public void update(BombGame game) {
        KeyboardListener kListener = game.getKeyListener();

        if (kListener.up()) {
            playerRect.y -= speed;
        }

        if (kListener.down()) {
            playerRect.y += speed;
        }

        if (kListener.left()) {
            playerRect.x -= speed;
        }

        if (kListener.right()) {
            playerRect.x += speed;
        }

    }

}