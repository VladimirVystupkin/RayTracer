package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class defining sphere
 *
 * @author vva
 */
public class Sphere extends SceneObject {

    final double radius;

    /**
     * class defining sphere by radius,material and object 3D conversation attributes
     *
     * @param radius        sphere radius
     * @param material      object material
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public Sphere(final double radius, @NonNull final Material material, @NonNull final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.radius = radius;
    }

    @Override
    @NonNull
    public Vector getStaticNormal(@NonNull final Point point) {
        return new Vector(point.getX(), point.getY(), point.getZ()).getNormalized();
    }

    @Override
    @NonNull
    public List<Point> getStaticIntersectionPoints(@NonNull final Ray line) {
        double x0 = line.getPoint().getX(), y0 = line.getPoint().getY(), z0 = line.getPoint().getZ();
        double m = line.getDirectionVector().getX(), n = line.getDirectionVector().getY(), p = line.getDirectionVector().getZ();


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
        List<Point> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        return points;
    }

}
