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
    CollisionListener listener;

    public Collider(CollisionListener listener) {
        super();
        this.listener = listener;
    }

    public Collider(CollisionListener listener, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.listener = listener;
    }

    public void checkCollision(Collider col) {
        boolean collision = this.intersects(col);
        if (collision) {
            // System.out.println("collision");
            if (col.getCollisionListener() instanceof Tilemap.MappedTile || col.getCollisionListener() == null) {
                this.fireCollision(col);
                return;
            }

            listeners.forEach(listener -> {
                if (col == listener.getCollider()) {
                    this.fireCollision(col);
                }
            });
        }
    }

    public CollisionListener getCollisionListener() {
        return this.listener;
    }

    public void addCollisionListener(CollisionListener listener) {
        listeners.add(listener);
    }

    public void removeCollisionListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    public void fireCollision(Collider col) {
        // System.out.println("fired");
        CollisionEvent e = new CollisionEvent(col);
        listener.onCollision(e);
        // for (CollisionListener listener : listeners) {
        // listener.onCollision(e);
        // }
    }
}