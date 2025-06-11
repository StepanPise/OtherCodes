package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Mat4Identity;
import transforms.Mat4Scale;
import transforms.Point3D;
import transforms.Vec2D;

public class Arrow extends Solid {

    public Arrow() {

        vertexBuffer.add(new Vertex(new Point3D(-0.5, 0.0, 1.0)));
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0.0, 1.0)));

        vertexBuffer.add(new Vertex(new Point3D(0.5, -0.2, 1),
                new Col(0xff0000),
                new Vec2D(0.5, 0.9)));
        vertexBuffer.add(new Vertex(new Point3D(0.7, 0.0, 1),
                new Col(0x00ff00),
                new Vec2D(0.9, 0.5)));
        vertexBuffer.add(new Vertex(new Point3D(0.5, 0.2, 1),
                new Col(0x0000ff),
                new Vec2D(0.5, 0.1)));

        // LINE
        indexBuffer.add(0);
        indexBuffer.add(1);

        // TRIANGLE
        indexBuffer.add(2);
        indexBuffer.add(3);
        indexBuffer.add(4);

        partBuffer.add(new Part(0, 1, TopologyType.LINES));
        partBuffer.add(new Part(2, 1, TopologyType.TRIANGLES));

        model = new Mat4Identity()
                .mul(new Mat4Scale(1, 1, 1));
    }
}