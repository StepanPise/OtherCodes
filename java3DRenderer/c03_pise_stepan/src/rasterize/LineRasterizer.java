package rasterize;

import model.Line;
import model.Vertex;
import raster.Raster;
import raster.ZBuffer;
import transforms.Col;

public abstract class LineRasterizer {
    // TODO: vyměnit raster za zBuffer
    protected final Raster<Col> raster;
    // protected final ZBuffer zBuffer;
    protected int color;

    public void rasterize(Vertex a, Vertex b) {

    }

    public LineRasterizer(Raster<Col> raster) {
        this.raster = raster;
        this.color = 0xff0000;
    }

    public LineRasterizer(Raster<Col> raster, int color) {
        this.raster = raster;
        this.color = color;
    }

    public void drawLine(Line line) {
        System.out.println("Draw line");
    }

    public void setColor(int color) {
        this.color = color;
    }
}
