package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;


/**
 * @author vva
 */
public class BoundingBox {

    Point min, max;
    double eps;
    double t;

    public BoundingBox(final Point min, final Point max, final double eps) {
        this.min = min;
        this.max = max;
        this.eps = eps;
    }

    public boolean hasIntersection(final Ray ray) {
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
