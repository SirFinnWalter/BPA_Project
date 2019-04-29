package bpaproject.characters;

import bpaproject.CollisionEvent;
import bpaproject.Player;
import bpaproject.framecontent.Game;

/* * * * * * * * * * * * * * * * * 
 * Date Created:  27 Apr 2019    *
 * Created By:    Dakota Taylor  *
 * * * * * * * * * * * * * * * * *
 * Last Modified: 27 Apr 2019    *
 * Modified By:   Dakota Taylor  *
 * * * * * * * * * * * * * * * * */

public class CharacterBehaviors {
    public Behavior bombBehavior;
    public Behavior actionBehavior;
    public Behavior updateBehavior;

    public CollisionBehavior enterBehavior;
    public CollisionBehavior stayBehavior;
    public CollisionBehavior leaveBehavior;

    @FunctionalInterface
    public interface Behavior {
        public void preform(Game game, Player player);
    }

    @FunctionalInterface
    public interface CollisionBehavior {
        public void preform(CollisionEvent e, Player player);
    }
}