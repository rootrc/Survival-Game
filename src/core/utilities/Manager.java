package core.utilities;

import core.window.GameComponent;

public abstract class Manager<T> {
    public static abstract class TComponent<T extends GameComponent> extends Manager<T> {
        private T component;

        public void set(T GameComponent) {
            this.component = GameComponent;
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

        public void pause() {
            component.pause();
        }

        public void unpause() {
            component.unpause();
        }
    }

}
