package core.utilities;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;

public abstract class FileOpener {
    private Scanner sc;

    public FileOpener(String str) {
        try {
            sc = new Scanner(new FileReader(new StringBuilder().append("res/").append(str).append(".txt").toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void open(String str) {
        closeFile();
        try {
            sc = new Scanner(new FileReader(new StringBuilder().append("res/").append(str).append(".txt").toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected int nextInt() {
        return sc.nextInt();
    }

    protected String nextStr() {
        return sc.next().replace('_', ' ');
    }

    protected void closeFile() {
        sc.close();
    }
}
