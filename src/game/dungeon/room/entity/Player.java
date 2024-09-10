package game.dungeon.room.entity;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.inventory.Inventory;
import game.dungeon.room.Room;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.DirectionUtilities;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;
import game.dungeon.settings.DiffSettings;
import game.dungeon.settings.KeyBinds;
import game.utilities.ImageUtilities;

public class Player extends Entity {
    private boolean movingUp;
    private boolean movingLeft;
    private boolean movingDown;
    private boolean movingRight;
    private Inventory inventory;
    private ArrayList<RoomObject> interactables = new ArrayList<>();
    private Action nextRoom;

    private double lightAmount;
    private int lightRadiusFactor;
    private int lightDecreaseFactor;
    private int lightDetectionRadiusSquared;

    private int interactionCooldown;
    private int dashCooldown;

    private final Action interact = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (interactables == null || interactionCooldown < 30) {
                return;
            }
            interactionCooldown = 0;
            for (int i = 0; i < interactables.size(); i++) {
                RoomObject interactable = interactables.get(i);
                interactable.interaction(Player.this);
                if (interactable instanceof Ladder) {
                    nextRoom.actionPerformed(e);
                }
            }
        }
    };

    public Player(Action nextRoom, Inventory inventory) {
        super(new SpriteSheet(ImageUtilities.getImage("entities", "playerTileset"), 4, 8, 8),
                new CollisionBox(0.5, 1.75, 1, 1),
                new CollisionBox(0, 1.25, 2, 2), 1 * Dungeon.TILESIZE / 4, 10, 4);
        this.nextRoom = nextRoom;
        this.inventory = inventory;
        lightAmount = DiffSettings.playerLightStartAmount;
        lightRadiusFactor = DiffSettings.playerLightRadiusFactor;
        lightDecreaseFactor = DiffSettings.playerLightDecreaseFactor;
        lightDetectionRadiusSquared = DiffSettings.playerLightDetectionRadiusSquared;
        setKeyBinds();
        interactionCooldown = 1000;
        dashCooldown = 1000;
    }

    public void set(double x, double y) {
        setLocation(x, y);
    }

    private void setKeyBinds() {
        getInputMap(2).put(KeyBinds.interact, "interact");
        getActionMap().put("interact", interact);
        getInputMap(2).put(KeyBinds.dash, "dash");
        getActionMap().put("dash", dash);
        getInputMap(2).put(KeyBinds.upPressed, "acc up");
        getInputMap(2).put(KeyBinds.upReleased, "decel up");
        getInputMap(2).put(KeyBinds.leftPressed, "acc left");
        getInputMap(2).put(KeyBinds.leftReleased, "decel left");
        getInputMap(2).put(KeyBinds.downPressed, "acc down");
        getInputMap(2).put(KeyBinds.downReleased, "decel down");
        getInputMap(2).put(KeyBinds.rightPressed, "acc right");
        getInputMap(2).put(KeyBinds.rightReleased, "decel right");
        getActionMap().put("acc up", accelerateUp);
        getActionMap().put("decel up", decelerateUp);
        getActionMap().put("acc left", accelerateLeft);
        getActionMap().put("decel left", decelerateLeft);
        getActionMap().put("acc down", accelerateDown);
        getActionMap().put("decel down", decelerateDown);
        getActionMap().put("acc right", accelerateRight);
        getActionMap().put("decel right", decelerateRight);
    }

    @Override
    public void update() {
        interactionCooldown++;
        dashCooldown++;
        setLightRadius(lightRadiusFactor * Math.min(Math.pow(lightAmount, 0.2), Math.sqrt(lightAmount) / 6));
        lightAmount -= Math.log(lightAmount) / lightDecreaseFactor;
        if (isMoving()) {
            getSpriteSheet().next();
        } else {
            getSpriteSheet().setFrame(getSpriteSheet().getFrame() / 2);
        }

        if (dashCooldown < 10) {
            setSpeedX(getMaxSpeed() * DirectionUtilities.getXMovement(Player.this));
            setSpeedY(getMaxSpeed() * DirectionUtilities.getYMovement(Player.this));
        } else {
            setDirection(DirectionUtilities.getMovingDirection(movingUp, movingLeft, movingDown, movingRight));
        }
        if (dashCooldown == 10) {
            setMaxSpeed(getMaxSpeed() / 3);
        }
        super.update();
    }

    @Override
    public void move() {
        if (dashCooldown < 4) {
            return;
        }
        double ax = 0;
        double ay = 0;
        if (movingUp && Math.abs(getSpeedY()) < getMaxSpeed()) {
            ay -= getAcc();
        }
        if (movingLeft && Math.abs(getSpeedX()) < getMaxSpeed()) {
            ax -= getAcc();
        }
        if (movingDown && Math.abs(getSpeedY()) < getMaxSpeed()) {
            ay += getAcc();
        }
        if (movingRight && Math.abs(getSpeedX()) < getMaxSpeed()) {
            ax += getAcc();
        }
        if (ax == 0 && Math.abs(getSpeedX()) <= getMaxSpeed() + 1) {
            if (getSpeedX() > 0) {
                setSpeedX(Math.max(getSpeedX() - getDeacc(), 0));
            } else if (getSpeedX() < 0) {
                setSpeedX(Math.min(getSpeedX() + getDeacc(), 0));
            }
        }
        if (ay == 0 && Math.abs(getSpeedY()) <= getMaxSpeed() + 1) {
            if (getSpeedY() > 0) {
                setSpeedY(Math.max(getSpeedY() - getDeacc(), 0));
            } else if (getSpeedY() < 0) {
                setSpeedY(Math.min(getSpeedY() + getDeacc(), 0));
            }
        }
        if (Math.abs(getSpeedX()) > getMaxSpeed()) {
            if (getSpeedX() > 0) {
                setSpeedX(Math.max(getSpeedX() - 1, 0));
            } else if (getSpeedX() < 0) {
                setSpeedX(Math.min(getSpeedX() + 1, 0));
            }
        }
        if (Math.abs(getSpeedY()) > getMaxSpeed()) {
            if (getSpeedY() > 0) {
                setSpeedY(Math.max(getSpeedY() - 1, 0));
            } else if (getSpeedY() < 0) {
                setSpeedY(Math.min(getSpeedY() + 1, 0));
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

    public Ladder getLadder() {
        for (RoomObject interactable : interactables) {
            interactable.interaction(Player.this);
            if (interactable instanceof Ladder) {
                return (Ladder) interactable;
            }
        }
        return null;
    }

    public int getDepthMovement() {
        return -getLadderDirection();
    }

    public int getLadderDirection() {
        if (getLadder() == null) {
            return 0;
        }
        return getLadder().getDirection();
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

    private final Action accelerateUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingUp = true;
        }
    };

    private final Action decelerateUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingUp = false;
        }
    };

    private final Action accelerateLeft = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingLeft = true;
        }
    };

    private final Action decelerateLeft = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingLeft = false;
        }
    };

    private final Action accelerateDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingDown = true;
        }
    };

    private final Action decelerateDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingDown = false;
        }
    };

    private final Action accelerateRight = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingRight = true;
        }
    };

    private final Action decelerateRight = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movingRight = false;
        }
    };

    private final Action dash = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (dashCooldown < 25) {
                return;
            }   
            if (movingUp && movingDown || movingLeft && movingRight) {
                return;
            }
            dashCooldown = 0;
            setMaxSpeed(3 * getMaxSpeed());
            Game.setFreezeFrame(2);
            Room.setScreenShakeDuration(2);
            Room.setScreenShakeStrength(2);
        }
    };

}