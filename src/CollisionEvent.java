/**
 * @file CollisionEvent.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class CollisionEvent {

    private Collider source;

    public CollisionEvent(Collider source) {
        this.source = source;
    }

    public Rectangle intersection(Collider col) {
        return source.intersection(col);
    }

    public Collider getSource() {
        return source;
    }
}