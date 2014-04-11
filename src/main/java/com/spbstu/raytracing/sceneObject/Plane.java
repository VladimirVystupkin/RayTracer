package com.spbstu.raytracing.sceneObject;


import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class defining plane
 *
 * @author vva
 */
public class Plane extends SceneObject {
    /**
     * a*x+b*y+c*z+d=0
     */
    final double a, b, c, d;
    final Vector normal;

    /**
     * Constructor to make pla by four coefficients(a*x+b*y+c*z+d=0),material and object 3D conversation attributes
     *
     * @param a             coefficient for x coordinate
     * @param b             coefficient for y coordinate
     * @param c             coefficient for z coordinate
     * @param d             coefficient defining start point  p0 for plane (d = -a *p0.x - b*p0.y-c*p0.z)
     * @param material      object material
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public Plane(final double a, final double b, final double c, final double d,
                 @NonNull final Material material, @NonNull final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        normal = new Vector(a, b, c).getNormalized();
    }

//    public Plane(Point point, Vector normal, Material material, Map<Attributes.AttributeName, Attribute> attributes) {
//        super(material, attributes);
//        a = normal.getX();
//        b = normal.getY();
//        c = normal.getZ();
//        d = -Vector.scalar(normal, point.toVector3D());
//        this.normal = normal;
//    }


    /**
     * Constructor to make plane  by 3 points,material and object 3D conversation attributes
     *
     * @param point1        first point to make plane
     * @param point2        second point to make plane
     * @param point3        third point to make plane
     * @param material      object material
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public Plane(@NonNull final Point point1, @NonNull final Point point2, @NonNull final Point point3,
                 @NonNull final Material material, @NonNull final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        double x1 = point1.getX();
        double y1 = point1.getY();
        double z1 = point1.getZ();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double z2 = point2.getZ();
        double x3 = point3.getX();
        double y3 = point3.getY();
        double z3 = point3.getZ();

        a = (y2 - y1) * (z3 - z1) - (y3 - y1) * (z2 - z1);
        b = (x2 - x1) * (z3 - z1) - (x3 - x1) * (z2 - z1);
        c = (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
        d = -x1 * a - y1 * b - z1 * c;
        normal = new Vector(a, b, c).getNormalized();

    }

    @Override
    @NonNull
    public Vector getStaticNormal(@NonNull final Point point) {
        return normal;
    }


    @Override
    @NonNull
    public List<Point> getStaticIntersectionPoints(@NonNull final Ray ray) {
        if (Vector.scalar(ray.getDirectionVector(), normal) == 0) {
            return new ArrayList<>();
        }
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double t = -(a * x0 + b * y0 + c * z0 + d) / (a * m + b * n + c * p);
        List<Point> intersectionPoints = new ArrayList<Point>();
        intersectionPoints.add(new Point(x0 + m * t, y0 + n * t, z0 + p * t));
        return intersectionPoints;
    }

}
