package bpaproject.characters;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import bpaproject.*;
import bpaproject.powerups.Powerup;
import bpaproject.framecontent.Game;

/**
 * @file Player.java
 * @author Dakota Taylor
 * @createdOn Sunday, 14 October, 2018
 */

/**
 * A {@code Player} is instance of {@code GameObject} and
 * {@code CollisionListener} that the user controls. The contructor allows you
 * to set the keys to control the player once.
 * <p>
 * The {@code Player} can take in an instance of {@code Sprite} to render in
 * place of the player box. If no instance of {@code Sprite} is passed, then the
 * renderer will default to rendering the {@code Player} player box. The
 * {@code Player} will not allow movement on collision with its collider.
 */
public abstract class CharacterBase {
    private int speed = 1 * GameWindow.ZOOM;
    protected Sprite sprite;
    protected AnimatedSprite animatedSprite = null;

    /**
     * Constructs a {@code Player} at the upper-left bound of {@code (x,y)} and
     * whose sprite and listener is specified by the argument with the same name.
     * <p>
     * If the sprite passed is an instance of {@code AnimatedSprite}, then sets the
     * animatedSprite of the {@code Player} to the sprite and renders the
     * animatedSprite instead. If the sprite passed is {@code null}, renders a
     * transparent white box at the upper-left bound of {@code (x,y)}.
     * 
     * @param x      The specified X coordinate
     * @param y      The specified Y coordinate
     * @param sprite The sprite render at the {@code Player{@code  location @param
     *               listener The listener to control the player movement
     */
    public CharacterBase(Sprite sprite) {
        this.sprite = sprite;
        if (sprite != null && sprite instanceof AnimatedSprite) {
            this.animatedSprite = (AnimatedSprite) sprite;
        }
    }

    public Sprite getSprite() {
        if (animatedSprite != null)
            return animatedSprite;
        else if (sprite != null)
            return sprite;
        else
            return null;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void updateBehaviors(Player player) {

    }
}