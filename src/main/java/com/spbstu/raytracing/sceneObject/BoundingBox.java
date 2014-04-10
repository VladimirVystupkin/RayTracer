package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;

/**
 * @autor vystupkin
 * @date 02.04.14
 * @description
 */
public class BoundingBox {

    Point3D min, max;
    double eps;
    double t;

    public BoundingBox(Point3D min, Point3D max, double eps) {
        this.min = min;
        this.max = max;
        this.eps = eps;
    }

    public BoundingBox(double a, double b, double c, double eps) {
        this.min = new Point3D(-a, -b, -c);
        this.max = new Point3D(a, b, c);
        this.eps = eps;
    }


    private double max(double a, double b) {
        return a >= b ? a : b;
    }

    private double min(double a, double b) {
        return a < b ? a : b;
    }

    public boolean crosses(Ray ray) {
        Point3D rayOrigin = ray.getPoint();
        Vector rayDirection = ray.getDirectionVector();

        double d0 = -eps;
        double d1 = eps;
        double dirX = Math.abs(rayDirection.getX())< eps?eps:rayDirection.getX();
        double dirY = Math.abs(rayDirection.getY())< eps?eps:rayDirection.getY();
        double dirZ = Math.abs(rayDirection.getZ())< eps?eps:rayDirection.getZ();
        // if (Math.abs(rayDirection.getX()) > eps) {
        d0 = (min.getX() - rayOrigin.getX()) / dirX;
        d1 = (max.getX() - rayOrigin.getX()) / dirX;
        if (d1 < d0) {
            t = d1;
            d1 = d0;
            d0 = t;
        }
        //}

//        if (Math.abs(rayDirection.getY()) > eps) {
        double t0 = (min.getY() - rayOrigin.getY()) / dirY;
        double t1 = (max.getY() - rayOrigin.getY()) / dirY;

        if (t1 < t0) {
            t = t1;
            t1 = t0;
            t0 = t;
        }
        d0 = max(d0, t0);
        d1 = min(d1, t1);
//        }

//        if (Math.abs(rayDirection.getZ()) > eps) {
        t0 = (min.getZ() - rayOrigin.getZ()) / dirZ;
        t1 = (max.getZ() - rayOrigin.getZ()) / dirZ;
        if (t1 < t0) {
            t = t1;
            t1 = t0;
            t0 = t;
        }
        d0 = max(d0, t0);
        d1 = min(d1, t1);
//        }

        return !(d1 < d0 || d0 == -eps);
    }

    public Point3D getMin() {
        return min;
    }

    public void setMin(Point3D min) {
        this.min = min;
    }

    public Point3D getMax() {
        return max;
    }

    public void setMax(Point3D max) {
        this.max = max;
    }
}
