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

    private int health;
    private int hitDirection;
    private int hitCnt;

    private SpriteSheet lastSpriteSheet;

    private final Action interact = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (interactables == null || interactionCooldown < 30) {
                return;
            }
            interactionCooldown = 0;
            for (int i = 0; i < interactables.size(); i++) {
                if (interactables.size() == 0) {
                    break;
                }
                RoomObject interactable = interactables.get(i);
                interactable.interaction(Player.this);
                if (interactable instanceof Ladder) {
                    nextRoom.actionPerformed(e);
                }
            }
        }
    };

    public Player(Action nextRoom, Inventory inventory) {
        super(new SpriteSheet(ImageUtilities.getImage("entities", "player"), 4, 8, 8),
                CollisionBox.getCollisionBox(0.5, 1.75, 1, 1),
                CollisionBox.getCollisionBox(0, 1.25, 2, 2),
                3 * Dungeon.TILESIZE / 16, 3 * Dungeon.TILESIZE / 160.0, 3 * Dungeon.TILESIZE / 80.0);
        this.nextRoom = nextRoom;
        this.inventory = inventory;
        lightAmount = DiffSettings.playerLightStartAmount;
        lightRadiusFactor = DiffSettings.playerLightRadiusFactor;
        lightDecreaseFactor = DiffSettings.playerLightDecreaseFactor;
        lightDetectionRadiusSquared = DiffSettings.playerLightDetectionRadiusSquared;
        health = 5;
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
            if (hitCnt <= 60) {
                getSpriteSheet().next();
            }
        } else {
            getSpriteSheet().setFrame(getSpriteSheet().getFrame() / 2);
        }

        if (dashCooldown < 10) {
            setSpeedX(a * getMaxSpeed() * DirectionUtilities.getXMovement(Player.this.getDirection()));
            setSpeedY(a * getMaxSpeed() * DirectionUtilities.getYMovement(Player.this.getDirection()));
        } else {
            setDirection(DirectionUtilities.getMovingDirection(movingUp, movingLeft, movingDown, movingRight));
        }
        if (dashCooldown == 10) {
            setMaxSpeed(getMaxSpeed() / 3);
        }

        if (hitCnt > 0) {
            if (hitCnt == 60) {
                replaceSpriteSheet(lastSpriteSheet);
            }
            hitCnt--;
            if (hitCnt > 55) {
                moveX(3 * getMaxSpeed() * DirectionUtilities.getXMovement(hitDirection));
                moveY(3 * getMaxSpeed() * DirectionUtilities.getYMovement(hitDirection));
            } else {
                if (hitCnt % 10 == 5) {
                    replaceSpriteSheet(ImageUtilities.getImage("entities", "playerWhiteFill"));
                } else if (hitCnt % 10 == 0) {
                    replaceSpriteSheet(lastSpriteSheet);
                }
            }
        }
        super.update();
    }

    private static final double a = Math.sqrt(2) / 2;

    @Override
    public void move() {
        if (dashCooldown < 4 | hitCnt >= 55) {
            return;
        }
        double ax = 0;
        double ay = 0;
        if (movingUp && Math.abs(getSpeedY()) <= getMaxSpeed()) {
            ay -= getAccSpeed();
        }
        if (movingLeft && Math.abs(getSpeedX()) <= getMaxSpeed()) {
            ax -= getAccSpeed();
        }
        if (movingDown && Math.abs(getSpeedY()) <= getMaxSpeed()) {
            ay += getAccSpeed();
        }
        if (movingRight && Math.abs(getSpeedX()) <= getMaxSpeed()) {
            ax += getAccSpeed();
        }
        if (ax == 0) {
            setSpeedX(Math.min(getSpeedX() - (int) Math.signum(getSpeedX()) * getDeaccSpeed(), 0));
        }
        if (ay == 0) {
            setSpeedY(Math.min(getSpeedY() - (int) Math.signum(getSpeedY()) * getDeaccSpeed(), 0));
        }
        if (ax != 0 && ay == 0) {
            setSpeedX(Math.max(Math.min(getSpeedX() + ax, getMaxSpeed()), -getMaxSpeed()));
        } else if (ax == 0 && ay != 0) {
            setSpeedY(Math.max(Math.min(getSpeedY() + ay, getMaxSpeed()), -getMaxSpeed()));
        } else if (ax != 0 && ay != 0) {
            setSpeedX(Math.max(Math.min(getSpeedX() + a * ax, a * getMaxSpeed()), -a * getMaxSpeed()));
            setSpeedY(Math.max(Math.min(getSpeedY() + a * ay, a * getMaxSpeed()), -a * getMaxSpeed()));
        }
    }

    public void collides(Player player) {
    }

    public void interaction(Player player) {
    }

    public void clearInteractable() {
        interactables.clear();
    }

    public void addInteractable(RoomObject object) {
        interactables.add(object);
    }

    public void takeDamage() {
        if (hitCnt > 0) {
            return;
        }
        health--;
        if (health == 1) {
            replaceSpriteSheet(ImageUtilities.getImage("entities", "playerRedOutline"));
        }
        setSpeedX(0);
        setSpeedY(0);
        Game.setFreezeFrame(6);
        Room.setScreenShakeDuration(20);
        Room.setScreenShakeStrength(20);
        if (dashCooldown < 10) {
            setMaxSpeed((int) (getMaxSpeed() / 3));
        }
        dashCooldown = 1000;
        hitCnt = 60;
        hitDirection = DirectionUtilities.reverseDirection(getDirection());
        lastSpriteSheet = getSpriteSheet();
        replaceSpriteSheet(ImageUtilities.getImage("entities", "playerRedFill"));
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
            setMaxSpeed((int) (3 * getMaxSpeed()));
            Game.setFreezeFrame(3);
            Room.setScreenShakeDuration(2);
            Room.setScreenShakeStrength(2);
        }
    };

}