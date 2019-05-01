package bpaproject.characters;

import java.io.File;

import bpaproject.*;

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

    private final AnimatedSprite characterAnimatedSprite;
    private final AnimatedSprite characterAnimatedSpriteActive;

    private boolean active = false;
    private int duration = 160;
    private int cooldown = duration + 80;
    private int current = cooldown;

    public CharacterA() {
        this.characterAnimatedSprite = CHARACTER_A_ANIMATED_SPRITE.clone();
        this.characterAnimatedSpriteActive = CHARACTER_A_ANIMATED_SPRITE_2.clone();
        this.currentSprite = this.characterAnimatedSprite;
    }

    @Override
    public void updateBehaviors(Player player) {
        player.behaviors.updateBehavior = (g, p) -> {
            p.updateBehavior.preform(g, p);
            if (active && current >= duration) {
                active = false;
                currentSprite = characterAnimatedSprite;
                p.updateDirection();
            }
            current++;
        };

        player.behaviors.actionBehavior = (g, p) -> {
            if (current >= cooldown) {
                current = 0;
                active = true;
                currentSprite = characterAnimatedSpriteActive;
                p.updateDirection();
            }
        };
    }

}