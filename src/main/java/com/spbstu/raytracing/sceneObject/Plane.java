package com.spbstu.raytracing.sceneObject;


import com.spbstu.raytracing.math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class Plane extends SceneObject {
    /**
     * a*x+b*y+c*z+d=0
     */
    double a, b, c, d;
    Vector3D normal;

    public Plane(double a, double b, double c, double d, Material material,Map<Attributes.Attribute,Matrix3D> attributes) {
        super(material,attributes);
        if (a == 0 && b == 0 && c == 0) {
            throw new IllegalArgumentException("a^2+b^2+c^2>0");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        normal = new Vector3D(a, b, c);
        normal.normalize();
    }

    public Plane(Point3D point, Vector3D normal, Material material,Map<Attributes.Attribute,Matrix3D> attributes) {
        super(material,attributes);
        a = normal.getX();
        b = normal.getY();
        c = normal.getZ();
        d = -Vector3D.scalar(normal, point.toVector3D());
        this.normal = normal;
    }

    public boolean isRightToPlane(Point3D point) {
        return a * point.getX() + b * point.getY() + c * point.getZ() + d >= 0;
    }

    public double distance(Point3D point) {
        return Math.abs(a * point.getX() + b * point.getY() + c * point.getZ() + d) / Math.sqrt(a * a + b * b + c * c);
    }


    public Plane(Point3D point1, Point3D point2, Point3D point3, Material material,Map<Attributes.Attribute,Matrix3D> attributes) {
        super(material,attributes);
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
        normal = new Vector3D(a, b, c);
        normal.normalize();

    }

    @Override
    public Vector3D getStaticNormal(Point3D point) {
        return normal;
    }


    /**
     * http://fxdx.ru/page/vzaimnoe-raspolozhenie-prjamoj-i-ploskosti-v-prostranstve-vzaimnoe-raspolozhenie-dvuh-ploskostej
     *
     * @param ray
     * @return
     */
    @Override
    public List<Point3D> getStaticCrossPoints(Ray3D ray) {
        if (Vector3D.scalar(ray.getDirectionVector(), normal) == 0) {
            return new ArrayList<>();
        }
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double t = -(a * x0 + b * y0 + c * z0 + d) / (a * m + b * n + c * p);
        List<Point3D> crossPoints = new ArrayList<Point3D>();
        crossPoints.add(new Point3D(x0 + m * t, y0 + n * t, z0 + p * t));
        return crossPoints;
    }

}
