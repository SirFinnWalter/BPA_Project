package bpaproject;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import bpaproject.characters.CharacterBase;
import bpaproject.characters.CharacterBehaviors;
import bpaproject.characters.CharacterBehaviors.Behavior;
import bpaproject.characters.CharacterBehaviors.CollisionBehavior;
import bpaproject.framecontent.Game;
import bpaproject.powerups.Powerup;

/* * * * * * * * * * * * * * * * * 
 * Date Created:  27 Apr 2019    *
 * Created By:    Dakota Taylor  *
 * * * * * * * * * * * * * * * * *
 * Last Modified: 28 Apr 2019    *
 * Modified By:   Dakota Taylor  *
 * * * * * * * * * * * * * * * * */

public class Player implements GameObject, CollisionListener {

    private CharacterBase character;
    private Rectangle renderBox;
    private Collider collider;
    private KeyboardListener listener = null;
    public boolean moving = false;
    public FacingDirection currentFD, newFD;
    private boolean alive;

    public int bombLength = 1;
    public int maxBombs = 1;
    public int bombCount = 0;

    public int bombCounter = 20;
    public int bombCooldown = 20;
    private int playerNum;
    public final CharacterBehaviors behaviors = new CharacterBehaviors();

    public final Behavior bombBehavior = (g, p) -> {
        if (bombCounter >= bombCooldown) {
            bombCounter = 0;
            if (bombCount < maxBombs) {
                Point mapPoint = g.getMap().mapPointToTilemap(collider.x + collider.width / 2,
                        collider.y + collider.height / 2);
                Point screenPoint = g.getMap().mapPointToScreen(mapPoint);
                Bomb bomb = new Bomb(p, screenPoint.x, screenPoint.y, bombLength);
                g.addGameObject(bomb);
                bombCount++;
            }
        }
    };

    public final Behavior updateBehavior = (g, p) -> {
        if (listener.up()) {
            collider.y -= character.getSpeed();
            newFD = FacingDirection.UP;
            moving = true;
        }
        if (listener.down()) {
            collider.y += character.getSpeed();
            newFD = FacingDirection.DOWN;
            moving = true;
        }

        if (moving)
            g.checkCollision(collider);

        if (listener.left()) {
            collider.x -= character.getSpeed();
            newFD = FacingDirection.LEFT;
            moving = true;
        }
        if (listener.right()) {
            collider.x += character.getSpeed();
            newFD = FacingDirection.RIGHT;
            moving = true;
        }

        if (moving) {
            g.checkCollision(collider);

            // TODO: check if null check is needed
            if (character.getSprite() instanceof AnimatedSprite)
                ((AnimatedSprite) character.getSprite()).update();

            renderBox.x = collider.x - ((renderBox.width * GameWindow.ZOOM - collider.width) / 2);
            renderBox.y = collider.y - ((renderBox.height * GameWindow.ZOOM - collider.height));

        } else if (character.getSprite() instanceof AnimatedSprite)
            ((AnimatedSprite) character.getSprite()).reset();

        if (currentFD != newFD) {
            currentFD = newFD;
            updateDirection();
        }
        if (listener.bomb())
            behaviors.bombBehavior.preform(g, p);

        if (listener.action())
            behaviors.actionBehavior.preform(g, p);

        if (!alive)
            g.removeGameObject(p);

        moving = false;

        if (bombCounter < bombCooldown)
            bombCounter++;
    };

    public final CollisionBehavior enterBehavior = (e, p) -> {
        switch (newFD) {
        case LEFT:
            collider.x += e.intersection(collider).width;
            break;
        case RIGHT:
            collider.x -= e.intersection(collider).width;
            break;
        case UP:
            collider.y += e.intersection(collider).height;
            break;
        case DOWN:
            collider.y -= e.intersection(collider).height;
            break;
        default:
            collider.x = renderBox.x;
            collider.y = renderBox.y;
            break;
        }
        Object source = e.getSource().getObject();
        if (source instanceof Explosion) {
            this.alive = false;
        } else if (source instanceof Powerup) {
            ((Powerup) source).applyPower(this);
        } else if (source instanceof GameObject) {
            this.collider.checkCollision(((GameObject) source).getCollider());
        }
    };

