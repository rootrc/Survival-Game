package game.dungeon.dungeon_ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.Game;
import game.dungeon.room.Room;
import game.game_components.GameComponent;
import game.utilities.AnimationUtilities;

public class MiniMap extends GameComponent {
    private Node currentNode;
    private Node nodes[] = new Node[100];

    private InternalMiniMapDisplay internalMiniMapDisplay;

    public MiniMap() {
        super(216, 216);
        setLocation(Game.screenWidth - getWidth() - 16, 16);
        internalMiniMapDisplay = new InternalMiniMapDisplay();
        add(internalMiniMapDisplay);
        for (int i = 0; i < 100; i++) {
            nodes[i] = new Node();
        }
    }

    public void setStartingRoom(Room startingRoom) {
        addStartingNodeToDisplay(nodes[startingRoom.getId()]);
        currentNode = nodes[startingRoom.getId()];
        currentNode.isCurrentNode = true;
        currentNode.color = Node.COLOR1;
    }

    @Override
    public void update() {

    }

    @Override
    public void drawComponent(Graphics2D g2d) {

    }

    public void updateRoom(Room nextRoom) {
        currentNode.isCurrentNode = false;
        currentNode.cnt = 60 - currentNode.cnt;
        currentNode = nodes[nextRoom.getId()];
        currentNode.isCurrentNode = true;
        currentNode.cnt = 60 - currentNode.cnt;
        internalMiniMapDisplay.updateCurrentNode();
    }

    private void addStartingNodeToDisplay(Node startingNode) {
        internalMiniMapDisplay.add(startingNode);
        startingNode.setLocation((internalMiniMapDisplay.getWidth() - getWidth()) / 2,
                (internalMiniMapDisplay.getHeight() - getHeight()) / 2);
    }

    public void updateNodeConnections(Room nextRoom, int connectingLadderXPos) {
        currentNode.connectToNode(nodes[nextRoom.getId()], connectingLadderXPos);
        addNodeToDisplay(nodes[nextRoom.getId()]);
    }

    private void addNodeToDisplay(Node nextNode) {
        internalMiniMapDisplay.add(nextNode);
        nextNode.setLocation(currentNode.getLocation());

        if (currentNode.downConnections.size() == 1) {
            nextNode.moveY(Node.MIN_DISTANCE);
        } else if (currentNode.downConnections.size() == 2) {
            Node a = currentNode.downConnections.get(0);
            Node b = currentNode.downConnections.get(1);
            b.moveY(Node.MIN_DISTANCE);
            if (a.connectingLadderXPos < b.connectingLadderXPos) {
                a.moveXAndPropagate(-Node.SIZE);
                b.moveXAndPropagate(Node.SIZE);
            } else {
                a.moveXAndPropagate(Node.SIZE);
                b.moveXAndPropagate(-Node.SIZE);
            }
        }
        resizeGraph();
    }

    private void resizeGraph() {
        boolean flag = false;
        for (int i = 0; i < 100; i++) {
            for (int j = i + 1; j < 100; j++) {
                Node v = nodes[i];
                Node u = nodes[j];
                if (v.getX() == 0 || u.getX() == 0) {
                    continue;
                }
                Node LCA = getLCA(v, u);
                if (LCA.downConnections.size() < 2) {
                    continue;
                }
                if (!(Math.abs(v.getX() - u.getX()) < Node.MIN_DISTANCE && v.getY() == u.getY())) {
                    continue;
                }
                int moveDistance = (Node.MIN_DISTANCE - Math.abs(v.getX() - u.getX())) / 2;
                Node a = LCA.downConnections.get(0);
                Node b = LCA.downConnections.get(1);
                if (a.getX() < b.getX()) {
                    a.moveXAndPropagate(-moveDistance);
                    b.moveXAndPropagate(moveDistance);
                } else {
                    a.moveXAndPropagate(moveDistance);
                    b.moveXAndPropagate(-moveDistance);
                }
                flag = true;
            }
        }
        if (flag) {
            resizeGraph();
        }
    }

    private Node getLCA(Node a0, Node b0) {
        Node a = a0;
        Node b = b0;
        while (a.depth > b.depth) {
            a = a.ansector;
        }
        while (a.depth < b.depth) {
            b = b.ansector;
        }
        while (a != b) {
            a = a.ansector;
            b = b.ansector;
        }
        return a;
    }

