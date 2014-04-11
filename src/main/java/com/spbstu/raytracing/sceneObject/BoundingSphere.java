package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Ray;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Bounding sphere to fast detect no intersection
 *
 * @autor vva
 * @date 02.04.14
 */
public class BoundingSphere {

    double r;

    /**
     * Constructor which makes bounding sphere by radius
     *
     * @param r radius of bounding sphere
     */
    public BoundingSphere(final double r) {
        this.r = r;
    }

    /**
     * Detects if bounding sphere has intersection
     * @param ray ray to detect intersection
     * @return true if bounding sphere has intersection,else - false
     */
    public boolean hasIntersection(@NonNull final Ray ray) {
        double a1 = ray.getPoint().getX(), b1 = ray.getPoint().getY(), c1 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double da = m * m + n * n + p * p;
        double db = 2 * (a1 * m + b1 * n + c1 * p);
        double dc = (a1 * a1 + b1 * b1 + c1 * c1 - r * r);
        return (db * db - 4 * da * dc) >= 0;
    }
}
