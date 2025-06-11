package raster;

import transforms.Col;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageBuffer implements Raster<Col> {

    private final BufferedImage image;

    public ImageBuffer(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setValue(int x, int y, Col value) {
        image.setRGB(x, y, value.getRGB());
    }

    @Override
    public Col getValue(int x, int y) {
        return new Col(image.getRGB(x, y));
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void clear() {
        Graphics g = image.getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public Graphics getGraphics() {
        return image.getGraphics();
    }
}
