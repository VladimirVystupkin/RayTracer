package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class defining cone
 *
 * @autor vva
 */
public class Cone extends SceneObject {

    double r, h;

    /**
     * Constructor to make cone
     *
     * @param r             radius
     * @param h             height
     * @param material      object material
     * @param attributesMap map from attributes
     * @see SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public Cone(double r, double h, @NonNull Material material, @NonNull Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.r = r;
        this.h = h;
    }

    @Override
    Vector getStaticNormal(@NonNull Point point) {
        PointExt pointExt = (PointExt) point;
        if ((boolean) pointExt.getInfo()) {
            return new Vector(2 * point.getX(), 2 * point.getY(), 0).getNormalized();
        } else {
            return new Vector(0, 0, -1);
        }
    }

    @Override
    public List<Point> getStaticIntersectionPoints(@NonNull Ray ray) {
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double a = h * h * (m * m + n * n) - r * r * p * p, b = 2 * (h * h * (x0 * m + y0 * n) - r * r * p * z0), c = (h * h * (x0 * x0 + y0 * y0) - r * r * z0 * z0);
        double discr = b * b - 4 * a * c;
        List<Point> points = new ArrayList<>();
        if (discr >= 0) {
            double t1 = (-b - Math.sqrt(discr)) / (2 * a);
            double t2 = (-b + Math.sqrt(discr)) / (2 * a);
            if ((z0 + p * t1) <= 0 && (z0 + p * t1) > -h) {
                points.add(new PointExt(x0 + m * t1, y0 + n * t1, z0 + p * t1, true));
            }
            if ((z0 + p * t2) <= 0 && (z0 + p * t2) > -h) {
                points.add(new PointExt(x0 + m * t2, y0 + n * t2, z0 + p * t2, true));
            }
        }
        if (p != 0) {
            double t4 = (-h - z0) / p;
            double x4 = x0 + m * t4;
            double y4 = y0 + n * t4;
            if (x4 * x4 + y4 * y4 <= r * r) {
                points.add(new PointExt(x4, y4, -h, false));
            }
        }

        return points;
    }

    @Override
    @NonNull
    public List<Point> getIntersectionPoints(@NonNull final Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>();
        Point startPoint = ray.getPoint();
        Point endPoint = Point.translate(startPoint, ray.getDirectionVector());
        Ray transformedRay = new Ray(Matrix.multiply(matrix, startPoint),
                Matrix.multiply(matrix, endPoint));
        for (Point staticIntersectionPoint : getStaticIntersectionPoints(transformedRay)) {
            intersectionPoints.add(Matrix.multiply(inverted, (PointExt) staticIntersectionPoint));

        }
        return intersectionPoints;
    }

    @NonNull
    public Vector getNormal(@NonNull final Point point) {
        Point transformed = Matrix.multiply(matrix, (PointExt) point);
        Vector staticNormal = getStaticNormal(transformed);
        Point startPoint = new Point(0, 0, 0);
        Point endPoint = staticNormal.toPoint3D();
        return new Vector(Matrix.multiply(inverted, startPoint),
                Matrix.multiply(inverted, endPoint)).getNormalized();
    }
}
