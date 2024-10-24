package core.dungeon.room.entity;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.inventory.Inventory;
import core.dungeon.room.Room;
import core.dungeon.room.object.Ladder;
import core.dungeon.room.object_utilities.CollisionBox;
import core.dungeon.room.object_utilities.DirectionUtilities;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.SpriteSheet;
import core.dungeon.room.tile.Tile;
import core.dungeon.settings.DiffSettings;
import core.dungeon.settings.KeyBinds;
import core.utilities.ImageUtilities;

public class Player extends Entity {
    private static final int highMaxSpeed = 3 * Tile.SIZE / 16;
    private static final int lowMaxSpeed = Tile.SIZE / 8;

    private boolean movingUp;
    private boolean movingLeft;
    private boolean movingDown;
    private boolean movingRight;
    private Inventory inventory;
    private ArrayList<RoomObject> interactables = new ArrayList<>();
    private Action nextRoom;
    private Action death;

    private double lightAmount;
    private int lightRadiusFactor;
    private int lightDecreaseFactor;
    private int lightDetectionRadiusSquared;

    private int interactionCooldown;
    private int dashCooldown;

    private double health;
    private int healthPoints;

    private int hitDirection;
    private int hitCnt;

    private SpriteSheet lastSpriteSheet;

    public Player(Action nextRoom, Action death, Inventory inventory) {
        super(new SpriteSheet(ImageUtilities.getImage("entities", "player"), 4, 8, 8),
                CollisionBox.getCollisionBox(0.5, 1.75, 1, 1),
                CollisionBox.getCollisionBox(0, 1.25, 2, 2),
                highMaxSpeed, Tile.SIZE / 40.0, Tile.SIZE / 80.0);
        this.nextRoom = nextRoom;
        this.death = death;
        this.inventory = inventory;
        lightAmount = DiffSettings.playerLightStartAmount;
        lightRadiusFactor = DiffSettings.playerLightRadiusFactor;
        lightDecreaseFactor = DiffSettings.playerLightDecreaseFactor;
        lightDetectionRadiusSquared = DiffSettings.playerLightDetectionRadiusSquared;
        health = DiffSettings.startingHealth;
        healthPoints = DiffSettings.startingHealth;
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
        getInputMap(2).put(KeyBinds.slowDownToggle, "slowDownToggle");
        getActionMap().put("slowDownToggle", slowDownToggle);
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
        if (Math.random() < 0.01) {
            addHealthPoints(-1);
        }
        interactionCooldown++;
        dashCooldown++;
        setLightRadius(lightRadiusFactor * Math.min(Math.pow(lightAmount, 0.2), Math.sqrt(lightAmount) / 6));
        lightAmount -= Math.log(lightAmount) / lightDecreaseFactor;
        if (isMoving()) {
            if (hitCnt <= Game.UPS) {
                getSpriteSheet().next();
            }
        } else {
            getSpriteSheet().setFrame(getSpriteSheet().getFrame() / 2);
        }

        if (dashCooldown < 10) {
            setSpeedX(A * getMaxSpeed() * DirectionUtilities.getXMovement(Player.this.getDirection()));
            setSpeedY(A * getMaxSpeed() * DirectionUtilities.getYMovement(Player.this.getDirection()));
        } else {
            setDirection(DirectionUtilities.getMovingDirection(movingUp, movingLeft, movingDown, movingRight));
        }
        if (dashCooldown == 10) {
            setMaxSpeed(getMaxSpeed() / 3);
        }

        if (hitCnt > 0) {
            if (hitCnt == Game.UPS) {
                replaceSpriteSheet(lastSpriteSheet);
            }
            hitCnt--;
            if (hitCnt > 11.0 / 12 * Game.UPS) {
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

    private static final double A = Math.sqrt(2) / 2;

    @Override
    public void move() {
        if (dashCooldown < 4 | hitCnt >= 11.0 / 12 * Game.UPS) {
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
            setSpeedX(Math.max(Math.min(getSpeedX() + A * ax, A * getMaxSpeed()), -A * getMaxSpeed()));
            setSpeedY(Math.max(Math.min(getSpeedY() + A * ay, A * getMaxSpeed()), -A * getMaxSpeed()));
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

    public double getHealth() {
        return health;
    }

    public void addHealth(int delta) {
        health += delta;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void addHealthPoints(int delta) {
        healthPoints += delta;
        health = Math.min(health, healthPoints);
    }

    public void takeDamage() {
        if (hitCnt > 0) {
            return;
        }
        health--;
        if ((int) health == 1) {
            replaceSpriteSheet(ImageUtilities.getImage("entities", "playerRedOutline"));
        }
        setSpeedX(0);
        setSpeedY(0);
        Game.setFreezeFrame(6);
        Room.setScreenShakeDuration(15);
        Room.setScreenShakeStrength(15);
        if (dashCooldown < 1.0 / 6 * Game.UPS) {
            setMaxSpeed((int) (getMaxSpeed() / 3));
        }
        dashCooldown = 1000;
        if (health == 0) {
            replaceSpriteSheet(ImageUtilities.getImage("entities", "playerRedFill"));
            death.actionPerformed(null);
            return;
        }
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

    private final Action slowDownToggle = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (getMaxSpeed() == highMaxSpeed) {
                setMaxSpeed(lowMaxSpeed);
            } else if (getMaxSpeed() == lowMaxSpeed) {
                setMaxSpeed(highMaxSpeed);
            }
        }
    };

    private final Action dash = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (dashCooldown < 5.0 / 12 * Game.UPS) {
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

}