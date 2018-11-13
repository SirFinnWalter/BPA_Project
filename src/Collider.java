import java.util.HashSet;
import java.util.Set;

/**
 * @file Collider.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

public class Collider extends Rectangle {
    private static final long serialVersionUID = 1630374324558026943L;
    private Set<GameObject> objects = new HashSet<GameObject>();
    GameObject object;

    public Collider(GameObject object) {
        super();
        this.object = object;
    }

    public Collider(GameObject object, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.object = object;
    }

    public void checkCollision(Collider col) {
        boolean collision = this.intersects(col);
        if (collision) {
            // System.out.println("collision");
            if (col.getGameObject() == null) {
                this.fireCollision(col);
                return;
            }

            objects.forEach(object -> {
                if (col == object.getCollider()) {
                    this.fireCollision(col);
                }
            });
        }
    }

    public void addGameObject(GameObject object) {
        objects.add(object);
    }

    public void removeGameObject(GameObject object) {
        objects.remove(object);
    }

    public GameObject getGameObject() {
        return this.object;
    }

    public Set<GameObject> getGameObjects() {
        return this.objects;
    }

    // public CollisionListener getCollisionListener() {
    // return this.object;
    // }

    // public void addCollisionListener(CollisionListener listener) {
    // listeners.add(listener);
    // }

    // public void removeCollisionListener(CollisionListener listener) {
    // listeners.remove(listener);
    // }

    public void fireCollision(Collider col) {
        // System.out.println("fired");
        CollisionEvent e = new CollisionEvent(col);
        if (object instanceof CollisionListener) {
            ((CollisionListener) object).onCollision(e);
        } else {
            System.out.println("Warning: Attempted to trigger collision on an object that is not listening.");
        }
        // for (CollisionListener listener : listeners) {
        // listener.onCollision(e);
        // }
    }
}