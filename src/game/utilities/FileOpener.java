package game.utilities;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class FileOpener {
    private String fileName;
    private BufferedReader br;

    public FileOpener(String fileName) {
        openFile(fileName);
    }

    protected void openFile(String fileName) {
        this.fileName = fileName;
        try {
            br = new BufferedReader(
                    new FileReader(new StringBuilder().append("res/").append(fileName).append(".txt").toString()));
            // sc = new Scanner(new FileReader(new
            // StringBuilder().append("res/").append(str).append(".txt").toString()));
        } catch (FileNotFoundException e) {
            System.err.println(new StringBuilder("File \"").append(fileName).append("\" not found").toString());
            System.exit(-1);
        }
    }

    protected int nextInt() {
        return Integer.parseInt(readLine());
    }

    protected String nextStr() {
        return readLine().replace('_', ' ');
    }

    protected void closeFile() {
        try {
            br.close();
        } catch (IOException e) {
            System.err.println(new StringBuilder("File ").append(fileName).append(" could not close").toString());
            System.exit(-1);
        }
    }

    private StringTokenizer st = new StringTokenizer("");

    private String readLine() {
        if (st.hasMoreTokens()) {
            return st.nextToken();
        }
        try {
            st = new StringTokenizer(br.readLine());
        } catch (IOException e) {
            System.err.println(new StringBuilder("IOException in").append(fileName).toString());
            System.exit(-1);
        }
        return st.nextToken();
    }
}
