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
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\tempDinosaur.png")), 16, 21), 8);

    public CharacterA(int x, int y, KeyboardListener listener) throws CloneNotSupportedException {
        super(x, y, (AnimatedSprite) CHARACTER_A_ANIMATED_SPRITE.clone(), listener);
    }

    @Override
    public void placeBomb(BombGame game) {
        if (bombCount < maxBombs) {
            Collider collider = this.getCollider();
            Point mapPoint = BombGame.MAP.mapPointToTilemap(collider.x + collider.width / 2,
                    collider.y + collider.height / 2);
            Point screenPoint = BombGame.MAP.mapPointToScreen(mapPoint);
            Bomb bomb = new Bomb(this, screenPoint.x, screenPoint.y, 6);
            game.addGameObject(bomb);
            bombCount++;
        }
    }

}