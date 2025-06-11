package raster;

import transforms.Col;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        if (x < 0 || x >= imageBuffer.getWidth() || y < 0 || y >= imageBuffer.getHeight()) {
            return;
        }

        if (z < depthBuffer.getValue(x, y)) {
            depthBuffer.setValue(x, y, z);
            imageBuffer.setValue(x, y, color);
        }
    }

    public Raster<Col> getImageBuffer() {
        return imageBuffer;
    }

    public void cleanZbuff() {
        depthBuffer.clear();
    }

}
