package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Mat4Identity;
import transforms.Mat4Scale;
import transforms.Point3D;

public class Pyramid extends Solid {

        public Pyramid() {
                vertexBuffer.add(new Vertex(
                                new Point3D(0, 0, 0),
                                new Col(0xFF0000)));

                vertexBuffer.add(new Vertex(
                                new Point3D(1, 0, 0),
                                new Col(0xFF0000)));

                vertexBuffer.add(new Vertex(
                                new Point3D(1, 1, 0),
                                new Col(0xFF0000)));

                vertexBuffer.add(new Vertex(
                                new Point3D(0, 1, 0),
                                new Col(0xFF0000)));

                vertexBuffer.add(new Vertex(
                                new Point3D(0.5, 0.5, 1),
                                new Col(0x00FF00)));
                // vertexBuffer.add(new Vertex(
                // new Point3D(0.8, -0.5, 1),
                // new Col(0xFF00FF)));

                indexBuffer.add(0);
                indexBuffer.add(1);
                indexBuffer.add(4);

                indexBuffer.add(1);
                indexBuffer.add(2);
                indexBuffer.add(4);

                indexBuffer.add(2);
                indexBuffer.add(3);
                indexBuffer.add(4);

                indexBuffer.add(3);
                indexBuffer.add(0);
                indexBuffer.add(4);

                indexBuffer.add(0);
                indexBuffer.add(1);
                indexBuffer.add(2);

                indexBuffer.add(2);
                indexBuffer.add(3);
                indexBuffer.add(0);

                // indexBuffer.add(2);
                // indexBuffer.add(3);
                // indexBuffer.add(5);

                partBuffer.add(new Part(0, 6, TopologyType.TRIANGLES));

                model = new Mat4Identity()
                                .mul(new Mat4Scale(1, 1, 1));
        }
}
