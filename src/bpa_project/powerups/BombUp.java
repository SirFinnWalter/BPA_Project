package bpa_project.powerups;

import java.io.File;

import bpa_project.*;
import bpa_project.characters.Player;

/**
 * @file BombUp.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 23 January, 2019
 */

public class BombUp extends Powerup {
    private static final Sprite BLASTUP_SPRITE = new Sprite(
            GameWindow.loadImage(new File("assets\\sprites\\bomb-up.png")));

    public BombUp(int x, int y) {
        super(x, y, BLASTUP_SPRITE.clone());
    }

    @Override
    public void applyPower(Player player) {
        player.increaseMaxBombs();
        this.destroyed = true;
    }
}