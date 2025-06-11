package raster;

public interface Raster<T> {
    void setValue(int x, int y, T value);

    T getValue(int x, int y);

    int getWidth();

    int getHeight();

    void clear();

    default boolean isInRaster(int x, int y) {

        if ((x < getWidth() && x >= 0) && (y < getHeight() && y >= 0)) {
            return true;
        } else {
            return false;
        }

    }
}
