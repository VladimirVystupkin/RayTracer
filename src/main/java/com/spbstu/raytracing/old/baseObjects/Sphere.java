package com.spbstu.raytracing.old.baseObjects;

import com.spbstu.raytracing.old.Line;
import com.spbstu.raytracing.old.Point3D;
import com.spbstu.raytracing.old.Vector3D;

import java.util.List;

/**
 * @author vva
 * @date 11.03.14
 * @description
 */
public class Sphere extends BaseObject {

    Point3D center;
    double radius;
    int color;

    public Sphere(Point3D center, double radius, int color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    public Vector3D getStaticNormal(Point3D point) {
        return null;
    }

    public List<Point3D> getStaticCrossPoints(Line line) {
        return null;
    }


    public Vector3D getNormal(Point3D point) {
        Vector3D normal = new Vector3D(point.getX() - center.getX(), point.getY() - center.getY(), point.getZ() - center.getZ());
        normal.normalize();
        return normal;
    }

    public Point3D getCrossPoint(Line line) {
        double x0 = line.getPoint().getX(), y0 = line.getPoint().getY(), z0 = line.getPoint().getZ();
        double m = line.getDirectionVector().getX(), n = line.getDirectionVector().getY(), p = line.getDirectionVector().getZ();


        double a1 = x0 - center.getX();
        double b1 = y0 - center.getY();
        double c1 = z0 - center.getZ();


        double da = m * m + n * n + p * p;
        double db = 2 * (a1 * m + b1 * n + c1 * p);
        double dc = (a1 * a1 + b1 * b1 + c1 * c1 - radius * radius);

        double discriminant = db * db - 4 * da * dc;
        if (discriminant < 0) {
            return null;
        }
        double t1 = (-db - Math.sqrt(discriminant)) / (2 * da);
        double t2 = (-db + Math.sqrt(discriminant)) / (2 * da);
        Point3D point1 = new Point3D(x0 + m * t1, y0 + n * t1, z0 + p * t1);
        Point3D point2 = new Point3D(x0 + m * t2, y0 + n * t2, z0 + p * t2);
        Point3D point = null;
        if (point1.getZ() < point2.getZ()) {
            point = point1;
        }
        if (point2.getZ() >= 0) {
            point = point2;
        }
        if (point == null) {
            return null;
        }
//        if (Vector3D.scalar(getNormal(point), line.getDirectionVector()) <= 0) {
//            throw new IllegalArgumentException("SHIT");
//        }
        return point;
    }

    public int getColor(Point3D point) {
        return color;
    }
}
