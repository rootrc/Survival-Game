package game.dungeon.dungeon_ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.Game;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.game_components.GameComponent;

public class MiniMap extends GameComponent {
    private Player player;
    private Node currentNode;
    private Node nodes[] = new Node[101];

    private InternalMiniMapDisplay internalMiniMapDisplay;

    public MiniMap(Player player) {
        super(200, 200);
        this.player = player;
        setLocation(Game.screenWidth - getWidth() - 16, 16);
        internalMiniMapDisplay = new InternalMiniMapDisplay();
        add(internalMiniMapDisplay);
        for (int i = 1; i <= 100; i++) {
            nodes[i] = new Node();
        }
    }

    public void setStartingRoom(Room startingRoom) {
        addStartingNodeToDisplay(nodes[startingRoom.getId()]);
        currentNode = nodes[startingRoom.getId()];
        currentNode.isCurrentNode = true;
        internalMiniMapDisplay.updateCurrentNode();
    }

    @Override
    public void update() {

    }

    @Override
    public void drawComponent(Graphics2D g2d) {

    }

    public void updateRoom(Room nextRoom) {
        currentNode.isCurrentNode = false;
        currentNode = nodes[nextRoom.getId()];
        currentNode.isCurrentNode = true;
        internalMiniMapDisplay.updateCurrentNode();
    }

    public void updateNodeConnections(Room nextRoom, int connectingLadderXPos1, int connectingLadderXPos2) {
        updateNodeConnections(nodes[nextRoom.getId()], connectingLadderXPos1, connectingLadderXPos2);
    }

    private void updateNodeConnections(Node nextNode, int connectingLadderXPos1, int connectingLadderXPos2) {
        currentNode.connectToNode(nextNode, connectingLadderXPos1, connectingLadderXPos2);
        addNodeToDisplay(nextNode);
    }

    private void addStartingNodeToDisplay(Node startingNode) {
        internalMiniMapDisplay.add(startingNode);
        startingNode.setLocationBasedOnCenter(0, 0);
    }

    private void addNodeToDisplay(Node nextNode) {
        if (nextNode.getParent() == this) {
            // TODO
            System.out.println("Issue");
            return;
        }
        internalMiniMapDisplay.add(nextNode);
        nextNode.setLocation(currentNode.getLocation());

        if (currentNode.getConnections().size() == 1) {
            nextNode.moveY(-player.getLadderDirection() * MiniMap.this.getHeight() / 4);
        } else if (currentNode.getConnections().size() == 2) {
            currentNode.getConnections().get(1).connectedNode
                    .moveY(-currentNode.getConnections().get(1).direction * MiniMap.this.getHeight() / 4);
            int direction;
            if (currentNode.getConnections().get(0).connectingLadderXPos < currentNode.getConnections()
                    .get(1).connectingLadderXPos) {
                direction = -1;
            } else {
                direction = 1;
            }
            if (currentNode.getConnections().get(0).connectedNode
                    .getX() == currentNode.getConnections().get(1).connectedNode.getX()) {
                currentNode.getConnections().get(0).connectedNode.adjustX(direction);
            }
            currentNode.getConnections().get(1).connectedNode.adjustX(-direction);
        }

    }

    private class InternalMiniMapDisplay extends GameComponent {
        public InternalMiniMapDisplay() {
            super(1000, 1000);
        }

        @Override
        public void update() {
        }

