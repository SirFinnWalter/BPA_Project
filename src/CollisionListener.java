
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
     * Notifies the class that a collsion has occurred. If the class does nothing
     * upon recieving, consider not using this interface.
     * 
     * @param e The collision event
     */
    public void onCollision(CollisionEvent e);

}