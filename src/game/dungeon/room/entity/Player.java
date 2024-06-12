package game.dungeon.room.entity;

import java.awt.event.ActionEvent;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.dungeon.inventory.Inventory;
import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.game_components.GamePanel;
import game.utilities.ImageUtilities;

public class Player extends Entity {
    private Inventory inventory;
    private RoomObject interactable;

    private Action nextRoom;

    public Player(Action nextRoom, Inventory inventory) {
        // TEMP
        super(ImageUtilities.getImage("entities", "player"),
                new CollisionBox(0, 0, 1, 1),
                new CollisionBox(-0.25, -0.25, 1.5, 1.5), GamePanel.TILESIZE / 4, null);
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

    private static final double a = Math.sqrt(2) / 2;
    private static final double acc = 10.0;
    private static final double deacc = 4.0;

    public void update() {
        interactionCooldown++;
        move();
    }

    private void move() {
        double ax = 0;
        double ay = 0;
        if (movementKeys.contains("w") && Math.abs(speedY) < maxSpeed) {
            ay -= maxSpeed / acc;
        }
        if (movementKeys.contains("a") && Math.abs(speedX) < maxSpeed) {
            ax -= maxSpeed / acc;
        }
        if (movementKeys.contains("s") && Math.abs(speedY) < maxSpeed) {
            ay += maxSpeed / acc;
        }
        if (movementKeys.contains("d") && Math.abs(speedX) < maxSpeed) {
            ax += maxSpeed / acc;
        }
        if (ax == 0) {
            if (speedX > 0) {
                speedX = Math.max(speedX - maxSpeed / deacc, 0);
            } else if (speedX < 0) {
                speedX = Math.min(speedX + maxSpeed / deacc, 0);
            }
        }
        if (ay == 0) {
            if (speedY > 0) {
                speedY = Math.max(speedY - maxSpeed / deacc, 0);
            } else if (speedY < 0) {
                speedY = Math.min(speedY + maxSpeed / deacc, 0);
            }
        }
        speedX += ax;
        speedY += ay;
        if (collision.checkTile(this, speedX, 0)) {
            if (ax != 0 && ay != 0) {
                moveX(speedX * a);
            } else {
                moveX(speedX);
            }
        }
        if (collision.checkTile(this, 0, speedY)) {
            if (ax != 0 && ay != 0) {
                moveY(speedY * a);
            } else {
                moveY(speedY);
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

    public void collide(RoomObject o) {
        // TODO
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
