package utils;

import model.Vectorizable;
import transforms.Col;

// Vertex implements Vectorizable<Vertex>
public class Lerp<T extends Vectorizable<T>> {
    public T lerp(T v1, T v2, double t) {
        return v1.mul(1 - t).add(v2.mul(t));
    }

}
