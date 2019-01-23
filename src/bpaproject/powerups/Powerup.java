package bpaproject.powerups;

import bpaproject.*;
import bpaproject.Tilemap.MappedTile;
import bpaproject.characters.Player;
import bpaproject.framecontent.Game;;

/**
 * @file Powerup.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 22 January, 2019
 */

public abstract class Powerup implements GameObject {
    protected Collider collider;
    private Sprite sprite = null;
    private AnimatedSprite animatedSprite = null;
    private boolean visible;
    protected boolean destroyed;

    public Powerup(int x, int y, Sprite sprite) {
        this.sprite = sprite;
        this.visible = false;
        this.destroyed = false;

        if (sprite != null && sprite instanceof AnimatedSprite) {
            this.animatedSprite = (AnimatedSprite) sprite;
        }
        collider = new Collider(this, x * GameWindow.ZOOM + 1 * GameWindow.ZOOM,
                y * GameWindow.ZOOM + 1 * GameWindow.ZOOM, 14 * GameWindow.ZOOM, 14 * GameWindow.ZOOM);
        collider.setBorder(1, 0xFF0000FF);
    }

    @Override
    public void update(Game game) {
        if (animatedSprite != null)
            animatedSprite.update(game);

        if (destroyed)
            game.removeGameObject(this);
    }

    @Override
    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        if (visible) {
            if (animatedSprite != null)
                renderer.renderSprite(animatedSprite, collider.x, collider.y, xZoom, yZoom);
            else if (sprite != null)
                renderer.renderSprite(sprite, collider.x, collider.y, xZoom, yZoom);
            else
                renderer.renderRectangle(collider, 1, 1);
        }
    }

    @Override
    public void init(Game game) {
        game.getPlayers().forEach(player -> {
            player.getCollider().addGameObject(this);
        });
    }

    public void applyPower(Player player) {
        throw new UnsupportedOperationException("Method not implemented yet!");
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Collider getCollider() {
        return this.collider;
    }
}
