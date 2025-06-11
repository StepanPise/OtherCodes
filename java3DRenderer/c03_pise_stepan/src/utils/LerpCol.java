package utils;

import transforms.Col;

public class LerpCol {
    public static Col lerp(Col c1, Col c2, double t) {

        double r = (1 - t) * c1.getR() + t * c2.getR();
        double g = (1 - t) * c1.getG() + t * c2.getG();
        double b = (1 - t) * c1.getB() + t * c2.getB();
        double a = (1 - t) * c1.getA() + t * c2.getA();

        return new Col(r, g, b, a);
    }
}
