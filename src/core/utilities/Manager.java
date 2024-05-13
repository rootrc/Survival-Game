package core.utilities;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Manager<T> implements Drawable, Updatable{
    public static abstract class SubScreen<T extends core.window.GameComponent> extends Manager<T> {
        private T subScreen;
        public void update() {
            subScreen.update();
        }

        public void draw(Graphics2D g2d) {
            subScreen.draw(g2d);
        }

        public void set(T subScreen) {
            this.subScreen = subScreen;
        }

        public T get() {
            return subScreen;
        }

        public int getWidth() {
            return subScreen.getWidth();
        }

        public int getHeight() {
            return subScreen.getHeight();
        }

        public void show() {
            subScreen.show();
        }
    
        public void stop() {
            subScreen.stop();
        }
    
        public void pause() {
            subScreen.pause();
        }
    
        public void unpause() {
            subScreen.unpause();
        }
    }
    public static abstract class List<T extends Drawable & Updatable> extends Manager<T> {
        private ArrayList<T> list = new ArrayList<>();
        public void update() {
            for (T t : list) {
                t.update();
            }
        }
    
        public void draw(Graphics2D g2d) {
            for (T t : list) {
                t.draw(g2d);
            }
        }

        public void add(T t) {
            list.add(t);
        }

        public ArrayList<T> get() {
            return list;
        }

        public T get(int i) {
            return list.get(i);
        }
    }
}
