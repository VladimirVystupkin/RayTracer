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
public class Triangle extends SceneObject {


    final Point a, b, c;
    final Vector normal;
    final Vector v12, v13;

    public Triangle(final Point a, final Point b, final Point c,
                    final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.a = a;
        this.b = b;
        this.c = c;
        normal = new Plane(a, b, c, material, attributesMap).normal;
        v12 = new Vector(a, b);
        v13 = new Vector(a, c);
    }


    public Vector getNormal() {
        return normal;
    }

    @Override

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        Vector T = new Vector(a, ray.getPoint());
        Vector P = Vector.cross(ray.getDirectionVector(), v13);
        Vector Q = Vector.cross(T, v12);
        double coeff = Vector.scalar(P, v12);
        double u = Vector.scalar(P, T) / coeff;
        if (Vector.scalar(Q, v13) < 0) {
            return intersectionInfoList;
        }
        if (u > 0 && u < 1) {
            double v = Vector.scalar(Q, ray.getDirectionVector()) / coeff;
            if (v > 0 && (u + v < 1)) {
                Point point = Point.tripleLinearCombination(1 - u - v, u, v, a, b, c);
                intersectionInfoList.add(new IntersectionInfo(point, getNormal(), material));
            }
        }
        return intersectionInfoList;
    }


    public static Triangle fromMap(final HashMap hashMap, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        HashMap aAttributes = (HashMap) hashMap.get("a");
        double ax = aAttributes.containsKey("x") ? Double.parseDouble(aAttributes.get("x").toString()) : 0;
        double ay = aAttributes.containsKey("y") ? Double.parseDouble(aAttributes.get("y").toString()) : 0;
        double az = aAttributes.containsKey("z") ? Double.parseDouble(aAttributes.get("z").toString()) : 0;
        Point a = new Point(ax, ay, az);
        HashMap bAttributes = (HashMap) hashMap.get("b");
        double bx = bAttributes.containsKey("x") ? Double.parseDouble(bAttributes.get("x").toString()) : 0;
        double by = bAttributes.containsKey("y") ? Double.parseDouble(bAttributes.get("y").toString()) : 0;
        double bz = bAttributes.containsKey("z") ? Double.parseDouble(bAttributes.get("z").toString()) : 0;
        Point b = new Point(bx, by, bz);
        HashMap cAttributes = (HashMap) hashMap.get("c");
        double cx = cAttributes.containsKey("x") ? Double.parseDouble(cAttributes.get("x").toString()) : 0;
        double cy = cAttributes.containsKey("y") ? Double.parseDouble(cAttributes.get("y").toString()) : 0;
        double cz = cAttributes.containsKey("z") ? Double.parseDouble(cAttributes.get("z").toString()) : 0;
        Point c = new Point(cx, cy, cz);
        return new Triangle(a, b, c, material, attributesMap);
    }
}
