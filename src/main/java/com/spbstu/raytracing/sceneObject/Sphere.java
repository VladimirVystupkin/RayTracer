package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 11.03.14
 * @description
 */
public class Sphere extends SceneObject {

    Point3D center;
    double radius;

    public Sphere( double radius, Material material,Map<Attributes.AttributeName,Attribute> attributes) {
        super(material,attributes);
        this.center = new Point3D(0,0,0);
        this.radius = radius;
    }

    @Override
    public Vector getStaticNormal(Point3D point) {
        Vector normal = new Vector(point.getX() - center.getX(), point.getY() - center.getY(), point.getZ() - center.getZ());
        normal.normalize();
        return normal;
    }

    @Override
    public List<Point3D> getStaticCrossPoints(Ray line) {
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
            return new ArrayList<>();
        }
        double t1 = (-db - Math.sqrt(discriminant)) / (2 * da);
        double t2 = (-db + Math.sqrt(discriminant)) / (2 * da);
        Point3D point1 = new Point3D(x0 + m * t1, y0 + n * t1, z0 + p * t1);
        Point3D point2 = new Point3D(x0 + m * t2, y0 + n * t2, z0 + p * t2);
        List<Point3D> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        return points;
    }

}
