package core.dungeon.mechanics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import core.dungeon.room.tile.Tile;

public class PathFinder {
    private static final int direct[][] = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    private int N, M;
    private Tile[][] collisionArray;
    private int[][] floodFill;
    private HashMap<Integer, Integer> map = new HashMap<>();

    public PathFinder(Tile[][] collisionArray) {
        this.collisionArray = collisionArray;
        N = collisionArray.length;
        M = collisionArray[0].length;
        floodFill();
    }

    public int getDistance(int r0, int c0, int r1, int c1) {
        if (r0 == r1 && c0 == c1) {
            return 0;
        }
        if (map.containsKey(hash(r0, c0, r1, c1))) {
            return map.get(hash(r0, c0, r1, c1));
        }
        if (map.containsKey(hash(r1, c1, r0, c0))) {
            return map.get(hash(r1, c1, r0, c0));
        }
        int[][] dist = new int[N][M];
        dist[r0][c0] = 1;
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(r0, c0));
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p.r == r1 && p.c == c1) {
                map.put(hash(r0, c0, r1, c1), dist[p.r][p.c] - 1);
                return dist[p.r][p.c] - 1;
            }
            for (int[] d : direct) {
                Point nextP = new Point(p.r + d[0], p.c + d[1]);
                if (canTravel(p.r, p.c, nextP.r, nextP.c, d) && dist[nextP.r][nextP.c] == 0) {
                    dist[nextP.r][nextP.c] = dist[p.r][p.c] + 1;
                    queue.add(nextP);
                }
            }
        }
        map.put(hash(r0, c0, r1, c1), -1);
        return -1;
    }

    public boolean isReachable(int r0, int c0, int r1, int c1) {
        return floodFill[r0][c0] != 0 && floodFill[r0][c0] == floodFill[r1][c1];
    }

    private void floodFill() {
        int cnt = 1;
        floodFill = new int[N][M];
        for (int r0 = 1; r0 < N - 1; r0++) {
            for (int c0 = 1; c0 < M - 1; c0++) {
                if (floodFill[r0][c0] != 0 || !canTravel(r0, c0)) {
                    continue;
                }
                floodFill[r0][c0] = cnt++;
                Queue<Point> queue = new LinkedList<>();
                queue.add(new Point(r0, c0));
                while (!queue.isEmpty()) {
                    Point p = queue.poll();
                    for (int[] d : direct) {
                        Point nextP = new Point(p.r + d[0], p.c + d[1]);
                        if (canTravel(p.r, p.c, nextP.r, nextP.c, d) && floodFill[nextP.r][nextP.c] == 0) {
                            floodFill[nextP.r][nextP.c] = floodFill[p.r][p.c];
                            queue.add(nextP);
                        }
                    }
                }
            }
        }
    }

    private int hash(int r0, int c0, int r1, int c1) {
        return 8000000 * r0 + 40000 * c0 + 200 * r1 + c1;
    }

    private boolean canTravel(int r, int c) {
        return collisionArray[r][c].getHitbox() == null;
        // return collisionArray[r][c].getHitbox() == null ||
        // collisionArray[r][c].getHitbox().getWidth() <= Tile.SIZE / 4 ||
        // collisionArray[r][c].getHitbox().getHeight() <= Tile.SIZE / 4;
    }

    private boolean canTravel(int r0, int c0, int r1, int c1, int[] d) {
        if (r1 < 0 || c1 < 0 || r1 >= N || c1 >= M) {
            return false;
        }
        if (d[0] == 1 && d[1] == 0) {
            return (collisionArray[r0][c0].getHitbox() == null
                    || (getDirectionOf(collisionArray[r0][c0]) != 2 && getDirectionOf(collisionArray[r0][c0]) != -1))
                    && (collisionArray[r1][c1].getHitbox() == null
                            || (getDirectionOf(collisionArray[r1][c1]) != 0 && getDirectionOf(collisionArray[r1][c1]) != -1));
        } else if (d[0] == -1 && d[1] == 0) {
            return (collisionArray[r0][c0].getHitbox() == null
                    || (getDirectionOf(collisionArray[r0][c0]) != 0 && getDirectionOf(collisionArray[r0][c0]) != -1))
                    && (collisionArray[r1][c1].getHitbox() == null
                            || (getDirectionOf(collisionArray[r1][c1]) != 2 && getDirectionOf(collisionArray[r1][c1]) != -1));
        } else if (d[0] == 0 && d[1] == 1) {
            return (collisionArray[r0][c0].getHitbox() == null
                    || (getDirectionOf(collisionArray[r0][c0]) != 1 && getDirectionOf(collisionArray[r0][c0]) != -1))
                    && (collisionArray[r1][c1].getHitbox() == null
                            || (getDirectionOf(collisionArray[r1][c1]) != 3 && getDirectionOf(collisionArray[r1][c1]) != -1));
        } else if (d[0] == 0 && d[1] == -1) {
            return (collisionArray[r0][c0].getHitbox() == null
                    || (getDirectionOf(collisionArray[r0][c0]) != 3 && getDirectionOf(collisionArray[r0][c0]) != -1))
                    && (collisionArray[r1][c1].getHitbox() == null
                            || (getDirectionOf(collisionArray[r1][c1]) != 1 && getDirectionOf(collisionArray[r1][c1]) != -1));
        }
        return false;
    }

    private int getDirectionOf(Tile tile) {
        if (tile.getHitbox() == null) {
            return -1;
        }
        if (tile.getHitbox().getHeight() <= Tile.SIZE / 4 && tile.getHitbox().getY() == 0) {
            return 0;
        }
        if (tile.getHitbox().getHeight() <= Tile.SIZE / 4 && tile.getHitbox().getY() != 0) {
            return 2;
        }
        if (tile.getHitbox().getWidth() <= Tile.SIZE / 4 && tile.getHitbox().getX() != 0) {
            return 1;
        }
        if (tile.getHitbox().getWidth() <= Tile.SIZE / 4 && tile.getHitbox().getX() == 0) {
            return 3;
        }
        return -1;
    }

    private static class Point {
        private int r;
        private int c;

        Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}