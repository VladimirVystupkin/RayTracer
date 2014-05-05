package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @autor vva
 */
public class Cylinder extends SceneObject {
    final double r, h;

    public Cylinder(final double r, final double h, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.r = r;
        this.h = h / 2;
    }


    Vector getNormal(Point point, boolean isCup) {
        if (!isCup) {
            return new Vector(2 * point.getX(), 2 * point.getY(), 0).getNormalized();
        } else {
            if (point.getZ() > 0) {
                return new Vector(0, 0, 1);
            } else {
                return new Vector(0, 0, -1);
            }
        }
    }

    @Override
    public List<IntersectionInfo> getStaticIntersectionInfo(Ray ray) {
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double a = (m * m + n * n), b = 2 * (x0 * m + y0 * n), c = (x0 * x0 + y0 * y0 - r * r);
        double discr = b * b - 4 * a * c;
        List<IntersectionInfo> points = new ArrayList<>();
        if (discr >= 0) {
            double t1 = (-b - Math.sqrt(discr)) / (2 * a);
            double t2 = (-b + Math.sqrt(discr)) / (2 * a);
            if (Math.abs(z0 + p * t1) < h) {
                Point point = new Point(x0 + m * t1, y0 + n * t1, z0 + p * t1);
                points.add(new IntersectionInfo(point, getNormal(point, false), material));
            }
            if (Math.abs(z0 + p * t2) < h) {
                Point point = new Point(x0 + m * t2, y0 + n * t2, z0 + p * t2);
                points.add(new IntersectionInfo(point, getNormal(point, false), material));
            }
        }
        if (p != 0) {
            double t3 = (h - z0) / p;
            double x3 = x0 + m * t3;
            double y3 = y0 + n * t3;
            if (x3 * x3 + y3 * y3 <= r * r) {
                Point point = new Point(x3, y3, h);
                points.add(new IntersectionInfo(point, getNormal(point, true), material));
            }
            double t4 = (-h - z0) / p;
            double x4 = x0 + m * t4;
            double y4 = y0 + n * t4;
            if (x4 * x4 + y4 * y4 <= r * r) {
                Point point = new Point(x4, y4, -h);
                points.add(new IntersectionInfo(point, getNormal(point, true), material));
            }
        }

        return points;
    }


    public static Cylinder fromMap(HashMap hashMap, Material material, Map<Attributes.AttributeName, Attribute> attributesMap) {
        return new Cylinder(Double.parseDouble(hashMap.get("radius").toString()), Double.parseDouble(hashMap.get("height").toString()), material, attributesMap);
    }
}
