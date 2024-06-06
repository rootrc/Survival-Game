package core.utilities;

import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Manager<T> implements Drawable, Updatable{
    public static abstract class Component<T extends core.window.GameComponent> extends Manager<T> {
        private T component;
        public void update() {
            component.update();
        }

        public void draw(Graphics2D g2d) {
            component.draw(g2d);
        }

        public void set(T subScreen) {
            this.component = subScreen;
        }

        public T get() {
            return component;
        }

        public int getWidth() {
            return component.getWidth();
        }

        public int getHeight() {
            return component.getHeight();
        }

        protected void setLocation(int x, int y) {
            component.setLocation(x, y);
        }

        public void show() {
            component.show();
        }
    
        public void stop() {
            component.stop();
        }
    
        public void pause() {
            component.pause();
        }
    
        public void unpause() {
            component.unpause();
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
