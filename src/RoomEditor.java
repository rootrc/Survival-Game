import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class RoomEditor {

    private static BufferedReader br;
    private static StringTokenizer st;
    private static BufferedReader br1;
    private static StringTokenizer st1;

    public static void main(String[] args) throws IOException {

        br = new BufferedReader(new FileReader("res/dungeongeneration/ladders.txt"));
        st = new StringTokenizer("");
        st1 = new StringTokenizer("");

        int roomCnt = nextInt();

        for (int i = 0; i < roomCnt; i++) {
            int id = nextInt();
            nextInt();
            nextInt();
            br1 = new BufferedReader(
                    new FileReader("res/dungeongeneration/tileGrids/map" + String.format("%02d", id) + ".txt"));

            PrintWriter outfile = new PrintWriter(
                    new FileWriter("res/dungeongeneration/tileGridsCopy/map" + String.format("%02d", id) + ".txt"));
            int N = nextInt1();
            int M = nextInt1();
            outfile.println(N + " " + M);
            int[][] arr = new int[N][M];
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    arr[r][c] = nextInt1();
                    if (c != M - 1) {
                        outfile.print(arr[r][c] + " ");
                    } else {
                        outfile.print(arr[r][c]);
                    }
                }
                outfile.println();
            }
            int objectDataSetCnt = nextInt1();
            outfile.println(objectDataSetCnt);
            for (int o = 0; o < objectDataSetCnt; o++) {
                for (int j = 0; j < 2; j++) {
                    int K = nextInt1();
                    outfile.println(K);
                    for (int k = 0; k < K; k++) {
                        outfile.println(nextInt1() + " " + nextInt1() + " " + nextInt1());
                    }
                }
                outfile.println(0);
            }
            outfile.close();
        }
    }

    private static int nextInt() {
        return Integer.parseInt(read());
    }

    private static String read() {
        if (st.hasMoreTokens()) {
            return st.nextToken();
        }
        try {
            st = new StringTokenizer(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return st.nextToken();
    }

    private static int nextInt1() {
        return Integer.parseInt(read1());
    }

    private static String read1() {
        if (st1.hasMoreTokens()) {
            return st1.nextToken();
        }
        try {
            st1 = new StringTokenizer(br1.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return st1.nextToken();
    }
}
