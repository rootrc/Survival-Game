package game.dungeon.room.entity;

import java.awt.event.ActionEvent;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.dungeon.Dungeon;
import game.dungeon.inventory.Inventory;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;

public class Player extends Entity {
    private HashSet<String> movementKeys = new HashSet<>();
    private Inventory inventory;
    private RoomObject interactable;
    private Action nextRoom;
    private int interactionCooldown;

    private final Action accelerate = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movementKeys.add(e.getActionCommand());
        }
    };

    private final Action decelerate = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movementKeys.remove(e.getActionCommand());
        }
    };

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

    public Player(Action nextRoom, Inventory inventory) {
        super("playerTileset",
                new CollisionBox(0.5, 1.75, 1, 1),
                new CollisionBox(0, 1.25, 2, 2), Dungeon.TILESIZE / 4, 10, 4);
        this.nextRoom = nextRoom;
        this.inventory = inventory;
        setKeyBinds();
    }

    public void set(double x, double y) {
        setLocation(x, y);
    }

    private void setKeyBinds() {
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed E"), "interact");
        getActionMap().put("interact", interact);
        for (char c : "WASD".toCharArray()) {
            getInputMap(2).put(KeyStroke.getKeyStroke(
                    new StringBuilder("pressed ").append(c).toString()),
                    new StringBuilder("acc ").append(c).toString());
            getInputMap(2).put(KeyStroke.getKeyStroke(
                    new StringBuilder("released ").append(c).toString()),
                    new StringBuilder("decel ").append(c).toString());
            getActionMap().put(new StringBuilder("acc ").append(c).toString(), accelerate);
            getActionMap().put(new StringBuilder("decel ").append(c).toString(), decelerate);
        }
    }

    @Override
    public void update() {
        super.update();
        interactionCooldown++;
    }

    @Override
    public void move() {
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
        super.move();
    }

    public void interaction(Player player) {

    }

    public void setInteractable(RoomObject interactable) {
        this.interactable = interactable;
    }

    public HashSet<String> getMovementKeys() {
        return movementKeys;
    }

    public int getLayer() {
        return 0;
    }

    public Ladder getLadder() {
        return (Ladder) interactable;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
