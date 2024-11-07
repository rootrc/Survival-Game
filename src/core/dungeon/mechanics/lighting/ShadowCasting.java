package core.dungeon.mechanics.lighting;

import java.util.HashMap;
import java.util.Stack;

import core.dungeon.mechanics.HeightHandler;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.tile.Tile;
import core.dungeon.room.tile.TileGrid;

public class ShadowCasting {
    private final HashMap<Integer, boolean[][]> memo = new HashMap<>();
    private int N, M;
    private Tile[][] arr;
    private Player player;
    private HeightHandler heightHandler;

    public ShadowCasting(Player player, TileGrid tileGrid) {
        this.player = player;
        heightHandler = tileGrid.getHeightHandler();
        arr = tileGrid.getCollisionChecker().getCollisionArray();
        N = tileGrid.getN();
        M = tileGrid.getM();
    }

    public boolean isVisible(RoomObject roomObject) {
        return isVisible(roomObject.getMinRow(), roomObject.getMinCol())
                || isVisible(roomObject.getMaxRow(), roomObject.getMinCol())
                || isVisible(roomObject.getMinRow(), roomObject.getMaxCol())
                || isVisible(roomObject.getMaxRow(), roomObject.getMaxCol());
    }

    public boolean isVisible(int r, int c) {
        if (r < 0 || c < 0 || r >= N || c >= M) {
            return false;
        }
        return getBooleanArray()[r][c];
    }

    public boolean[][] getBooleanArray() {
        int hash = 10000 * getOrigin().r + getOrigin().c;
        if (memo.containsKey(hash)) {
            return memo.get(hash);
        }
        boolean[][] res = new boolean[N][M];
        compute_fov(res);
        memo.put(hash, res);
        return res;
    }

    public void compute_fov(boolean[][] res) {
        markVisible(res, getOrigin());
        for (int i = 0; i < 4; i++) {
            Quadrant quadrant = new Quadrant(i, getOrigin());
            Stack<Row> stack = new Stack<>();
            stack.add(new Row(1, -1, 1));
            while (!stack.isEmpty()) {
                Row row = stack.pop();
                int min_col = Math.max((int) Math.floor(row.depth * row.startSlope + 0.5),
                        -(int) Math.sqrt(player.getStats().getDetectionRadiusSquared() - row.depth * row.depth) + 1);
                int max_col = Math.min((int) Math.ceil(row.depth * row.endSlope - 0.5),
                        (int) Math.sqrt(player.getStats().getDetectionRadiusSquared() - row.depth * row.depth) - 1);
                Point prevPoint = null;
                for (int col = min_col; col <= max_col; col++) {
                    Point point = quadrant.transform(new Point(row.depth, col));
                    if (isBlocking(point) || (col >= row.depth * row.startSlope && col <= row.depth * row.endSlope)) {
                        markVisible(res, point);
                    }
                    if (prevPoint == null) {
                        prevPoint = point;
                        continue;
                    }
                    if (isBlocking(prevPoint) && !isBlocking(point)) {
                        row.startSlope = ((double) (2 * col - 1)) / (2 * row.depth);
                    }
                    if (!isBlocking(prevPoint) && isBlocking(point)) {
                        stack.add(new Row(row.depth + 1, row.startSlope, ((double) (2 * col - 1)) / (2 * row.depth)));
                    }
                    prevPoint = point;
                }
                if (prevPoint == null) {
                    continue;
                }
                if (!isBlocking(prevPoint)) {
                    stack.add(new Row(row.depth + 1, row.startSlope, row.endSlope));
                }
            }
        }
    }

    public Point getOrigin() {
        return new Point(player.getMinRow(), player.getMinCol());
    }

    public void markVisible(boolean[][] res, Point point) {
        if (point.r < 0 || point.c < 0 || point.r >= N || point.c >= M) {
            return;
        }
        res[point.r][point.c] = true;
    }

    public boolean isBlocking(Point point) {
        if (point.r < 0 || point.c < 0 || point.r >= N || point.c >= M) {
            return true;
        }
        if (arr[point.r][point.c] == null) {
            return false;
        }
        if (heightHandler.getLayer(player) == HeightHandler.TOP) {
            return arr[point.r][point.c].getHeight() == Tile.CEILING;
        } else {
            return arr[point.r][point.c].getHeight() > Tile.FLOOR;
        }
    }

    private static class Quadrant {
        private static final int NORTH = 0;
        private static final int EAST = 1;
        private static final int SOUTH = 2;
        private static final int WEST = 3;
        private int cardinal;
        private Point origin;

        private Quadrant(int cardinal, Point origin) {
            this.cardinal = cardinal;
            this.origin = origin;
        }

        private Point transform(Point point) {
            switch (cardinal) {
                case NORTH:
                    return new Point(origin.r - point.r, origin.c + point.c);
                case EAST:
                    return new Point(origin.r + point.r, origin.c + point.c);
                case SOUTH:
                    return new Point(origin.r + point.c, origin.c + point.r);
                case WEST:
                    return new Point(origin.r + point.c, origin.c - point.r);
            }
            return null;
        }
    }

    private static class Row {
        private int depth;
        private double startSlope;
        private double endSlope;

        private Row(int depth, double startSlope, double endSlope) {
            this.depth = depth;
            this.startSlope = startSlope;
            this.endSlope = endSlope;
        }
    }

    private static class Point {
        private int r;
        private int c;

        private Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}