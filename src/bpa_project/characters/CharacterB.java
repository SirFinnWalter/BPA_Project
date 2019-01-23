package bpa_project.characters;

import java.awt.Point;
import java.io.File;

import bpa_project.*;
import bpa_project.AnimatedSprite.AnimationType;

/**
 * @file CharacterB.java
 * @author Dakota Taylor
 * @createdOn Thursday, 06 December, 2018
 */

public class CharacterB extends Player {
    private static final AnimatedSprite CHARACTER_B_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\bombiboi.png")), 16, 16), 8);
    private int initialX, initialY;

    public CharacterB(int x, int y, KeyboardListener listener) {
        super(x, y, CHARACTER_B_ANIMATED_SPRITE.clone(), listener);
        this.initialX = x;
        this.initialY = y;
        animatedSprite.setAnimationType(AnimationType.destroy);
    }

    @Override
    public void placeBomb(Game game) {

    }

    @Override
    public void useAction(Game game) {
    }

    private boolean invicible;
    private int timer;

    @Override
    public void update(Game game) {
        if (animatedSprite.isDestroyed()) {
            invicible = true;
            timer = 0;

            Point mapPoint = game.getMap().mapPointToTilemap(collider.x + collider.width / 2,
                    collider.y + collider.height / 2);
            Point screenPoint = game.getMap().mapPointToScreen(mapPoint);
            int x = screenPoint.x;
            int y = screenPoint.y;

            Bomb.createSegments(game, Explosion.EXPLOSION_ANIMATED_SPRITE, 1, x, y, 0, 0);
            Bomb.createSegments(game, Explosion.EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, bombLength, x, y,
                    -16 * GameWindow.ZOOM, 0);
            Bomb.createSegments(game, Explosion.EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, bombLength, x, y,
                    16 * GameWindow.ZOOM, 0);
            Bomb.createSegments(game, Explosion.EXPLOSION_ANIMATED_SPRITE_VERTICAL, bombLength, x, y, 0,
                    -16 * GameWindow.ZOOM);
            Bomb.createSegments(game, Explosion.EXPLOSION_ANIMATED_SPRITE_VERTICAL, bombLength, x, y, 0,
                    16 * GameWindow.ZOOM);

            this.collider.x = initialX * GameWindow.ZOOM;
            this.collider.y = initialY * GameWindow.ZOOM;
            animatedSprite.reset();
            this.setDestroyed(false);
        }
        super.update(game);
        if (getListener().action() || getListener().bomb())
            moving = true;
        if (invicible) {
            timer++;
            if (timer > 40) {
                invicible = false;
                timer = 0;
            }
        }
    }

    @Override
    public void onCollisionEnter(CollisionEvent e) {
        super.onCollisionEnter(e);
        if (invicible)
            setDestroyed(false);
    }

    @Override
    public void onCollisionStay(CollisionEvent e) {
        super.onCollisionStay(e);
        if (invicible)
            setDestroyed(false);
    }
}