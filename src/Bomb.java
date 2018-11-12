import java.io.File;

/**
 * @file Bomb.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 07 November, 2018
 */

public class Bomb implements GameObject, CollisionListener {
    private int tickFrame = 5;
    private static final AnimatedSprite BOMB_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\bomb.png")), 16, 16), 5);

    Collider collider;

    // private Sprite sprite;
    private AnimatedSprite animatedSprite;
    private Player owner;
    private boolean init;

    public Bomb(Player player, int x, int y) {
        try {
            animatedSprite = (AnimatedSprite) BOMB_ANIMATED_SPRITE.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);
        this.owner = player;
        collider = new Collider(x, y, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM, "bomb");
        collider.setBorder(1, 0xFFFF0000);
        init = true;
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        // animatedSprite.render(renderer, xZoom, yZoom);
        renderer.renderSprite(animatedSprite, collider.x, collider.y, xZoom, yZoom);
        // renderer.renderRectangle(collider, 1, 1);
    }

    public void init(BombGame game) {
        game.getPlayers().forEach(player -> {
            if (player != owner)
                collider.addCollisionListener(player);
        });
    }

    public void update(BombGame game) {
        animatedSprite.update(game);

        if (animatedSprite.isDestroyed()) {
            game.removeGameObject(this);
            // collider = null;
        }

        if (init && !owner.getCollider().intersects(this.collider)) {
            collider.addCollisionListener(owner);
            init = false;
        }
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onCollision(CollisionEvent e) {

    }
}