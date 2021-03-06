package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.util.*;
import java.util.List;

/**
 * @author vva
 */
public class Torus extends SceneObject {


    final double tubeRadius, radius;

    final BoundingSphere boundingSphere;

    public Torus(final double tubeRadius, final double radius, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.tubeRadius = tubeRadius;
        this.radius = radius;
        this.boundingSphere = new BoundingSphere(tubeRadius + radius + 1);
    }


    Vector getNormal(final Point point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double normalX = 2 * (x * x + y * y + z * z + radius * radius - tubeRadius * tubeRadius) * 2 * x - 8 * radius * radius * x;
        double normalY = 2 * (x * x + y * y + z * z + radius * radius - tubeRadius * tubeRadius) * 2 * y - 8 * radius * radius * y;
        double normalZ = 2 * (x * x + y * y + z * z + radius * radius - tubeRadius * tubeRadius) * 2 * z;
        return new Vector(normalX, normalY, normalZ).getNormalized();
    }


    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        if (!boundingSphere.hasIntersection(ray)) {
            return new ArrayList<>();
        }
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double a1 = (m * m + n * n + p * p);
        double b1 = 2 * (x0 * m + y0 * n + z0 * p);
        double c1 = x0 * x0 + y0 * y0 + z0 * z0 + radius * radius - tubeRadius * tubeRadius;

        double a2 = a1 * a1;
        double b2 = 2 * a1 * b1;
        double c2 = (2 * a1 * c1 + b1 * b1);
        double d2 = 2 * b1 * c1;
        double e2 = c1 * c1;

        double c3 = 4 * radius * radius * (m * m + n * n);
        double d3 = 8 * radius * radius * (m * x0 + n * y0);
        double e3 = 4 * radius * radius * (x0 * x0 + y0 * y0);

        double a = a2;
        double b = b2;
        double c = c2 - c3;
        double d = d2 - d3;
        double e = e2 - e3;
        double t[] = Solvers.solveQuartic(a, b, c, d, e);

        if (t == null) {
            return new ArrayList<>();
        }
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        for (int i = 0; i < t.length; i++) {
            Point point = new Point(x0 + m * t[i], y0 + n * t[i], z0 + p * t[i]);
            intersectionInfoList.add(new IntersectionInfo(point, getNormal(point), material));
        }
        return intersectionInfoList;
    }


    public static Torus fromMap(final HashMap hashMap, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        return new Torus(Double.parseDouble(hashMap.get("tube_radius").toString()), Double.parseDouble(hashMap.get("radius").toString()), material, attributesMap);
    }
}
