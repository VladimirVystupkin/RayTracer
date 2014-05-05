package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class Cone extends SceneObject {

    final double r, h;

    public Cone(final double r, final double h, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.r = r;
        this.h = h;
    }


    Vector getNormal(final Point point, boolean isCup) {
        if (!isCup) {
            return new Vector(2 * point.getX(), 2 * point.getY(), 0).getNormalized();
        } else {
            return new Vector(0, 0, -1);
        }
    }


    @Override

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double a = h * h * (m * m + n * n) - r * r * p * p, b = 2 * (h * h * (x0 * m + y0 * n) - r * r * p * z0), c = (h * h * (x0 * x0 + y0 * y0) - r * r * z0 * z0);
        double discr = b * b - 4 * a * c;
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        if (discr >= 0) {
            double t1 = (-b - Math.sqrt(discr)) / (2 * a);
            double t2 = (-b + Math.sqrt(discr)) / (2 * a);
            if ((z0 + p * t1) <= 0 && (z0 + p * t1) > -h) {
                Point point = new Point(x0 + m * t1, y0 + n * t1, z0 + p * t1);
                intersectionInfoList.add(new IntersectionInfo(point, getNormal(point, false), material));
            }
            if ((z0 + p * t2) <= 0 && (z0 + p * t2) > -h) {
                Point point = new Point(x0 + m * t2, y0 + n * t2, z0 + p * t2);
                intersectionInfoList.add(new IntersectionInfo(point, getNormal(point, false), material));
            }
        }
        if (p != 0) {
            double t4 = (-h - z0) / p;
            double x4 = x0 + m * t4;
            double y4 = y0 + n * t4;
            if (x4 * x4 + y4 * y4 <= r * r) {
                Point point = new Point(x4, y4, -h);
                intersectionInfoList.add(new IntersectionInfo(point, getNormal(point, true), material));
            }
        }

        return intersectionInfoList;
    }


    public static Cone fromMap(final HashMap hashMap, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        return new Cone(Double.parseDouble(hashMap.get("radius").toString()), Double.parseDouble(hashMap.get("height").toString()), material, attributesMap);
    }
}