        @Override
        public void drawComponent(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(155, 103, 60));
            for (int i = 1; i <= 100; i++) {
                for (Edge edge : nodes[i].getConnections(1)) {
                    g2d.drawLine(nodes[i].getX() + nodes[i].getWidth() / 2,
                            nodes[i].getY() + nodes[i].getHeight() / 2,
                            edge.connectedNode.getX() + edge.connectedNode.getWidth() / 2,
                            edge.connectedNode.getY() + edge.connectedNode.getHeight() / 2);
                }
            }
            // for (Edge edge : currentNode.getConnections(1)) {
                // g2d.drawLine(currentNode.getX() + currentNode.getWidth() / 2,
                // currentNode.getY() + currentNode.getHeight() / 2,
                // edge.connectedNode.getX() + edge.connectedNode.getWidth() / 2,
                // edge.connectedNode.getY() + edge.connectedNode.getHeight() / 2);
                // }
                // for (Edge edge : currentNode.getConnections(-1)) {
                    // g2d.drawLine(currentNode.getX() + currentNode.getWidth() / 2,
                    // currentNode.getY() + currentNode.getHeight() / 2,
                    // edge.connectedNode.getX() + edge.connectedNode.getWidth() / 2,
                    // edge.connectedNode.getY() + edge.connectedNode.getHeight() / 2);
                    // }
        }

        public void updateCurrentNode() {
            setLocation((MiniMap.this.getWidth() - currentNode.getWidth()) / 2 - currentNode.getX(),
                    (MiniMap.this.getHeight() - currentNode.getHeight()) / 2 - currentNode.getY());
        }
    }

    private class Node extends GameComponent {
        private ArrayList<Edge> upConnections;
        private ArrayList<Edge> downConnections;
        private boolean isCurrentNode;

        private Node() {
            super(MiniMap.this.getHeight() / 8 - 1, MiniMap.this.getHeight() / 8 - 1);
            upConnections = new ArrayList<>();
            downConnections = new ArrayList<>();
        }

        @Override
        public void update() {
        }

        @Override
        public void drawComponent(Graphics2D g2d) {
            g2d.setColor(Color.lightGray);
            if (isCurrentNode) {
                g2d.setColor(Color.yellow);
            }
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        private void connectToNode(Node node, int connectingLadderXPos1, int connectingLadderXPos2) {
            if (player.getLadderDirection() == Ladder.UP_DIRECTION) {
                upConnections.add(new Edge(node, player.getLadderDirection(), connectingLadderXPos1));
                node.downConnections.add(new Edge(this, -player.getLadderDirection(), connectingLadderXPos2));
            }
            if (player.getLadderDirection() == Ladder.DOWN_DIRECTION) {
                downConnections.add(new Edge(node, player.getLadderDirection(), connectingLadderXPos1));
                node.upConnections.add(new Edge(this, -player.getLadderDirection(), connectingLadderXPos1));
            }
        }

        private ArrayList<Edge> getConnections() {
            return getConnections(player.getLadderDirection());
        }

        private ArrayList<Edge> getConnections(int direction) {
            if (direction == Ladder.UP_DIRECTION) {
                return upConnections;
            }
            if (direction == Ladder.DOWN_DIRECTION) {
                return downConnections;
            }
            return null;
        }

        private void setLocationBasedOnCenter(int x, int y) {
            setLocation((MiniMap.this.internalMiniMapDisplay.getWidth() - getWidth()) / 2 + x,
                    (MiniMap.this.internalMiniMapDisplay.getHeight() - getHeight()) / 2 + y);
        }

        public void adjustX(int delta) {
            super.moveX(delta * MiniMap.this.getHeight() / 8);
            for (Edge edge : downConnections) {
                edge.connectedNode.adjustX(delta);
            }
        }

        // private void getRelativeLocation(int x, int y) {
        // setLocation((MiniMap.this.internalMiniMapDisplay.getWidth() - getWidth()) / 2
        // + x, (MiniMap.this.internalMiniMapDisplay.getHeight()- getHeight() ) / 2 +
        // y);
        // }
    }

    private class Edge {
        private Node connectedNode;
        private int direction;
        private int connectingLadderXPos;

        private Edge(Node node, int direction, int connectingLadderXPos) {
            connectedNode = node;
            this.direction = direction;
            this.connectingLadderXPos = connectingLadderXPos;
        }
    }

}
