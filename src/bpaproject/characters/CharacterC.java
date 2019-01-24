package bpaproject.characters;

import java.io.File;

import bpaproject.*;
import bpaproject.powerups.Powerup;
import bpaproject.framecontent.Game;

/**
 * @file CharacterC.java
 * @author Dakota Taylor
 * @createdOn Monday, 10 December, 2018
 */

public class CharacterC extends Player {
    private static final AnimatedSprite CHARACTER_C_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\stego.png")), 16, 16), 12);
    private static final AnimatedSprite CHARACTER_C_ANIMATED_SPRITE_2 = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\ghosty_gooster.png")), 16, 16), 12);

    private int timer = 0;
    private boolean invincible;
    private boolean secondLife;

    public CharacterC(int x, int y, KeyboardListener listener) {
        super(x, y, CHARACTER_C_ANIMATED_SPRITE.clone(), listener);
    }

    @Override
    public void update(Game game) {
        super.update(game);
        if (invincible) {
            timer++;
            if (timer > 300) {
                invincible = false;
            }
        }
    }

    @Override
    public void onCollisionEnter(CollisionEvent e) {
        switch (newFD) {
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
            collider.x = playerBox.x;
            collider.y = playerBox.y;
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
                    setDestroyed(true);
            }
        } else if (source instanceof Powerup) {
            ((Powerup) source).applyPower(this);
        } else if (source instanceof GameObject) {
            this.collider.checkCollision(((GameObject) source).getCollider());
        }
    }

    @Override
    public void onCollisionStay(CollisionEvent e) {
        Object source = e.getSource().getObject();
        if (source instanceof Explosion) {
            if (!secondLife) {
                invincible = true;
                secondLife = true;
                animatedSprite.reset();
                this.animatedSprite = (AnimatedSprite) CHARACTER_C_ANIMATED_SPRITE_2.clone();

            } else {
                if (!invincible)
                    setDestroyed(true);
            }
        }
    }
}