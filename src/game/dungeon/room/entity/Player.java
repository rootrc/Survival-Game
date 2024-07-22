package game.dungeon.room.entity;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.dungeon.Dungeon;
import game.dungeon.inventory.Inventory;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.settings.DiffSettings;
import game.dungeon.settings.KeyBinds;

public class Player extends Entity {
    private HashSet<String> movementKeys = new HashSet<>();
    private Inventory inventory;
    private ArrayList<RoomObject> interactables = new ArrayList<>();
    private Action nextRoom;
    private int interactionCooldown;

    private double lightAmount;
    private int lightRadiusFactor;
    private int lightDecreaseFactor;
    private int lightDetectionRadiusSquared;

    private final Action accelerate = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movementKeys.add(e.getActionCommand().toLowerCase());
        }
    };

    private final Action decelerate = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movementKeys.remove( e.getActionCommand().toLowerCase());
        }
    };

    private final Action interact = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (interactables == null || interactionCooldown < 30) {
                return;
            }
            interactionCooldown = 0;
            for (RoomObject interactable : interactables) {
                interactable.interaction(Player.this);
                if (interactable instanceof Ladder) {
                    nextRoom.actionPerformed(e);
                }
            }
        }
    };

    public Player(Action nextRoom, Inventory inventory) {
        super("playerTileset",
                new CollisionBox(0.5, 1.75, 1, 1),
                new CollisionBox(0, 1.25, 2, 2), Dungeon.TILESIZE / 4, 10, 4);
        this.nextRoom = nextRoom;
        this.inventory = inventory;
        lightAmount = DiffSettings.playerLightStartAmount;
        lightRadiusFactor = DiffSettings.playerLightRadiusFactor;
        lightDecreaseFactor = DiffSettings.playerLightDecreaseFactor;
        lightDetectionRadiusSquared = DiffSettings.playerLightDetectionRadiusSquared;
        setKeyBinds();
    }

    public void set(double x, double y) {
        setLocation(x, y);
    }

    private void setKeyBinds() {
        getInputMap(2).put(KeyBinds.interact, "interact");
        getActionMap().put("interact", interact);
        getInputMap(2).put(KeyBinds.upPressed, "acc up");
        getInputMap(2).put(KeyBinds.upReleased, "decel up");
        getInputMap(2).put(KeyBinds.leftPressed, "acc left");
        getInputMap(2).put(KeyBinds.leftReleased, "decel left");
        getInputMap(2).put(KeyBinds.downPressed, "acc down");
        getInputMap(2).put(KeyBinds.downReleased, "decel down");
        getInputMap(2).put(KeyBinds.rightPressed, "acc right");
        getInputMap(2).put(KeyBinds.rightReleased, "decel right");
        getActionMap().put("acc up", accelerate);
        getActionMap().put("decel up", decelerate);
        getActionMap().put("acc left", accelerate);
        getActionMap().put("decel left", decelerate);
        getActionMap().put("acc down", accelerate);
        getActionMap().put("decel down", decelerate);
        getActionMap().put("acc right", accelerate);
        getActionMap().put("decel right", decelerate);
    }

    @Override
    public void update() {
        super.update();
        interactionCooldown++;
        setLightRadius(lightRadiusFactor * Math.min(Math.pow(lightAmount, 0.2), Math.sqrt(lightAmount) / 6));
        lightAmount -= Math.log(lightAmount) / lightDecreaseFactor;
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

    public void clearInteractable() {
        interactables.clear();
    }

    public void addInteractable(RoomObject object) {
        interactables.add(object);
    }

    public HashSet<String> getMovementKeys() {
        return movementKeys;
    }

    public Ladder getLadder() {
        for (RoomObject interactable : interactables) {
            interactable.interaction(Player.this);
            if (interactable instanceof Ladder) {
                return (Ladder) interactable;
            }
        }
        return null;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addLightAmount(int delta) {
        lightAmount += delta;
    }

    public int getLightDetectionRadiusSquared() {
        return lightDetectionRadiusSquared;
    }
}
