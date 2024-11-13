package core.utilities;

import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Opens files
public abstract class FileOpener {
    private String fileName;
    private BufferedReader br;
    private StringTokenizer st;

    public FileOpener(String fileName) {
        openFile(fileName);
        st = new StringTokenizer("");
    }

    protected final void openFile(String fileName) {
        this.fileName = fileName;
        try {
            br = new BufferedReader(
                    new FileReader(new StringBuilder().append("res/").append(fileName).append(".txt").toString()));
        } catch (FileNotFoundException e) {
            System.err.println(new StringBuilder("File \"").append(fileName).append("\" not found").toString());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    protected final int nextInt() {
        return Integer.parseInt(read());
    }

    protected final String next() {
        return read().replace('_', ' ');
    }

    protected final void closeFile() {
        try {
            br.close();
        } catch (IOException e) {
            System.err.println(new StringBuilder("File ").append(fileName).append(" could not close").toString());
            System.exit(-1);
        }
    }

    private String read() {
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

    public String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.err.println(new StringBuilder("IOException in").append(fileName).toString());
            System.exit(-1);
        }
        return "";
    }
}