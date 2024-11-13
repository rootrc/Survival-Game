package core.dungeon.dungeon_ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import core.Game;
import core.dungeon.room.Room;
import core.game_components.GameComponent;
import core.utilities.Easing;

public class MiniMap extends GameComponent {
    private Node currentNode;
    private Node nodes[] = new Node[100];

    private InternalMiniMapDisplay internalMiniMapDisplay;

    public MiniMap() {
        super(216, 216);
        setLocation(Game.SCREEN_WIDTH - getWidth() - 16, 16);
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
        currentNode.colorCnt = 70 - currentNode.colorCnt;
        currentNode = nodes[nextRoom.getId()];
        currentNode.isCurrentNode = true;
        currentNode.colorCnt = 70 - currentNode.colorCnt;
        internalMiniMapDisplay.updateCurrentNode();
    }

    private void addStartingNodeToDisplay(Node startingNode) {
        internalMiniMapDisplay.add(startingNode);
        startingNode.setLocation((internalMiniMapDisplay.getWidth() - getWidth()) / 2,
                (internalMiniMapDisplay.getHeight() - getHeight()) / 2);
        startingNode.getEasing().set(null, startingNode.getLocation());
    }
    
    public void updateNodeConnections(Room nextRoom, int connectingLadderXPos) {
        currentNode.connectToNode(nodes[nextRoom.getId()], connectingLadderXPos);
        addNodeToDisplay(nodes[nextRoom.getId()]);
    }

