package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;

import java.util.*;
import java.util.List;

/**
 * @autor vystupkin
 * @date 01.04.14
 * @description
 */
public class Torus extends SceneObject {


    double innerR, outerR;
    BoundingSphere boundingSphere;
    BoundingBox boundingBox;

    public Torus(double innerR, double outerR, Material material, Map<Attributes.AttributeName, Attribute> attributes) {
        super(material, attributes);
        this.innerR = innerR;
        this.outerR = outerR;
        this.boundingSphere = new BoundingSphere(innerR + outerR + 1);
        this.boundingBox = new BoundingBox(2 * (innerR + outerR + 1), 2 * (innerR + outerR + 1), 2 * (innerR + 1), 1e-5);
    }


    @Override
    public Vector getStaticNormal(Point3D point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double normalX = 2 * (x * x + y * y + z * z + outerR * outerR - innerR * innerR) * 2 * x - 8 * outerR * outerR * x;
        double normalY = 2 * (x * x + y * y + z * z + outerR * outerR - innerR * innerR) * 2 * y - 8 * outerR * outerR * y;
        double normalZ = 2 * (x * x + y * y + z * z + outerR * outerR - innerR * innerR) * 2 * z;
        Vector normal = new Vector(normalX, normalY, normalZ);
        normal.normalize();
        return normal;
    }


    public double getDiff(Point3D point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double value = Math.pow(x * x + y * y + z * z + outerR * outerR - innerR * innerR, 2) - 4 * outerR * outerR * (x * x + y * y);
        return Math.abs(value);
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray ray) {
        if (!boundingSphere.crosses(ray)) {
            return new ArrayList<>();
        }
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double a1 = (m * m + n * n + p * p);
        double b1 = 2 * (x0 * m + y0 * n + z0 * p);
        double c1 = x0 * x0 + y0 * y0 + z0 * z0 + outerR * outerR - innerR * innerR;

        double a2 = a1 * a1;
        double b2 = 2 * a1 * b1;
        double c2 = (2 * a1 * c1 + b1 * b1);
        double d2 = 2 * b1 * c1;
        double e2 = c1 * c1;

        double c3 = 4 * outerR * outerR * (m * m + n * n);
        double d3 = 8 * outerR * outerR * (m * x0 + n * y0);
        double e3 = 4 * outerR * outerR * (x0 * x0 + y0 * y0);

        double a = a2;
        double b = b2;
        double c = c2 - c3;
        double d = d2 - d3;
        double e = e2 - e3;
        double t[] = Solvers.solveQuartic(a,b,c,d,e);

        if (t == null) {
            return new ArrayList<>();
        }
        List<Point3D> crossPoints = new ArrayList<>();
        for (int i = 0; i < t.length; i++) {
            Point3D point = new Point3D(x0 + m * t[i], y0 + n * t[i], z0 + p * t[i]);
            crossPoints.add(point);
        }
        return crossPoints;
    }



    public static double getDiff(double a, double b, double c, double d, double e, double x) {
        return a * x * x * x * x + b * x * x * x + c * x * x + d * x + e;
    }
}
