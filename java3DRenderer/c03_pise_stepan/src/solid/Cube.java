package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Mat4Identity;
import transforms.Mat4Scale;
import transforms.Point3D;

public class Cube extends Solid {

    public Cube() {
        vertexBuffer.add(new Vertex(new Point3D(-0.5, -0.5, -0.5), new Col(0xFF0000))); // v0
        vertexBuffer.add(new Vertex(new Point3D(0.5, -0.5, -0.5), new Col(0x00FF00))); // v1
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0.5, -0.5), new Col(0x0000FF))); // v2
        vertexBuffer.add(new Vertex(new Point3D(-0.5, 0.5, -0.5), new Col(0xFFFF00))); // v3

        vertexBuffer.add(new Vertex(new Point3D(-0.5, -0.5, 0.5), new Col(0xFF00FF))); // v4
        vertexBuffer.add(new Vertex(new Point3D(0.5, -0.5, 0.5), new Col(0x00FFFF))); // v5
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0.5, 0.5), new Col(0xFFFFFF))); // v6
        vertexBuffer.add(new Vertex(new Point3D(-0.5, 0.5, 0.5), new Col(0x888888))); // v7

        int[] indices = {
                0, 1, 2, 0, 2, 3,
                4, 5, 6, 4, 6, 7,
                0, 1, 5, 0, 5, 4,
                2, 3, 7, 2, 7, 6,
                0, 3, 7, 0, 7, 4,
                1, 2, 6, 1, 6, 5
        };

        for (int index : indices) {
            indexBuffer.add(index);
        }

        partBuffer.add(new Part(0, 12, TopologyType.TRIANGLES));

        model = new Mat4Identity().mul(new Mat4Scale(1, 1, 1));
    }
}
