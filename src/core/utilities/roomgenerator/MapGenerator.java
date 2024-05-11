package core.utilities.roomgenerator;

public class MapGenerator {
    // TEMP
    public static int[][] getRandomMap(int N, int M) {
        SimplexNoise simplexNoise = new SimplexNoise(257, 0.3, (int) (50000 * Math.random()));

        double xStart = 0;
        double XEnd = 500;
        double yStart = 0;
        double yEnd = 500;

        int[][] res = new int[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (i == 0 || i == N - 1 || j == 0 || j == M - 1) {
                    res[i][j] = 0;
                    if (j == M - 1) {
                        System.out.print("0");
                    } else {
                        System.out.print("0 ");
                    }
                    continue;
                }
                if (i == 1 || i == N - 2 || i == 2 || i == N - 3 || j == 1 || j == M - 2 || j == 2 || j == M - 3) {
                    res[i][j] = 3;
                    System.out.print("3 ");
                    continue;
                }
                if (i == 3 || i == N - 4 || j == 3 || j == M - 4) {
                    res[i][j] = 1;
                    System.out.print("1 ");
                    continue;
                }
                int x = (int) (xStart + i * ((XEnd - xStart) / N));
                int y = (int) (yStart + j * ((yEnd - yStart) / M));
                double temp = 0.5 * (1 + simplexNoise.getNoise(x, y));
                // if (temp > 0.7) {
                //     res[i][j] = 0;
                //     System.out.print("0");
                // } else if (temp > 0.65) {
                //     res[i][j] = 3;
                //     System.out.print("3");
                // } else if (temp > 0.5) {
                //     res[i][j] = 1;
                //     System.out.print("1");
                // } else {
                //     res[i][j] = 5;
                //     System.out.print("5");
                // }
                // if (j != M - 1) {
                //     System.out.print(" ");
                // }

                temp -= 0.5;
                if (temp > 0) {
                    temp *= -1;
                }
                temp = -temp;
                temp += 0.5;

                if (temp < 0.55) {
                    res[i][j] = 5;
                    System.out.print("5");
                } else if (temp < 0.63) {
                    res[i][j] = 1;
                    System.out.print("1");
                } else if (temp < 0.7) {
                    res[i][j] = 3;
                    System.out.print("3");
                } else {
                    res[i][j] = 0;
                    System.out.print("0");
                }
                if (j != M - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        return res;
    }
}