    public final CollisionBehavior stayBehavior = (e, p) -> {
        Object source = e.getSource().getObject();
        if (source instanceof Explosion) {
            this.alive = false;
        }
    };
    public final static int MAX_PLAYERS = 4;
    public static int playerCount = 0;

    public Player(int x, int y, CharacterBase character, KeyboardListener controls) {

        behaviors.bombBehavior = bombBehavior;
        behaviors.updateBehavior = updateBehavior;
        behaviors.actionBehavior = (g, p) -> {
        };

        behaviors.enterBehavior = enterBehavior;
        behaviors.stayBehavior = stayBehavior;
        behaviors.leaveBehavior = (e, p) -> {
        };

        character.updateBehaviors(this);

        renderBox = new Rectangle(x * GameWindow.ZOOM, y * GameWindow.ZOOM, character.getSprite().getWidth(),
                character.getSprite().getHeight());
        collider = new Collider(this, x * GameWindow.ZOOM, y * GameWindow.ZOOM, 14 * GameWindow.ZOOM,
                14 * GameWindow.ZOOM);

        renderBox.setColor(0x88FFFFFF);
        collider.setBorder(1, 0xFF0000FF);

        this.listener = controls;
        this.character = character;
        this.alive = true;
        currentFD = FacingDirection.LEFT;
        newFD = FacingDirection.LEFT;

        playerNum = ++playerCount;
        if (playerNum > MAX_PLAYERS) {
            GameWindow.crash("Over max number (" + MAX_PLAYERS + ") of players!");
        }
        updateDirection();
    }

    public void updateDirection() {
        if (character.getSprite() instanceof AnimatedSprite) {
            AnimatedSprite animatedSprite = (AnimatedSprite) character.getSprite();

            int fd = (currentFD == FacingDirection.LEFT || currentFD == FacingDirection.RIGHT) ? currentFD.getValue()
                    : FacingDirection.LEFT.getValue();
            int framesLength = (animatedSprite.getLength() / 2);
            animatedSprite.setAnimationRange(fd * framesLength, fd * framesLength + (framesLength - 1));
        }
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    public Rectangle getRenderBox() {
        return renderBox;
    }

    @Override
    public void update(Game game) {
        behaviors.updateBehavior.preform(game, this);
    }

    @Override
    public void render(RenderHandler renderer, int zoom) {
        renderer.renderSprite(character.getSprite(), renderBox.x, renderBox.y, zoom);
    }

    @Override
    public void init(Game game) {
        game.getPlayers().forEach(player -> {
            if (player != this) {
                collider.addGameObject(player);
                player.collider.addGameObject(this);
            }
        });
    }

    public int getPlayerNum() {
        return this.playerNum;
    }

    public KeyboardListener getListener() {
        return this.listener;
    }

    /**
     * Auto-adjust the location of the {@code collider} based on the intersection of
     * the {@code collider} and the source collider.
     */
    @Override
    public void onCollisionEnter(CollisionEvent e) {
        behaviors.enterBehavior.preform(e, this);
    }

    @Override
    public void onCollisionLeave(CollisionEvent e) {
        behaviors.leaveBehavior.preform(e, this);
    }

    @Override
    public void onCollisionStay(CollisionEvent e) {
        behaviors.stayBehavior.preform(e, this);

    }

    public void increaseBombLength() {
        bombLength++;
    }

    public void increaseMaxBombs() {
        maxBombs++;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Directions the {@code Player} can be facing. Can be refered by name or
     * integer value.
     */
    public enum FacingDirection {
        LEFT(0), RIGHT(1), UP(2), DOWN(3);

        private int value;
        private static Map<Integer, FacingDirection> map = new HashMap<>();

        private FacingDirection(int value) {
            this.value = value;
        }

        static {
            for (FacingDirection fd : FacingDirection.values()) {
                map.put(fd.value, fd);
            }
        }

        public static FacingDirection valueOf(int fd) {
            return (FacingDirection) map.get(fd);
        }

        public int getValue() {
            return value;
        }
    }
}