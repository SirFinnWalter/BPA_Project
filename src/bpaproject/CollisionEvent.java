package bpaproject;

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
     * Returns a new {@code Rectangle} from the intersection between the source and
     * the {@code col}.
     * 
     * @param col The collider
     * @return A {@code Rectangle} from the intersection or an empty
     *         {@code Rectangle} if the rectangles do not intersect
     */
    public Rectangle intersection(Collider col) {
        return source.intersection(col);
    }

    /**
     * @return The collider that triggered the event.
     */
    public Collider getSource() {
        return source;
    }

}