package bpa_project.characters;

import java.awt.Point;
import java.io.File;

import bpa_project.*;

/**
 * @file CharacterA.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 05 December, 2018
 */
public class CharacterA extends Player {
    private static final AnimatedSprite CHARACTER_A_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(GameWindow.loadImage(new File("assets\\sprites\\d.png")), 16, 16), 18);

    public CharacterA(int x, int y, KeyboardListener listener) throws CloneNotSupportedException {
        super(x, y, (AnimatedSprite) CHARACTER_A_ANIMATED_SPRITE.clone(), listener);
    }

    @Override
    public void update(Game game) {
        super.update(game);
        if (!animatedSprite.isVisible() && current >= duration)
            animatedSprite.setVisible(true);

        current++;
    }

    @Override
    public void placeBomb(Game game) {
        if (bombCount < maxBombs) {
            Collider collider = this.getCollider();
            Point mapPoint = game.getMap().mapPointToTilemap(collider.x + collider.width / 2,
                    collider.y + collider.height / 2);
            Point screenPoint = game.getMap().mapPointToScreen(mapPoint);
            Bomb bomb = new Bomb(this, screenPoint.x, screenPoint.y, bombLength);
            game.addGameObject(bomb);
            bombCount++;
        }
    }

    boolean active = false;
    int current = 500;
    int duration = 80;
    int cooldown = 160;

    @Override
    public void useAction(Game game) {
        if (current >= cooldown) {
            current = 0;
            animatedSprite.setVisible(false);
        }
    }
}