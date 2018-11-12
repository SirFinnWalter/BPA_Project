
/**
 * @file CollisionListener.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public interface CollisionListener {

    public void onCollision(CollisionEvent e);

    public Collider getCollider();
}