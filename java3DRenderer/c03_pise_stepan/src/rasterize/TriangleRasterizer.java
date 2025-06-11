package rasterize;

import model.Line;
import model.Vertex;
import raster.ZBuffer;
import shader.Shader;
import transforms.Vec2D;
import utils.*;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final LineRasterizer lineRasterizer;
    Shader shader;

    public TriangleRasterizer(ZBuffer zBuffer, LineRasterizer lineRasterizer, Shader shader) {
        this.zBuffer = zBuffer;
        this.lineRasterizer = lineRasterizer;
        this.shader = shader;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {
        Vec2D pa = viewport(a);
        Vec2D pb = viewport(b);
        Vec2D pc = viewport(c);

        int xA = (int) Math.round(pa.getX());
        int yA = (int) Math.round(pa.getY());
        int xB = (int) Math.round(pb.getX());
        int yB = (int) Math.round(pb.getY());
        int xC = (int) Math.round(pc.getX());
        int yC = (int) Math.round(pc.getY());

        // Debug - vykreslení obrysové hrany trojúhelníku
        if (shader == null) {
            lineRasterizer.drawLine(new Line(xA, yA,
                    xB, yB));
            lineRasterizer.drawLine(new Line(xA, yA,
                    xC, yC));
            lineRasterizer.drawLine(new Line(xB, yB,
                    xC, yC));

        }
        if (yA > yB) {
            Vertex temp = a;
            a = b;
            b = temp;

            int tempX = xA;
            xA = xB;
            xB = tempX;

            int tempY = yA;
            yA = yB;
            yB = tempY;
        }
        if (yB > yC) {
            Vertex temp = b;
            b = c;
            c = temp;
            int tempX = xB;
            xB = xC;
            xC = tempX;
            int tempY = yB;
            yB = yC;
            yC = tempY;
        }
        if (yA > yB) {
            Vertex temp = a;
            a = b;
            b = temp;
            int tempX = xA;
            xA = xB;
            xB = tempX;
            int tempY = yA;
            yA = yB;
            yB = tempY;
        }

        // System.out.println("A,B,C: " + yA + " " + yB + " " + yC);
        // TODO: ořezání

        Lerp<Vertex> lerp = new Lerp<>();

        for (int y = yA; y <= yB; y++) {
            if (y < 0 || y >= zBuffer.getImageBuffer().getHeight())
                continue;
            double tAB = (y - yA) / (double) (yB - yA);
            double tAC = (y - yA) / (double) (yC - yA);
            int xAB = (int) Math.round((1 - tAB) * xA + tAB * xB);
            int xAC = (int) Math.round((1 - tAC) * xA + tAC * xC);
            Vertex ab = lerp.lerp(a, b, tAB);
            Vertex ac = lerp.lerp(a, c, tAC);
            fill(xAB, ab, xAC, ac, y);
        }

        for (int y = yB; y <= yC; y++) {
            if (y < 0 || y >= zBuffer.getImageBuffer().getHeight())
                continue;
            double tBC = (y - yB) / (double) (yC - yB);
            double tAC = (y - yA) / (double) (yC - yA);
            int xBC = (int) Math.round((1 - tBC) * xB + tBC * xC);
            int xAC = (int) Math.round((1 - tAC) * xA + tAC * xC);
            Vertex bc = lerp.lerp(b, c, tBC);
            Vertex ac = lerp.lerp(a, c, tAC);
            fill(xBC, bc, xAC, ac, y);
        }
    }

    private void fill(int x1, Vertex v1, int x2, Vertex v2, int y) {

        if (shader != null) {
            if (x1 > x2) {
                Vertex temp = v1;
                v1 = v2;
                v2 = temp;
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
            }
            Lerp<Vertex> lerp = new Lerp<>();
            // LerpCol lerpCol = new LerpCol();

            for (int x = x1; x <= x2; x++) {
                if (x < 0 || x >= zBuffer.getImageBuffer().getWidth())
                    continue;

                double t = (x - x1) / (double) (x2 - x1);
                Vertex pixel = lerp.lerp(v1, v2, t);
                double interpolatedZ = 1.0 / ((1 - t) / v1.getPosition().getZ() + t / v2.getPosition().getZ());

                // Col colorP = lerpCol.lerp(v1.getColor(), v2.getColor(), t);
                // zBuffer.setPixelWithZTest(x, y, interpolatedZ, new Col(colorP)); //
                // (0.0,1.0,0.0,1.0)

                int pixelX = x - 1;
                if (pixelX >= 0 && pixelX < zBuffer.getImageBuffer().getWidth()) {
                    zBuffer.setPixelWithZTest(pixelX, y, interpolatedZ, shader.getColor(pixel));
                }

                zBuffer.setPixelWithZTest(x, y, interpolatedZ, shader.getColor(pixel));
            }
        }

    }

    public Vec2D viewport(Vertex vertex) {
        Vec2D xy = new Vec2D(vertex.getPosition().getX(), vertex.getPosition().getY());
        return xy.mul(new Vec2D(1, -1)).add(new Vec2D(1, 1))
                .mul(new Vec2D((zBuffer.getImageBuffer().getWidth() - 1) / 2.,
                        (zBuffer.getImageBuffer().getHeight() - 1) / 2.));
    }
}