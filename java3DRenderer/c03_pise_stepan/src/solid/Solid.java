package solid;

import model.Part;
import model.Vertex;
import transforms.Mat4;

import java.util.ArrayList;
import java.util.List;

public abstract class Solid {
    protected List<Vertex> vertexBuffer = new ArrayList<Vertex>();
    protected List<Integer> indexBuffer = new ArrayList<Integer>();
    protected List<Part> partBuffer = new ArrayList<Part>();
    private boolean isActive = false;

    protected Mat4 model;

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Part> getPartBuffer() {
        return partBuffer;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
