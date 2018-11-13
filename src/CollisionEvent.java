/**
 * @file CollisionEvent.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class CollisionEvent {

    private Collider trigger;

    public CollisionEvent(Collider trigger) {
        this.trigger = trigger;
    }

    public Rectangle intersection(Collider col) {
        return trigger.intersection(col);
    }

    public Collider getSource() {
        return trigger;
    }

}