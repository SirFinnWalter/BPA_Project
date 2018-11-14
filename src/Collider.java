import java.util.HashSet;
import java.util.Set;

/**
 * @file Collider.java
 * @author Dakota Taylor
 * @createdOn Saturday, 10 November, 2018
 */

/**
 * A {@code Collider} is a rectangle that can collide with another
 * {@code Collider}.
 * <p>
 * Colliders will only trigger a collision event only if the collider has the
 * other collider in its colliders pool and the {@code GameObject} that create
 * the collider is an instance of {@code CollisionListener}
 */
public class Collider extends Rectangle {
    private static final long serialVersionUID = 1630374324558026943L;
    // private Set<Collider> colliders = new HashSet<Collider>();
    private Set<GameObject> gameObjects = new HashSet<GameObject>();
    // GameObject object;
    Object object;

    /**
     * Constructs a new {@code Collider} with an empty {@code Rectangle} with a
     * reference to a {@code GameObject}.
     *
     * @param object The game object
     * @see Rectangle
     */
    public Collider(GameObject object) {
        super();
        this.object = object;
    }

    /**
     * Constructs a new {@code Collider} with a {@code Rectangle} whose upper-left
     * corner is specified as {@code (x,y)} and whose width and height are specified
     * by the arguments of the same name and a reference to a specified
     * {@code GameObject}.
     *
     * @param object The specified {@code GameObject}
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     * @param width  the width of the {@code Collider}
     * @param height the height of the {@code Collider}
     * @see Rectangle
     */
    public Collider(GameObject object, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.object = object;
    }

    /**
     * Constructs a new {@code Collider} with a {@code Rectangle} whose upper-left
     * corner is specified as {@code (x,y)} and whose width and height are specified
     * by the arguments of the same name and a reference to a specified
     * {@code MappedTile}.
     * 
     * @param tile   The specified {@code MappedTile}
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     * @param width  the width of the {@code Collider}
     * @param height the height of the {@code Collider}
     */
    public Collider(Tilemap.MappedTile tile, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.object = tile;
    }

    /**
     * Checks for a collision with {@code col} then fires a collision event if
     * either the {@code col} is a {@code MappedTile} collider or the {@code col} is
     * in {@code colliders}.
     *
     * @param col The collider to check
     */
    public void checkCollision(Collider col) {
        boolean collision = this.intersects(col);
        if (collision) {

            if (col.getObject() instanceof Tilemap.MappedTile) {
                this.fireCollision(col);
                return;
            }
            gameObjects.forEach(gameObject -> {
                if (col == gameObject.getCollider()) {
                    this.fireCollision(col);
                }
            });
        }
    }

    /**
     * Adds an instance of {@code GameObject} whose collider will fire a
     * {@code CollisionEvent} upon collision.
     *
     * @param object The {@code GameObject} to add
     */
    public void addGameObject(GameObject object) {
        gameObjects.add(object);
    }

    /**
     * Removes an instance of {@code GameObject} so its collider will not fire a
     * {@code CollisionEvent} upon collision.
     *
     * @param object The {@code GameObject} to remove
     */
    public void removeGameObject(GameObject object) {
        gameObjects.remove(object);
    }

    /**
     * Returns the object. Will either be an instance of {@code GameObject} or a
     * {@code MappedTile}
     * 
     * @return the object
     */
    public Object getObject() {
        return this.object;
    }

    /**
     * @return The colliders
     */
    public Set<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void fireCollision(Collider col) {
        CollisionEvent e = new CollisionEvent(col);
        // TODO: Find a better way to ensure object is listening to collision before
        // firing a collision event
        if (object instanceof CollisionListener) {
            ((CollisionListener) object).onCollision(e);
        } else {
            throw new RuntimeException("Warning: Attempted to trigger collision on an object that is not listening.");
        }
    }
}