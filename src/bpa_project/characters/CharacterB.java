package bpa_project.characters;

import java.awt.Point;
import java.io.File;

import bpa_project.*;
import bpa_project.AnimatedSprite.AnimationType;

/**
 * @file CharacterB.java
 * @author Dakota Taylor
 * @createdOn Thursday, 06 December, 2018
 */

public class CharacterB extends Player {
    private static final AnimatedSprite CHARACTER_B_ANIMATED_SPRITE = new AnimatedSprite(
            new SpriteSheet(BombGame.loadImage(new File("assets\\sprites\\test.png")), 16, 16), 20);
    private int initialX, initialY;

    public CharacterB(int x, int y, KeyboardListener listener) throws CloneNotSupportedException {
        super(x, y, (AnimatedSprite) CHARACTER_B_ANIMATED_SPRITE.clone(), listener);
        this.initialX = x;
        this.initialY = y;
        animatedSprite.setAnimationType(AnimationType.destroy);
    }

    @Override
    public void placeBomb(BombGame game) {

    }

    @Override
    public void update(BombGame game) {
        if (animatedSprite.isDestroyed()) {
            try {

                Point mapPoint = BombGame.MAP.mapPointToTilemap(collider.x + collider.width / 2,
                        collider.y + collider.height / 2);
                Point screenPoint = BombGame.MAP.mapPointToScreen(mapPoint);
                int x = screenPoint.x;
                int y = screenPoint.y;

                Bomb.createSegments(game, Bomb.EXPLOSION_ANIMATED_SPRITE, 1, x, y, 0, 0);
                Bomb.createSegments(game, Bomb.EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, bombLength, x, y,
                        -16 * BombGame.XZOOM, 0);
                Bomb.createSegments(game, Bomb.EXPLOSION_ANIMATED_SPRITE_HORTIZONTAL, bombLength, x, y,
                        16 * BombGame.XZOOM, 0);
                Bomb.createSegments(game, Bomb.EXPLOSION_ANIMATED_SPRITE_VERTICAL, bombLength, x, y, 0,
                        -16 * BombGame.YZOOM);
                Bomb.createSegments(game, Bomb.EXPLOSION_ANIMATED_SPRITE_VERTICAL, bombLength, x, y, 0,
                        16 * BombGame.YZOOM);

                // this.collider.x = 16 * 23;
                // this.collider.y = 16;
                this.collider.x = initialX * BombGame.XZOOM;
                this.collider.y = initialY * BombGame.YZOOM;
                animatedSprite.reset();
                this.setDestroyed(false);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        super.update(game);

    }
}