    private void addNodeToDisplay(Node nextNode) {
        internalMiniMapDisplay.add(nextNode, -1);
        nextNode.setLocation(currentNode.getLocation());
        nextNode.getEasing().set(null, currentNode.getLocation());

        if (currentNode.downConnections.size() == 1) {
            nextNode.moveAndPropagate(0, Node.MIN_DISTANCE);
        } else if (currentNode.downConnections.size() == 2) {
            Node a = currentNode.downConnections.get(0);
            Node b = nextNode;
            if (a.connectingLadderXPos < b.connectingLadderXPos) {
                a.moveAndPropagate(-Node.SIZE);
                b.moveAndPropagate(Node.SIZE, Node.MIN_DISTANCE);
            } else if (a.connectingLadderXPos > b.connectingLadderXPos) {
                a.moveAndPropagate(Node.SIZE);
                b.moveAndPropagate(-Node.SIZE, Node.MIN_DISTANCE);
            }
        } else if (currentNode.downConnections.size() == 3) {
            Node a = currentNode.downConnections.get(0);
            Node b = currentNode.downConnections.get(1);
            Node c = nextNode;
            int distance = Math.max(a.getX(), b.getX()) - c.getX();
            if (a.connectingLadderXPos < c.connectingLadderXPos
                    && b.connectingLadderXPos < c.connectingLadderXPos) {
                a.moveAndPropagate(-distance);
                b.moveAndPropagate(-distance);
                c.moveAndPropagate(2 * Node.SIZE, Node.MIN_DISTANCE);
            } else if (a.connectingLadderXPos > c.connectingLadderXPos
                    && b.connectingLadderXPos > c.connectingLadderXPos) {
                a.moveAndPropagate(distance);
                b.moveAndPropagate(distance);
                c.moveAndPropagate(-2 * Node.SIZE, Node.MIN_DISTANCE);
            } else if (a.connectingLadderXPos < c.connectingLadderXPos
                    && b.connectingLadderXPos > c.connectingLadderXPos) {
                if (distance < 2 * Node.SIZE) {
                    a.moveAndPropagate(-(2 * Node.SIZE - distance));
                    b.moveAndPropagate(2 * Node.SIZE - distance);
                }
            } else if (a.connectingLadderXPos > c.connectingLadderXPos
                    && b.connectingLadderXPos < c.connectingLadderXPos) {
                if (distance < 2 * Node.SIZE) {
                    a.moveAndPropagate(2 * Node.SIZE - distance);
                    b.moveAndPropagate(-(2 * Node.SIZE - distance));
                }
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
                if (!(Math.abs(v.getEasing().getP1().getX() - u.getEasing().getP1().getX()) < Node.MIN_DISTANCE
                        && v.getEasing().getP1().getY() == u.getEasing().getP1().getY())) {
                    continue;
                }
                int moveDistance = (int) (Node.MIN_DISTANCE - Math.abs(v.getEasing().getP1().getX() - u.getEasing().getP1().getX())) / 2;
                if (LCA.downConnections.size() == 2) {
                    Node a = LCA.downConnections.get(0);
                    Node b = LCA.downConnections.get(1);
                    if (a.getEasing().getP1().getX() < b.getEasing().getP1().getX()) {
                        a.moveAndPropagate(-moveDistance);
                        b.moveAndPropagate(moveDistance);
                    } else if (a.getEasing().getP1().getX() > b.getEasing().getP1().getX()) {
                        a.moveAndPropagate(moveDistance);
                        b.moveAndPropagate(-moveDistance);
                    }
                }
                if (LCA.downConnections.size() == 3) {
                    Node a = getLCAa(v, u);
                    Node b = getLCAb(v, u);
                    Node c = LCA.downConnections.get(0);
                    if (LCA.downConnections.get(1) != a && LCA.downConnections.get(1) != b) {
                        c = LCA.downConnections.get(1);
                    } else if (LCA.downConnections.get(2) != a && LCA.downConnections.get(2) != b) {
                        c = LCA.downConnections.get(2);
                    }
                    if (a.getEasing().getP1().getX() < b.getEasing().getP1().getX()
                            && b.getEasing().getP1().getX() < c.getEasing().getP1().getX()) {
                        a.moveAndPropagate(-2 * moveDistance);
                    } else if (a.getEasing().getP1().getX() > b.getEasing().getP1().getX()
                            && b.getEasing().getP1().getX() > c.getEasing().getP1().getX()) {
                        a.moveAndPropagate(2 * moveDistance);
                    } else if (a.getEasing().getP1().getX() < c.getEasing().getP1().getX()
                            && b.getEasing().getP1().getX() > c.getEasing().getP1().getX()) {
                        a.moveAndPropagate(-moveDistance);
                        b.moveAndPropagate(moveDistance);
                    } else if (a.getEasing().getP1().getX() < c.getEasing().getP1().getX()
                            && b.getEasing().getP1().getX() < c.getEasing().getP1().getX()) {
                        a.moveAndPropagate(moveDistance);
                        b.moveAndPropagate(-moveDistance);
                    }
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

    private Node getLCAa(Node a0, Node b0) {
        Node a = a0;
        Node b = b0;
        while (a.depth > b.depth) {
            a = a.ansector;
        }
        while (a.depth < b.depth) {
            b = b.ansector;
        }
        while (a.ansector != b.ansector) {
            a = a.ansector;
            b = b.ansector;
        }
        return a;
    }

    private Node getLCAb(Node a0, Node b0) {
        Node a = a0;
        Node b = b0;
        while (a.depth > b.depth) {
            a = a.ansector;
        }
        while (a.depth < b.depth) {
            b = b.ansector;
        }
        while (a.ansector != b.ansector) {
            a = a.ansector;
            b = b.ansector;
        }
        return b;
    }

    
    public int getExploredRoomCnt() {
        return internalMiniMapDisplay.getComponentCount();
    }

    public int getMaxDepth() {
        int maxDepth = -1;
        for (int i = 0; i < 100; i++) {
            maxDepth = Math.max(maxDepth, nodes[i].depth);
        }
        return maxDepth;
    }

    private class InternalMiniMapDisplay extends GameComponent {
        public InternalMiniMapDisplay() {
            super(1000, 1000);
            setLocation(MiniMap.this.getWidth() + (-getWidth() - Node.SIZE) / 2,
                    MiniMap.this.getHeight() + (-getHeight() - Node.SIZE) / 2);
            easing = new Easing(70);
            easing.set(null, getLocation());
        }

        private Easing easing;
        private int cnt;

        @Override
        public void update() {
            if (easing.getP1().equals(getLocation())) {
                return;
            }
            cnt++;
            setLocation(easing.easeInOutQuad(cnt));
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
            cnt = 0;
            easing.set(getLocation(), new Point((int) (MiniMap.this.getWidth() - Node.SIZE) / 2 - currentNode.getEasing().getP1().x,
                    (int) (MiniMap.this.getHeight() - Node.SIZE) / 2 - currentNode.getEasing().getP1().y));
        }
    }

    private static class Node extends GameComponent {
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
        private int colorCnt;

        private Easing easing;
        private int movementCnt;

        private Node() {
            super(SIZE, SIZE);
            downConnections = new ArrayList<>();
            color = COLOR0;
            colorCnt = 70;
            easing = new Easing(70);
        }

        @Override
        public void update() {
            if (isCurrentNode && !color.equals(COLOR1)) {
                colorCnt++;
                color = new Color(COLOR0.getRed()
                        + (int) ((COLOR1.getRed() - COLOR0.getRed()) * Easing.easeInOutQuad(colorCnt / 70.0)),
                        COLOR0.getGreen() + (int) ((COLOR1.getGreen() - COLOR0.getGreen())
                                * Easing.easeInOutQuad(colorCnt / 70.0)),
                        COLOR0.getBlue() + (int) ((COLOR1.getBlue() - COLOR0.getGreen())
                                * Easing.easeInOutQuad(colorCnt / 70.0)));
            } else if (!isCurrentNode && !color.equals(COLOR0)) {
                colorCnt++;
                color = new Color(COLOR1.getRed()
                        + (int) ((COLOR0.getRed() - COLOR1.getRed()) * Easing.easeInOutQuad(colorCnt / 70.0)),
                        COLOR1.getGreen() + (int) ((COLOR0.getGreen() - COLOR1.getGreen())
                                * Easing.easeInOutQuad(colorCnt / 70.0)),
                        COLOR1.getBlue() + (int) ((COLOR0.getBlue() - COLOR1.getBlue())
                                * Easing.easeInOutQuad(colorCnt / 70.0)));
            }
            if (!easing.getP1().equals(getLocation())) {
                movementCnt++;
                setLocation(easing.easeInOutQuad(movementCnt));
            } else {
                movementCnt = 0;
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

        private void moveAndPropagate(int dx, int dy) {
            easing.set(getLocation(), new Point((int) easing.getP1().getX() + dx, (int) easing.getP1().getY() + dy));
            for (Node node : downConnections) {
                node.moveAndPropagate(dx);
            }
        }

        private void moveAndPropagate(int dx) {
            easing.set(getLocation(), new Point((int) easing.getP1().getX() + dx, (int) easing.getP1().getY()));
            for (Node node : downConnections) {
                node.moveAndPropagate(dx);
            }
        }

        public Easing getEasing() {
            return easing;
        }
    }
}