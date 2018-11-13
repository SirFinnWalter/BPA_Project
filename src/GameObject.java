/**
 * @file GameObject.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public interface GameObject {
    /**
     * Returns the game object collider if present.
     * 
     * @return The game object collider.
     */
    public Collider getCollider();

    /**
     * Every game object will need to be render to the screen.
     * 
     * @param renderer The render handler to handle the render.
     * @param xZoom    The zoom across the hortizontal plane.
     * @param yZoom    The zoom across the vertical plane.
     */
    public void render(RenderHandler renderer, int xZoom, int yZoom);

    /**
     * Every game object will update.
     * 
     * @param game The game window and info.
     */
    public void update(BombGame game);

    /**
     * Some game object needs to setup using the game.
     * 
     * @param game The game window and info.
     */
    public void init(BombGame game);
}