package bpa_project.characters;

import bpa_project.GameObject;
import bpa_project.Game;;

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

    void placeBomb(Game game);

    void useAction(Game game);

    int getPlayerNum();
}