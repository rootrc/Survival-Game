package game.dungeon.room.entity;

import java.awt.event.ActionEvent;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.dungeon.Dungeon;
import game.dungeon.inventory.Inventory;
import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.utilities.ImageUtilities;

public class Player extends Entity {
    private Inventory inventory;
    private RoomObject interactable;
    private Action nextRoom;

    public Player(Action nextRoom, Inventory inventory) {
        // TEMP
        super(ImageUtilities.getImage("entities", "player"),
                new CollisionBox(0, 0, 1, 1),
                new CollisionBox(-0.25, -0.25, 1.5, 1.5), Dungeon.TILESIZE / 4, null, 10, 4);
        this.nextRoom = nextRoom;
        this.inventory = inventory;
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed E"), "interact");
        getActionMap().put("interact", interact);
    }

    public void set(double x, double y, CollisionChecker collision) {
        setLocation(x, y);
        this.collision = collision;
    }

    HashSet<String> movementKeys = new HashSet<>();

    public final Action accelerate = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movementKeys.add(e.getActionCommand());
        }
    };

    public final Action decelerate = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movementKeys.remove(e.getActionCommand());
        }
    };

    public void update() {
        interactionCooldown++;
        move();
    }

    private void move() {
        double ax = 0;
        double ay = 0;
        if (movementKeys.contains("w") && Math.abs(getSpeedY()) < getMaxSpeed()) {
            ay -= getAcc();
        }
        if (movementKeys.contains("a") && Math.abs(getSpeedX()) < getMaxSpeed()) {
            ax -= getAcc();
        }
        if (movementKeys.contains("s") && Math.abs(getSpeedY()) < getMaxSpeed()) {
            ay += getAcc();
        }
        if (movementKeys.contains("d") && Math.abs(getSpeedX()) < getMaxSpeed()) {
            ax += getAcc();
        }
        if (ax == 0) {
            if (getSpeedX() > 0) {
                setSpeedX(Math.max(getSpeedX() - getDeacc(), 0));
            } else if (getSpeedX() < 0) {
                setSpeedX(Math.min(getSpeedX() + getDeacc(), 0));
            }
        }
        if (ay == 0) {
            if (getSpeedY() > 0) {
                setSpeedY(Math.max(getSpeedY() - getDeacc(), 0));
            } else if (getSpeedY() < 0) {
                setSpeedY(Math.min(getSpeedY() + getDeacc(), 0));
            }
        }
        addSpeedX(ax);
        addSpeedY(ay);
        if (collision.checkTile(this, getSpeedX(), 0)) {
            if (ax != 0 && ay != 0) {
                moveX(getSpeedX() * a);
            } else {
                moveX(getSpeedX());
            }
        }
        if (collision.checkTile(this, 0, getSpeedY())) {
            if (ax != 0 && ay != 0) {
                moveY(getSpeedY() * a);
            } else {
                moveY(getSpeedY());
            }
        }
    }

    private int interactionCooldown;

    private final Action interact = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (interactable == null || interactionCooldown < 30) {
                return;
            }
            interactionCooldown = 0;
            interactable.interaction(Player.this);
            if (interactable.getClass() == Ladder.class) {
                nextRoom.actionPerformed(e);
            }
        }
    };

    public void collide(RoomObject object) {
        CollisionBox h1 = getHitBox();
        CollisionBox h2 = object.getHitBox();
        if (h1.getMinX() + getX() < h2.getMinX() + object.getX()
                && h1.getMaxX() + getX() - h2.getMinX() - object.getX() < getSpeedX()
                && (getSpeedX() > 0 || movementKeys.contains("d"))) {
            setX(h2.getMinX() + object.getX() - h1.getMaxX());
            setSpeedX(0);
        }
        if (h1.getMinX() + getX() - h2.getMaxX() - object.getX() > getSpeedX()
                && h2.getMaxX() + object.getX() < h1.getMaxX() + getX() && (getSpeedX() < 0 || movementKeys.contains("a"))) {
            setX(h2.getMaxX() + object.getX() - h1.getMinX());
            setSpeedX(0);
        }
        if (h1.getMinY() + getY() < h2.getMinY() + object.getY()
                && h1.getMaxY() + getY() - h2.getMinY() - object.getY() < getSpeedY()
                && (getSpeedY() > 0 || movementKeys.contains("s"))) {
            setY(h2.getMinY() + object.getY() - h1.getMaxY());
            setSpeedY(0);
        }
        if (h1.getMinY() + getY() - h2.getMaxY() - object.getY() > getSpeedY()
                && h2.getMaxY() + object.getY() < h1.getMaxY() + getY() && (getSpeedY() < 0 || movementKeys.contains("w"))) {
            setY(h2.getMaxY() + object.getY() - h1.getMinY());
            setSpeedY(0);
        }
    }

    public void interaction(Player player) {

    }

    public void setInteractable(RoomObject interactable) {
        this.interactable = interactable;
    }

    public Ladder getLadder() {
        return (Ladder) interactable;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
