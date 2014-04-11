package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Bounding box to fast detect no intersection
 * @autor vva
 */
public class BoundingBox {

    Point min, max;
    double eps;
    double t;

    /**
     * Constructor which makes box by minimum  and maximum by all axises points
     * @param min minimum by all axises point
     * @param max maximum by all axises point
     * @param eps intersection detection error
     */
    public BoundingBox(@NonNull final Point min,@NonNull final Point max,final double eps) {
        this.min = min;
        this.max = max;
        this.eps = eps;
    }


    /**
     * Detects if bounding box has intersection
     * @param ray ray to detect intersection
     * @return true if bounding box has intersection,else - false
     */
    public boolean hasIntersection(@NonNull final Ray ray) {
        Point rayOrigin = ray.getPoint();
        Vector rayDirection = ray.getDirectionVector();

        double d0 = -eps;
        double d1 = eps;
        double dirX = Math.abs(rayDirection.getX()) < eps ? eps : rayDirection.getX();
        double dirY = Math.abs(rayDirection.getY()) < eps ? eps : rayDirection.getY();
        double dirZ = Math.abs(rayDirection.getZ()) < eps ? eps : rayDirection.getZ();
        d0 = (min.getX() - rayOrigin.getX()) / dirX;
        d1 = (max.getX() - rayOrigin.getX()) / dirX;
        if (d1 < d0) {
            t = d1;
            d1 = d0;
            d0 = t;
        }
        double t0 = (min.getY() - rayOrigin.getY()) / dirY;
        double t1 = (max.getY() - rayOrigin.getY()) / dirY;

        if (t1 < t0) {
            t = t1;
            t1 = t0;
            t0 = t;
        }
        d0 = Math.max(d0, t0);
        d1 = Math.min(d1, t1);
        t0 = (min.getZ() - rayOrigin.getZ()) / dirZ;
        t1 = (max.getZ() - rayOrigin.getZ()) / dirZ;
        if (t1 < t0) {
            t = t1;
            t1 = t0;
            t0 = t;
        }
        d0 = Math.max(d0, t0);
        d1 = Math.min(d1, t1);
        return !(d1 < d0 || d0 == -eps);
    }

}
