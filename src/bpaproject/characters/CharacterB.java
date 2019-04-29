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

public class CharacterB extends CharacterBase {
    private static final AnimatedSprite CHARACTER_B_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\bombiboi.png")), 16, 16), 7);
    private static final AnimatedSprite CHARACTER_B_ANIMATED_SPRITE_2 = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\bombiboi_cooldown.png")), 16, 16), 7);

    private boolean invicible;
    private int timer;
    private int cooldown;

    public CharacterB() {
        super(CHARACTER_B_ANIMATED_SPRITE.clone());
        animatedSprite.setAnimationType(AnimationType.DESTROY);
        cooldown = 200;
    }

    @Override
    public void updateBehaviors(Player player) {
        player.behaviors.bombBehavior = (g, p) -> {
        };
        player.behaviors.updateBehavior = (g, p) -> {
            if (animatedSprite.isDestroyed()) {
                invicible = true;
                timer = 0;

                Point mapPoint = g.getMap().mapPointToTilemap(p.getCollider().x + p.getCollider().width / 2,
                        p.getCollider().y + p.getCollider().height / 2);
                Point screenPoint = g.getMap().mapPointToScreen(mapPoint);
                int x = screenPoint.x;
                int y = screenPoint.y;

                Explosion.createExplosion(g, p.bombLength, x, y);

                animatedSprite = CHARACTER_B_ANIMATED_SPRITE_2.clone();
                animatedSprite.setAnimationType(AnimationType.LOOPING);
                animatedSprite.reset();
                p.updateDirection();
            }

            p.updateBehavior.preform(g, p);

            if (p.getListener().action() || p.getListener().bomb())
                p.moving = true;

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
                    p.updateDirection();
                    timer = 0;
                }
            }
        };

        player.behaviors.enterBehavior = (e, p) -> {
            p.enterBehavior.preform(e, p);
            if (invicible)
                p.setAlive(true);

            if (e.getSource().getObject() instanceof BombUp)
                cooldown -= 20;
        };

        player.behaviors.stayBehavior = (e, p) -> {
            p.stayBehavior.preform(e, p);
            if (invicible)
                p.setAlive(true);
        };
    }

}