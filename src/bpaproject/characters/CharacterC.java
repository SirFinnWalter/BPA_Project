package bpaproject.characters;

import java.io.File;

import bpaproject.*;
import bpaproject.powerups.Powerup;

/**
 * @file CharacterC.java
 * @author Dakota Taylor
 * @createdOn Monday, 10 December, 2018
 */

public class CharacterC extends CharacterBase {
    private static final AnimatedSprite CHARACTER_C_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\stego.png")), 16, 16), 12);
    private static final AnimatedSprite CHARACTER_C_ANIMATED_SPRITE_2 = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\ghosty_gooster.png")), 16, 16), 12);

    private int timer = 0;
    private boolean invincible;
    private boolean secondLife;

    public CharacterC() {
        super(CHARACTER_C_ANIMATED_SPRITE.clone());
    }

    @Override
    public void updateBehaviors(Player player) {
        player.behaviors.updateBehavior = (g, p) -> {
            p.updateBehavior.preform(g, p);

            if (invincible) {
                timer++;
                if (timer > 300) {
                    invincible = false;
                }
            }
        };

        player.behaviors.enterBehavior = (e, p) -> {
            Collider collider = p.getCollider();
            Rectangle renderBox = p.getRenderBox();
            switch (p.newFD) {
            case UP:
                collider.y += e.intersection(collider).height;
                break;
            case DOWN:
                collider.y -= e.intersection(collider).height;
                break;
            case LEFT:
                collider.x += e.intersection(collider).width;
                break;
            case RIGHT:
                collider.x -= e.intersection(collider).width;
                break;
            default:
                collider.x = renderBox.x;
                collider.y = renderBox.y;
                break;
            }
            Object source = e.getSource().getObject();
            if (source instanceof Explosion) {
                if (!secondLife) {
                    invincible = true;
                    secondLife = true;
                    animatedSprite.reset();
                    this.animatedSprite = CHARACTER_C_ANIMATED_SPRITE_2.clone();

                } else {
                    if (!invincible)
                        p.setAlive(false);
                }
            } else if (source instanceof Powerup) {
                ((Powerup) source).applyPower(p);
            } else if (source instanceof GameObject) {
                collider.checkCollision(((GameObject) source).getCollider());
            }
        };

        player.behaviors.stayBehavior = (e, p) -> {
            Object source = e.getSource().getObject();
            if (source instanceof Explosion) {
                if (!secondLife) {
                    invincible = true;
                    secondLife = true;
                    animatedSprite.reset();
                    this.animatedSprite = (AnimatedSprite) CHARACTER_C_ANIMATED_SPRITE_2.clone();

                } else {
                    if (!invincible)
                        p.setAlive(false);
                }
            }
        };
    }
}