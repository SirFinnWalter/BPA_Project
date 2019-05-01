package bpaproject.characters;

import bpaproject.*;

/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

public abstract class CharacterBase {
    private int speed = 1 * GameWindow.ZOOM;
    protected AnimatedSprite currentSprite;

    public AnimatedSprite getSprite() {
        return this.currentSprite;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void updateBehaviors(Player player) {

    }
}