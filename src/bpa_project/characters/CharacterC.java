package bpa_project.characters;

import java.io.File;

import bpa_project.*;

/**
 * @file CharacterC.java
 * @author Dakota Taylor
 * @createdOn Monday, 10 December, 2018
 */

public class CharacterC extends Player {
    private static final AnimatedSprite CHARACTER_C_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\stegowalkTEMP.png")), 16, 16), 20);
    private static final AnimatedSprite CHARACTER_C_ANIMATED_SPRITE_2 = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\ghostygoosterwalk.png")), 16, 16), 20);

    private int initialX, initialY;
    private boolean secondLife;

    public CharacterC(int x, int y, KeyboardListener listener) throws CloneNotSupportedException {
        super(x, y, (AnimatedSprite) CHARACTER_C_ANIMATED_SPRITE.clone(), listener);
        this.initialX = x;
        this.initialY = y;
    }

    @Override
    public void update(Game game) {
        if (this.isDestroyed() && !secondLife) {
            this.collider.x = initialX * GameWindow.ZOOM;
            this.collider.y = initialY * GameWindow.ZOOM;
            this.setDestroyed(false);
            secondLife = true;
            animatedSprite.reset();

            try {
                this.animatedSprite = (AnimatedSprite) CHARACTER_C_ANIMATED_SPRITE_2.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            // FIXME: Update position
            game.checkCollision(this.collider);
        }
        super.update(game);
    }
}