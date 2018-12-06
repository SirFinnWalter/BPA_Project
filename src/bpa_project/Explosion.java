package bpa_project;

/**
 * @file Explosion.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 05 December, 2018
 */

public class Explosion implements GameObject {
    private AnimatedSprite animatedSprite;
    private Collider collider;

    public Explosion(AnimatedSprite sprite, int x, int y) {

        collider = new Collider(this, x, y, 16 * BombGame.XZOOM, 16 * BombGame.YZOOM);
        collider.setBorder(1, 0xFFFF0000);
        this.animatedSprite = sprite;
        this.animatedSprite.setAnimationType(AnimatedSprite.AnimationType.destroy);
    }

    @Override
    public Collider getCollider() {
        return this.collider;
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        renderer.renderSprite(animatedSprite, collider.x, collider.y, xZoom, yZoom);
        // renderer.renderRectangle(collider, 1, 1);
    }

    @Override
    public void update(BombGame game) {
        animatedSprite.update(game);
        if (animatedSprite.isDestroyed()) {
            game.removeGameObject(this);
        }
    }

    @Override
    public void init(BombGame game) {
        game.getPlayers().forEach(player -> {
            player.getCollider().addGameObject(this);
            player.getCollider().checkCollision(collider);
        });
    }
}