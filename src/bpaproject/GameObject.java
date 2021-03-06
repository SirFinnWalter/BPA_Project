package bpaproject;

import bpaproject.framecontent.Game;

/**
 * @file GameObject.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

/**
 * The interface for objects that appear on the screen. The class that
 * implements this interface should require constant updating and rendering.
 */
public interface GameObject {
    /**
     * Many game objects will also have a collider to prevent collisions with the
     * player, if the class implementing this interface does not contain a collider,
     * then it should return {@code null}.
     * 
     * @return The game object collider
     */
    public Collider getCollider();

    /**
     * Every game object must to be render to the screen. If the class implementing
     * this does not render anything to the screen, consider not using this
     * interface.
     * 
     * @param renderer renderer to handle the rendering
     * @param zoom     The zoom to render the object with
     */
    public void render(RenderHandler renderer, int zoom);

    /**
     * Every game object must update its state. If the class implementing this does
     * not update values or its state, consider not using this interface.
     * 
     * @param game The game window and info
     */
    public void update(Game game);

    /**
     * Configs the class needs set (but cannot do in its constructor) before it
     * starts updating.
     * 
     * @param game The game window and info
     */
    public void init(Game game);
}