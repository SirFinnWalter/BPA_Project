import java.util.HashSet;
import java.util.Set;

/**
 * @file Collider.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class Collider extends Rectangle {
    private static final long serialVersionUID = 1630374324558026943L;
    private Set<CollisionListener> listeners = new HashSet<CollisionListener>();

    public Collider() {
        super();
        this.tempName = "???";
    }

    String tempName;

    public Collider(int x, int y, int width, int height, String tempName) {
        super(x, y, width, height);
        this.tempName = tempName;
    }

    public boolean checkCollision(Collider col) {
        boolean collision = this.intersects(col);
        if (collision) {
            this.fireCollision(col);
        }
        return this.intersects(col);
    }

    public void addCollisionListener(CollisionListener listener) {
        listeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    public void fireCollision(Collider col) {
        // System.out.println("fired");
        CollisionEvent e = new CollisionEvent(this, col);
        for (CollisionListener listener : listeners) {
            if (listener.getCollider() == col) {
                listener.onCollision(e);
            }
        }
    }
}