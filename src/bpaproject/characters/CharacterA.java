package bpaproject.characters;

import java.io.File;

import bpaproject.*;
import bpaproject.framecontent.Game;

/**
 * @file CharacterA.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 05 December, 2018
 */
public class CharacterA extends Player {
    private static final AnimatedSprite CHARACTER_A_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\d'erp.png")), 16, 16), 18);

    //
    public CharacterA(int x, int y, KeyboardListener listener) {
        super(x, y, CHARACTER_A_ANIMATED_SPRITE.clone(), listener);
    }

    @Override
    public void update(Game game) {
        super.update(game);
        if (!sprite.isVisible() && current >= duration)
            sprite.setVisible(true);

        current++;
    }

    boolean active = false;
    int current = 500;
    int duration = 80;
    int cooldown = 160;

    @Override
    public void useAction(Game game) {
        if (current >= cooldown) {
            current = 0;
            sprite.setVisible(false);
        }
    }
}