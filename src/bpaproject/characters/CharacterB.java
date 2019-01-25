package bpaproject.characters;

import java.awt.Point;
import java.io.File;

import bpaproject.*;
import bpaproject.AnimatedSprite.AnimationType;
import bpaproject.framecontent.Game;
import bpaproject.powerups.BombUp;

/**
 * @file CharacterB.java
 * @author Dakota Taylor
 * @createdOn Thursday, 06 December, 2018
 */

public class CharacterB extends Player {
    private static final AnimatedSprite CHARACTER_B_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\bombiboi.png")), 16, 16), 7);
    private static final AnimatedSprite CHARACTER_B_ANIMATED_SPRITE_2 = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\bombiboi_cooldown.png")), 16, 16), 7);

    public CharacterB(int x, int y, KeyboardListener listener) {
        super(x, y, CHARACTER_B_ANIMATED_SPRITE.clone(), listener);
        animatedSprite.setAnimationType(AnimationType.DESTROY);
        cooldown = 200;
    }

    /**
     * {@code CharacterB} cannot place bombs
     */
    @Override
    public void placeBomb(Game game) {
    }

    @Override
    public void useAction(Game game) {
    }

    private boolean invicible;
    private int timer;
    private int cooldown;

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

            Explosion.createExplosion(game, bombLength, x, y);

            animatedSprite = CHARACTER_B_ANIMATED_SPRITE_2.clone();
            animatedSprite.setAnimationType(AnimationType.LOOPING);
            animatedSprite.reset();
            super.updateDirection();
        }
        super.update(game);

        if (getListener().action() || getListener().bomb())
            moving = true;

        if (invicible) {
            if (timer > 40) {
                invicible = false;
            }
        }
        if (animatedSprite.getAnimationType() == AnimationType.LOOPING) {
            timer++;
            if (timer > cooldown) {
                animatedSprite = CHARACTER_B_ANIMATED_SPRITE.clone();
                animatedSprite.setAnimationType(AnimationType.DESTROY);
                animatedSprite.reset();
                super.updateDirection();
                timer = 0;
            }
        }

    }

    @Override
    public void onCollisionEnter(CollisionEvent e) {
        super.onCollisionEnter(e);
        if (invicible)
            setDestroyed(false);

        if (e.getSource().getObject() instanceof BombUp) {
            cooldown -= 20;
        }
    }

    @Override
    public void onCollisionStay(CollisionEvent e) {
        super.onCollisionStay(e);
        if (invicible)
            setDestroyed(false);
    }
}