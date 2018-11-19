/**
 * @file CollisionEvent.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class CollisionEvent {

    private Collider source;

    /**
     * Creates a new collision event.
     * 
     * @param source The collider that triggered the event.
     */
    public CollisionEvent(Collider source) {
        this.source = source;
    }

    /**
     * Returns the cross-section between the triggering collider and another
     * collider.
     * 
     * @param col The collider to compare.
     * @return The cross-section between the triggering collider and another
     *         collider.
     */
    public Rectangle intersection(Collider col) {
        return source.intersection(col);
    }

    public Collider getSource() {
        return source;
    }

}