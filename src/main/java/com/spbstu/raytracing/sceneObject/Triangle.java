package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class defining triangle
 *
 * @author vva
 */
public class Triangle extends SceneObject {


    final Point a, b, c;
    final Vector normal;
    final Vector v12, v13;

    /**
     * Constructor  defining  triangle by  clockwise  going points ,material and object 3D conversation attributes
     *
     * @param a             first triangle point
     * @param b             second triangle point
     * @param c             third triangle point
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public Triangle(@NonNull final Point a,@NonNull final  Point b,@NonNull final  Point c,
                    @NonNull final Material material,@NonNull final  Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.a = a;
        this.b = b;
        this.c = c;

        normal = new Plane(a, b, c, material, attributesMap).normal;
        v12 = new Vector(a, b);
        v13 = new Vector(a, c);
    }

    @Override
    @NonNull
    public Vector getStaticNormal(@NonNull final Point point) {
        return normal;
    }

    @Override
    @NonNull
    public List<Point> getStaticIntersectionPoints(@NonNull final Ray ray) {
        List<Point> intersectionPoints = new ArrayList<>();
        Vector T = new Vector(a, ray.getPoint());
        Vector P = Vector.cross(ray.getDirectionVector(), v13);
        Vector Q = Vector.cross(T, v12);
        double coeff = Vector.scalar(P, v12);
        double u = Vector.scalar(P, T) / coeff;
        if (Vector.scalar(Q, v13) < 0) {
            return intersectionPoints;
        }
        if (u > 0 && u < 1) {
            double v = Vector.scalar(Q, ray.getDirectionVector()) / coeff;
            if (v > 0 && (u + v < 1)) {
                intersectionPoints.add(Point.tripleLinearCombination(1 - u - v, u, v, a, b, c));
            }
        }
        return intersectionPoints;
    }
}
