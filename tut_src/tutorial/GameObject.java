package tutorial;

/**
 * @file GameObject.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public interface GameObject {

    // Called whenever possibles
    public void render(RenderHandler renderer, int xZoom, int yZoom);

    // Called ~ every 60 frames
    public void update(BombGame game);
}