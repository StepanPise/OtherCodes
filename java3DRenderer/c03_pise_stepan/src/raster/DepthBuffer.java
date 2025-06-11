package raster;

public class DepthBuffer implements Raster<Double> {

    private final double[][] buffer;
    private final int width, height;

    public DepthBuffer(int width, int height) {
        this.buffer = new double[width][height];
        this.width = width;
        this.height = height;
        clear();
    }

    @Override
    public void setValue(int x, int y, Double value) {
        if (isInRaster(x, y)) {
            buffer[x][y] = value;
        }
    }

    @Override
    public Double getValue(int x, int y) {
        return buffer[x][y];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffer[x][y] = 1.0; // Double.POSITIVE_INFINITY
            }
        }
    }
}
