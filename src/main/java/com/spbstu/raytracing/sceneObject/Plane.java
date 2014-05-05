package com.spbstu.raytracing.sceneObject;


import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class Plane extends SceneObject {

    final double a, b, c, d;
    final Vector normal;

    public Plane(final double a, final double b, final double c, final double d,
                 final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(material, attributesMap);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        normal = new Vector(a, b, c).getNormalized();
    }

    public Plane(final Point point, final Vector normal, final Material material, final Map<Attributes.AttributeName, Attribute> attributes) {
        super(material, attributes);
        a = normal.getX();
        b = normal.getY();
        c = normal.getZ();
        d = -Vector.scalar(normal, point.toVector3D());
        this.normal = normal;
    }

    public Plane(final Point point1, final Point point2, final Point point3,
                 final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
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

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        if (Vector.scalar(ray.getDirectionVector(), normal) == 0) {
            return new ArrayList<>();
        }
        double x0 = ray.getPoint().getX(), y0 = ray.getPoint().getY(), z0 = ray.getPoint().getZ();
        double m = ray.getDirectionVector().getX(), n = ray.getDirectionVector().getY(), p = ray.getDirectionVector().getZ();
        double t = -(a * x0 + b * y0 + c * z0 + d) / (a * m + b * n + c * p);
        List<IntersectionInfo> intersectionInfoList = new ArrayList<IntersectionInfo>();
        intersectionInfoList.add(new IntersectionInfo(new Point(x0 + m * t, y0 + n * t, z0 + p * t), normal, material));
        return intersectionInfoList;
    }


    public static Plane fromMap(final HashMap hashMap, final Material material, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        HashMap positionAttributes = (HashMap) hashMap.get("position");
        double x = positionAttributes.containsKey("x") ? Double.parseDouble(positionAttributes.get("x").toString()) : 0;
        double y = positionAttributes.containsKey("y") ? Double.parseDouble(positionAttributes.get("y").toString()) : 0;
        double z = positionAttributes.containsKey("z") ? Double.parseDouble(positionAttributes.get("z").toString()) : 0;
        Point pos = new Point(x, y, z);
        HashMap normalAttributes = (HashMap) hashMap.get("normal");
        double nx = normalAttributes.containsKey("x") ? Double.parseDouble(normalAttributes.get("x").toString()) : 0;
        double ny = normalAttributes.containsKey("y") ? Double.parseDouble(normalAttributes.get("y").toString()) : 0;
        double nz = normalAttributes.containsKey("z") ? Double.parseDouble(normalAttributes.get("z").toString()) : 0;
        Vector normal = new Vector(nx, ny, nz);
        return new Plane(pos, normal, material, attributesMap);

    }
}
