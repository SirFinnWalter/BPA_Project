package bpaproject;

/**
 * @file CollisionListener.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

/**
 * Listens for collision events. The class that implements this interface should
 * act upon getting a collision call.
 */
public interface CollisionListener {

    /**
     * Notifies the class that its collider just collided with another collider.
     * 
     * @param e The collision event
     */
    public void onCollisionEnter(CollisionEvent e);

    /**
     * Notifies the class that its collider is intersecting another collider.
     * 
     * @param e The collision event
     */
    public void onCollisionStay(CollisionEvent e);

    /**
     * Notifies the class that its collider just left another collider.
     * 
     * @param e The collision event
     */
    public void onCollisionLeave(CollisionEvent e);
}