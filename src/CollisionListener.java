
/**
 * @file CollisionListener.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public interface CollisionListener {
    // public Collider getCollider();

    /**
     * Called whenever a collision with the object has occurred.
     * 
     * @param e The collision event.
     */
    public void onCollision(CollisionEvent e);

}