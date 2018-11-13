/**
 * @file CollisionEvent.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class CollisionEvent {

    private Collider trigger;

    /**
     * Creates a new collision event.
     * 
     * @param trigger The collider that triggered the event.
     */
    public CollisionEvent(Collider trigger) {
        this.trigger = trigger;
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
        return trigger.intersection(col);
    }

    // public Collider getTrigger() {
    // return trigger;
    // }

}