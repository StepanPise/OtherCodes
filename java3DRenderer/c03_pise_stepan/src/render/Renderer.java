package render;

import model.Line;
import model.Part;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import shader.Shader;
import solid.Solid;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Renderer {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;
    private Mat4 view, proj;
    private int width, height;
    private Mat4 mvp;
    private Shader shader;

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height,
            Mat4 view, Mat4 proj) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;

        this.width = width;
        this.height = height;
        this.view = new Mat4Identity();
        this.proj = new Mat4Identity();
    }

    public void renderSolid(Solid solid) {

        // TODO: MVP matice
        mvp = new Mat4(solid.getModel().mul(view).mul(proj));

        for (Part part : solid.getPartBuffer()) {
            switch (part.getType()) {
                case LINES:
                    renderLines(solid, part);
                    break;
                case TRIANGLES:
                    renderTriangles(solid, part);
                    break;
                default:
                    break;
            }
        }
    }

    private void renderLines(Solid solid, Part part) {
        int start = part.getStart();
        for (int i = 0; i < part.getCount(); i++) {
            int indexA = start;
            int indexB = start + 1;
            start += 2;

            Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexA));
            Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexB));

            Point3D pointA = a.getPosition().mul(mvp);
            Point3D pointB = b.getPosition().mul(mvp);

            Point3D aDehomog = pointA.mul(1 / pointA.getW());
            Point3D bDehomog = pointB.mul(1 / pointB.getW());

            Vertex vertexA = new Vertex(new Point3D(aDehomog), a.getColor());
            Vertex vertexB = new Vertex(new Point3D(bDehomog), b.getColor());

            Vec3D pa = transformToWindow(
                    new Vec3D(vertexA.getPosition().getX(), vertexA.getPosition().getY(),
                            vertexA.getPosition().getZ()));
            Vec3D pb = transformToWindow(
                    new Vec3D(vertexB.getPosition().getX(), vertexB.getPosition().getY(),
                            vertexB.getPosition().getZ()));

            int xA = (int) Math.round(pa.getX());
            int yA = (int) Math.round(pa.getY());
            int xB = (int) Math.round(pb.getX());
            int yB = (int) Math.round(pb.getY());

            lineRasterizer.drawLine(new Line(xA, yA,
                    xB, yB));

            // lineRasterizer.rasterize(vertexA, vertexB);
        }
    }

    private void renderTriangles(Solid solid, Part part) {
        int start = part.getStart();
        for (int i = 0; i < part.getCount(); i++) {
            int indexA = start;
            int indexB = start + 1;
            int indexC = start + 2;
            start += 3;

            Vertex a = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexA));
            Vertex b = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexB));
            Vertex c = solid.getVertexBuffer().get(solid.getIndexBuffer().get(indexC));

            Point3D pointA = a.getPosition().mul(mvp);
            Point3D pointB = b.getPosition().mul(mvp);
            Point3D pointC = c.getPosition().mul(mvp);

            if (isInView(pointA, pointB, pointC)) {
                Point3D aDehomog = pointA.mul(1 / pointA.getW());
                Point3D bDehomog = pointB.mul(1 / pointB.getW());
                Point3D cDehomog = pointC.mul(1 / pointC.getW());

                Vertex vertexA = new Vertex(new Point3D(aDehomog), a.getColor());
                Vertex vertexB = new Vertex(new Point3D(bDehomog), b.getColor());
                Vertex vertexC = new Vertex(new Point3D(cDehomog), c.getColor());

                triangleRasterizer.rasterize(vertexA, vertexB, vertexC);

            }

        }
    }

    private void clipTriangle(Vertex a, Vertex b, Vertex c) {
        // TODO: fast clip

        // TODO: ořezání podle z
        float zMin = 0;
        // 1. seřadit vrcholy pod z od max po min. A = max
        if (a.getPosition().getZ() < zMin)
            return;

        if (b.getPosition().getZ() < zMin) {
            // TODO: interpolací spočítáme nový trojúhelník
            // triangleRasterizer.rasterize(a, ab, ac);
            return;
        }

        if (c.getPosition().getZ() < zMin) {
            // TODO: interpolací spočítáme 2 nové trojúhelníky a rasterizujeme
            return;
        }

        // TODO: Nic z předchozího neplatí, rasterizujeme původné trojúhelník

    }

    public Vec3D transformToWindow(Vec3D v) {
        return v
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2, (height - 1) / 2, 1));
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public boolean isInView(Point3D pointA, Point3D pointB, Point3D pointC) {

        boolean boolA = pointA.getX() > -pointA.getW() && pointA.getX() < pointA.getW() &&
                pointA.getY() > -pointA.getW() && pointA.getY() < pointA.getW() &&
                pointA.getZ() > 0 && pointA.getZ() < pointA.getW();

        boolean boolB = pointB.getX() > -pointB.getW() && pointB.getX() < pointB.getW() &&
                pointB.getY() > -pointB.getW() && pointB.getY() < pointB.getW() &&
                pointB.getZ() > 0 && pointB.getZ() < pointB.getW();

        boolean boolC = pointC.getX() > -pointC.getW() && pointC.getX() < pointC.getW() &&
                pointC.getY() > -pointC.getW() && pointC.getY() < pointC.getW() &&
                pointC.getZ() > 0 && pointC.getZ() < pointC.getW();

        return boolA && boolB && boolC;
    }

}
