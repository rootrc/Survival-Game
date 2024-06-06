package core.dungeon.room.objects.entity;

import java.awt.event.ActionEvent;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.mechanics.inventory.HotbarManager;
import core.dungeon.room.objects.Ladder;
import core.dungeon.room.objects.object_utilities.CollisionBox;
import core.dungeon.room.objects.object_utilities.RoomObject;
import core.utilities.ImageUtilities;
import core.window.GamePanel;

public class Player extends Entity {
    private HotbarManager inventory;
    private Ladder ladder;
    private RoomObject interactable;

    public Player(HotbarManager inventory) {
        // TEMP
        super(ImageUtilities.getImage("entities", "player"),
                new CollisionBox(0, 0, 1, 1),
                new CollisionBox(-0.25, -0.25, 1.5, 1.5), GamePanel.TILESIZE / 4, null);
        this.inventory = inventory;
    }

    public void set(double x, double y, CollisionChecker collision) {
        setX(x);
        setY(y);
        this.collision = collision;
        ladder = null;
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

    private final double a = Math.sqrt(2) / 2;
    private final double acc = 10.0;
    private final double deacc = 4.0;

    public void update() {
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

    public final Action interact = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (interactable != null) {
                interactable.interaction(Player.this);
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
        return ladder;
    }

    public void setLadder(Ladder ladder) {
        this.ladder = ladder;
    }
}
