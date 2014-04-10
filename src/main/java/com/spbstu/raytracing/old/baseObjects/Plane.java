package com.spbstu.raytracing.old.baseObjects;

import com.spbstu.raytracing.old.Line;
import com.spbstu.raytracing.old.Point3D;
import com.spbstu.raytracing.old.Vector3D;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public  class Plane extends BaseObject {
    /**
     * a*x+b*y+c*z+d=0
     */
    double a, b, c, d;
    int color;
    Vector3D normal;

    public Plane(double a, double b, double c, double d, int color) {
        if (a == 0 && b == 0 && c == 0) {
            throw new IllegalArgumentException("a^2+b^2+c^2>0");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.color = color;
        normal = new Vector3D(a, b, c);
        normal.normalize();
    }


    public Plane(Point3D point1, Point3D point2, Point3D point3, int color) {
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
        this.color = color;
        normal = new Vector3D(a, b, c);
        normal.normalize();

    }

    public Vector3D getNormal(Point3D point) {
        return normal;
    }


    /**
     * http://fxdx.ru/page/vzaimnoe-raspolozhenie-prjamoj-i-ploskosti-v-prostranstve-vzaimnoe-raspolozhenie-dvuh-ploskostej
     *
     * @param
     * @return
     */
//    @Override
//    public Point3D getCrossPoint(Line line) {
//        if (Vector3D.scalar(line.getDirectionVector(), normal) == 0) {
//            return null;
//        }
//        double x0 = line.getPoint().getX(), y0 = line.getPoint().getY(), z0 = line.getPoint().getZ();
//        double m = line.getDirectionVector().getX(), n = line.getDirectionVector().getY(), p = line.getDirectionVector().getZ();
//        double t = -(a * x0 + b * y0 + c * z0 + d) / (a * m + b * n + c * p);
//        return new Point3D(x0 + m * t, y0 + n * t, z0 + p * t);
//    }

    public int getColor(Point3D point) {
        return color;
    }
}
