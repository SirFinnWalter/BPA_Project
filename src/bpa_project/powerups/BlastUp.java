package bpa_project.powerups;

import java.io.File;

import bpa_project.*;
import bpa_project.characters.Player;

/**
 * @file BlastUp.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 22 January, 2019
 */

public class BlastUp extends Powerup {
    private static final Sprite BLASTUP_SPRITE = new Sprite(
            GameWindow.loadImage(new File("assets\\sprites\\blast-up.png")));

    public BlastUp(int x, int y) {
        super(x, y, BLASTUP_SPRITE.clone());
    }

    @Override
    public void applyPower(Player player) {
        player.increaseBombLength();
        this.destroyed = true;
    }
}