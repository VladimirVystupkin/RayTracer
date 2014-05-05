package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class Sphere extends SceneObject {

    final double radius;

    public Sphere(final double radius, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.radius = radius;
    }


    Vector getNormal(final Point point) {
        return new Vector(point.getX(), point.getY(), point.getZ()).getNormalized();
    }

    @Override

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();

        double a1 = x0;
        double b1 = y0;
        double c1 = z0;

        double da = m * m + n * n + p * p;
        double db = 2 * (a1 * m + b1 * n + c1 * p);
        double dc = (a1 * a1 + b1 * b1 + c1 * c1 - radius * radius);

        double discriminant = db * db - 4 * da * dc;
        if (discriminant < 0) {
            return new ArrayList<>();
        }
        double t1 = (-db - Math.sqrt(discriminant)) / (2 * da);
        double t2 = (-db + Math.sqrt(discriminant)) / (2 * da);
        Point point1 = new Point(x0 + m * t1, y0 + n * t1, z0 + p * t1);
        Point point2 = new Point(x0 + m * t2, y0 + n * t2, z0 + p * t2);
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        intersectionInfoList.add(new IntersectionInfo(point1, getNormal(point1), material));
        intersectionInfoList.add(new IntersectionInfo(point2, getNormal(point2), material));
        return intersectionInfoList;
    }


    public static Sphere fromMap(final HashMap hashMap, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        return new Sphere(Double.parseDouble(hashMap.get("radius").toString()), material, attributesMap);
    }
}
