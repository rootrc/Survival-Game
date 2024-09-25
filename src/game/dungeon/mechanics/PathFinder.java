package game.dungeon.mechanics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import game.dungeon.room.tile.Tile;

public class PathFinder {
    private static final int direct[][] = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    private int N, M;
    private HashMap<Integer, Integer> map = new HashMap<>();
    private boolean[][] arr;
    private int[][] floodFill;

    public PathFinder(Tile[][] collisionArray) {
        N = collisionArray.length;
        M = collisionArray[0].length;
        arr = new boolean[collisionArray.length][collisionArray[0].length];
        for (int r = 0; r < collisionArray.length; r++) {
            for (int c = 0; c < collisionArray[r].length; c++) {
                if (collisionArray[r][c].getHitbox() == null || collisionArray[r][c].getHitbox().getWidth() < 4
                        || collisionArray[r][c].getHitbox().getHeight() < 4) {
                    arr[r][c] = true;
                }
            }
        }
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
                if (arr[nextP.r][nextP.c] && dist[nextP.r][nextP.c] == 0) {
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
                if (floodFill[r0][c0] != 0 || !arr[r0][c0]) {
                    continue;
                }
                floodFill[r0][c0] = cnt++;
                Queue<Point> queue = new LinkedList<>();
                queue.add(new Point(r0, c0));
                while (!queue.isEmpty()) {
                    Point p = queue.poll();
                    for (int[] d : direct) {
                        Point nextP = new Point(p.r + d[0], p.c + d[1]);
                        if (arr[nextP.r][nextP.c] && floodFill[nextP.r][nextP.c] == 0) {
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

    private static class Point {
        private int r;
        private int c;

        Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}
