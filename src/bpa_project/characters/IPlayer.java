package bpa_project.characters;

import bpa_project.GameObject;
import bpa_project.BombGame;

/**
 * @file IPlayer.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 05 December, 2018
 */

public interface IPlayer extends GameObject {
    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void placeBomb(BombGame game);

    void useAction(BombGame game);

    int getPlayerNum();
}