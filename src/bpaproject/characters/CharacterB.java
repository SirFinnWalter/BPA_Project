package bpaproject.characters;

import java.awt.Point;
import java.io.File;

import bpaproject.*;
import bpaproject.AnimatedSprite.AnimationType;
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

    private final AnimatedSprite characterAnimatedSprite;
    private final AnimatedSprite characterAnimatedSprite2;

    private boolean invicible = false;
    private int timer = 500;
    private int cooldown = 200;

    public CharacterB() {
        this.characterAnimatedSprite = CHARACTER_B_ANIMATED_SPRITE.clone();
        this.characterAnimatedSprite2 = CHARACTER_B_ANIMATED_SPRITE_2.clone();
        this.currentSprite = characterAnimatedSprite;

        currentSprite.setAnimationType(AnimationType.DESTROY);
    }

    @Override
    public void updateBehaviors(Player player) {
        player.behaviors.bombBehavior = (g, p) -> {
        };
        player.behaviors.updateBehavior = (g, p) -> {
            if (currentSprite.isDestroyed()) {
                invicible = true;
                timer = 0;

                Point mapPoint = g.getMap().mapPointToTilemap(p.getCollider().x + p.getCollider().width / 2,
                        p.getCollider().y + p.getCollider().height / 2);
                Point screenPoint = g.getMap().mapPointToScreen(mapPoint);
                int x = screenPoint.x;
                int y = screenPoint.y;

                Explosion.createExplosion(g, p.bombLength, x, y);

                currentSprite = characterAnimatedSprite2;
                currentSprite.reset();
                p.updateDirection();
            }

            p.updateBehavior.preform(g, p);

            if (p.getListener().action() || p.getListener().bomb())
                p.moving = true;

            if (invicible && timer > 40)
                invicible = false;

            if (currentSprite == characterAnimatedSprite2 && timer > cooldown) {
                currentSprite = characterAnimatedSprite;
                currentSprite.reset();
                p.updateDirection();
            }
            timer++;
        };

        player.behaviors.enterBehavior = (e, p) ->

        {
            p.enterBehavior.preform(e, p);
            if (invicible)
                p.setAlive(true);

            if (e.getSource().getObject() instanceof BombUp) {
                cooldown -= cooldown > 40 ? 20 : 0;
            }
        };

        player.behaviors.stayBehavior = (e, p) -> {
            p.stayBehavior.preform(e, p);
            if (invicible)
                p.setAlive(true);
        };
    }

}