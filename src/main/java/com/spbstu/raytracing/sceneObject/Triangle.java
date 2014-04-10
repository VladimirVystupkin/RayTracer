package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.Relation;
import com.spbstu.raytracing.math.*;

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
    Vector3D normal;
    Vector3D v12, v13;

    public Triangle(Point3D a, Point3D b, Point3D c, Material material, Map<Attributes.Attribute, Matrix3D> attributesMap) {
        super(material, attributesMap);
        this.a = a;
        this.b = b;
        this.c = c;

        normal = new Plane(a, b, c, material, attributesMap).normal;
        v12 = new Vector3D(a, b);
        v13 = new Vector3D(a, c);
    }

    @Override
    public Vector3D getStaticNormal(Point3D point) {
        return normal;
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray3D ray) {
        List<Point3D> crossPoints = new ArrayList<>();
        Vector3D T = new Vector3D(a, ray.getPoint());
        Vector3D P = Vector3D.cross(ray.getDirectionVector(), v13);
        Vector3D Q = Vector3D.cross(T, v12);
        double coeff = Vector3D.scalar(P, v12);
        double u = Vector3D.scalar(P, T) / coeff;
        if (Vector3D.scalar(Q, v13) < 0) {
            return crossPoints;
        }
        if (u > 0 && u < 1) {
            double v = Vector3D.scalar(Q, ray.getDirectionVector()) / coeff;
            if (v > 0 && (u + v < 1)) {
                crossPoints.add(Point3D.tripleLinearCombination(1 - u - v, u, v, a, b, c));
            }
        }
        return crossPoints;
    }
}
