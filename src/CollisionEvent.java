/**
 * @file CollisionEvent.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class CollisionEvent {

    private Collider source, trigger;

    public CollisionEvent(Collider source, Collider trigger) {
        this.source = source;
        this.trigger = trigger;
    }

    public Rectangle intersection(Collider col) {
        return source.intersection(col);
    }

    public Collider getSource() {
        return source;
    }

    public Collider getTrigger() {
        return trigger;
    }
}