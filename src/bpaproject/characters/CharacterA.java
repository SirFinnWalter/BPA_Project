package bpaproject.characters;

import java.io.File;

import bpaproject.*;
import bpaproject.framecontent.Game;

/**
 * @file CharacterA.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 05 December, 2018
 */
public class CharacterA extends CharacterBase {
    private static final AnimatedSprite CHARACTER_A_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\d'erp.png")), 16, 16), 18);
    private static final AnimatedSprite CHARACTER_A_ANIMATED_SPRITE_2 = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\d'erp_invisible.png")), 16, 16), 18);

    public CharacterA() {
        super(CHARACTER_A_ANIMATED_SPRITE.clone());
    }

    @Override
    public void updateBehaviors(Player player) {
        player.behaviors.updateBehavior = (g, p) -> {
            p.updateBehavior.preform(g, p);
            if (active && current >= duration) {
                active = false;
                animatedSprite = CHARACTER_A_ANIMATED_SPRITE.clone();
                p.updateDirection();
            }
            current++;
        };

        player.behaviors.actionBehavior = (g, p) -> {
            if (current >= cooldown) {
                current = 0;
                active = true;
                animatedSprite = CHARACTER_A_ANIMATED_SPRITE_2.clone();
                p.updateDirection();
            }
        };
    }

    boolean active = false;
    int current = 160;
    int duration = 80;
    int cooldown = 160;
}