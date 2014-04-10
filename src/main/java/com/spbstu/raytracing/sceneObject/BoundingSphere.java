package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Ray;

/**
 * @autor vystupkin
 * @date 02.04.14
 * @description
 */
public class BoundingSphere {

    double r;

    public BoundingSphere(double r) {
        this.r = r;
    }


    public boolean crosses(Ray ray) {
        double a1 = ray.getPoint().getX(), b1 = ray.getPoint().getY(), c1 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();


        double da = m * m + n * n + p * p;
        double db = 2 * (a1 * m + b1 * n + c1 * p);
        double dc = (a1 * a1 + b1 * b1 + c1 * c1 - r * r);

        return (db * db - 4 * da * dc) >= 0;

    }
}
