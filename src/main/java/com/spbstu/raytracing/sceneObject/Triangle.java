package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 05.04.14
 * @description
 */
public class Triangle extends SceneObject {


    Point3D a, b, c;
    Vector normal;
    Vector v12, v13;

    public Triangle(Point3D a, Point3D b, Point3D c, Material material, Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.a = a;
        this.b = b;
        this.c = c;

        normal = new Plane(a, b, c, material, attributesMap).normal;
        v12 = new Vector(a, b);
        v13 = new Vector(a, c);
    }

    @Override
    public Vector getStaticNormal(Point3D point) {
        return normal;
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Vector T = new Vector(a, ray.getPoint());
        Vector P = Vector.cross(ray.getDirectionVector(), v13);
        Vector Q = Vector.cross(T, v12);
        double coeff = Vector.scalar(P, v12);
        double u = Vector.scalar(P, T) / coeff;
        if (Vector.scalar(Q, v13) < 0) {
            return crossPoints;
        }
        if (u > 0 && u < 1) {
            double v = Vector.scalar(Q, ray.getDirectionVector()) / coeff;
            if (v > 0 && (u + v < 1)) {
                crossPoints.add(Point3D.tripleLinearCombination(1 - u - v, u, v, a, b, c));
            }
        }
        return crossPoints;
    }
}