    private class InternalMiniMapDisplay extends GameComponent {
        public InternalMiniMapDisplay() {
            super(1000, 1000);
            setLocation(MiniMap.this.getWidth() + (-getWidth() - Node.SIZE) / 2,
                    MiniMap.this.getHeight() + (-getHeight() - Node.SIZE) / 2);
            x1 = getX();
            y1 = getY();
        }

        private int cnt;
        private int x0;
        private int y0;
        private int x1;
        private int y1;

        @Override
        public void update() {
            if (x1 == getX() && y1 == getY()) {
                return;
            }
            cnt++;
            setX(x0 + (x1 - x0) * AnimationUtilities.easeInOutQuad(AnimationUtilities.easeInOutQuad(cnt / 60.0)));
            setY(y0 + (y1 - y0) * AnimationUtilities.easeInOutQuad(AnimationUtilities.easeInOutQuad(cnt / 60.0)));
        }

        @Override
        public void drawComponent(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(155, 103, 60));
            for (Node v : nodes) {
                for (Node u : v.downConnections) {
                    g2d.drawLine(v.getX() + Node.SIZE / 2, v.getY() + Node.SIZE / 2, u.getX() + Node.SIZE / 2,
                            u.getY() + Node.SIZE / 2);
                }
            }
        }

        public void updateCurrentNode() {
            x0 = getX();
            y0 = getY();
            x1 = (MiniMap.this.getWidth() - Node.SIZE) / 2 - currentNode.getX();
            y1 = (MiniMap.this.getHeight() - Node.SIZE) / 2 - currentNode.getY();
            cnt = 0;
            // setLocation((MiniMap.this.getWidth() - Node.SIZE) / 2 - currentNode.getX(),
            // (MiniMap.this.getHeight() - Node.SIZE) / 2 - currentNode.getY());
        }
    }

    private class Node extends GameComponent {
        private static final int SIZE = 16;
        private static final int MIN_DISTANCE = 2 * SIZE;
        private static final Color COLOR0 = Color.lightGray;
        private static final Color COLOR1 = Color.yellow;

        private Node ansector;
        private int connectingLadderXPos;
        private ArrayList<Node> downConnections;
        private boolean isCurrentNode;
        private int depth;

        private Color color;
        private int cnt;

        private Node() {
            super(SIZE, SIZE);
            downConnections = new ArrayList<>();
            color = COLOR0;
            cnt = 60;
        }

        @Override
        public void update() {
            if (isCurrentNode && !color.equals(COLOR1)) {
                cnt++;
                color = new Color(COLOR0.getRed() + (int) ((COLOR1.getRed() - COLOR0.getRed()) * AnimationUtilities.easeInOutQuad(cnt / 60.0)),
                        COLOR0.getGreen() + (int) ((COLOR1.getGreen() - COLOR0.getGreen()) * AnimationUtilities.easeInOutQuad(cnt / 60.0)),
                        COLOR0.getBlue() + (int) ((COLOR1.getBlue() - COLOR0.getGreen()) * AnimationUtilities.easeInOutQuad(cnt / 60.0)));
            } else if (!isCurrentNode && !color.equals(COLOR0)) {
                cnt++;
                color = new Color(COLOR1.getRed() + (int) ((COLOR0.getRed() - COLOR1.getRed()) * AnimationUtilities.easeInOutQuad(cnt / 60.0)),
                        COLOR1.getGreen() + (int) ((COLOR0.getGreen() - COLOR1.getGreen()) * AnimationUtilities.easeInOutQuad(cnt / 60.0)),
                        COLOR1.getBlue() + (int) ((COLOR0.getBlue() - COLOR1.getBlue()) * AnimationUtilities.easeInOutQuad(cnt / 60.0)));
            }
        }

        @Override
        public void drawComponent(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        private void connectToNode(Node node, int connectingLadderXPos) {
            downConnections.add(node);
            node.ansector = this;
            node.connectingLadderXPos = connectingLadderXPos;
            node.depth = depth + 1;
        }

        private void moveXAndPropagate(int delta) {
            super.moveX(delta);
            for (Node node : downConnections) {
                node.moveXAndPropagate(delta);
            }
        }
    }

}