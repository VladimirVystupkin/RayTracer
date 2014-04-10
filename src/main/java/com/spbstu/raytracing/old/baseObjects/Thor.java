package com.spbstu.raytracing.old.baseObjects;

import com.spbstu.raytracing.old.Line;
import com.spbstu.raytracing.old.Point3D;
import com.spbstu.raytracing.old.Solvers;
import com.spbstu.raytracing.old.Vector3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author vva
 * @date 11.03.14
 * @description
 */
public class Thor extends BaseObject {

    /**
     * (x^2+y^2+z^2+R^2-r^2)^2-4*R^2*(x^2+y^2)=0
     */
    double innerR, outerR;
    Point3D center;
    int color;

    public Thor(Point3D center, double innerR, double outerR, int color) {
        this.innerR = innerR;
        this.outerR = outerR;
        this.center = center;
        this.color = color;
    }

    
    public Vector3D getNormal(Point3D point) {
        double x = point.getX() - center.getX();
        double y = point.getY() - center.getY();
        double z = point.getZ() - center.getZ();
        double normalX = 2 * (x * x + y * y + z * z + outerR * outerR - innerR * innerR) * 2 * x - 8 * outerR * outerR * x;
        double normalY = 2 * (x * x + y * y + z * z + outerR * outerR - innerR * innerR) * 2 * y - 8 * outerR * outerR * y;
        double normalZ = 2 * (x * x + y * y + z * z + outerR * outerR - innerR * innerR) * 2 * z;
        Vector3D normal = new Vector3D(normalX, normalY, normalZ);
        normal.normalize();
        return normal;
    }

    
    public Point3D getCrossPoint(Line line) {
        double x0 = line.getPoint().getX() - center.getX(), y0 = line.getPoint().getY() - center.getY(), z0 = line.getPoint().getZ() - center.getZ();
        // double x0 = line.getPoint().getX(), y0 = line.getPoint().getY(), z0 = line.getPoint().getZ();
        double m = line.getDirectionVector().getX(), n = line.getDirectionVector().getY(), p = line.getDirectionVector().getZ();

//        double a1 = (m * m + n * n + p * p);
//        double b1 = 2 * (x0 * m + y0 * n + z0 * p);
//        double c1 = x0 * x0 + y0 * y0 + z0 * z0 + outerR * outerR - innerR * innerR;
//
//        double u = 4 * outerR * outerR * (m * m + n * n);
//        double v = 8 * outerR * outerR * (x0 * m + y0 * n);
//        double w = 4 * outerR * outerR * (x0 * x0 + y0 * y0);
//
//        double a = a1 * a1;
//        double b = 2 * a1 * b1;
//        double c = b1 * b1 + 2 * a1 * c1 - u;
//        double d = 2 * b1 * c1 - v;
//        double e = c1 * c1 - w;
//        for (int tt = 0; tt < 100; tt++) {
//            Point3D point = new Point3D(x0 + m * tt, y0 + n * tt, z0 + p*tt);
//            double x = point.getX();
//            double y = point.getY();
//            double z = point.getZ();
//
//            double original = (Math.pow((x * x + y * y + z * z + outerR * outerR - innerR * innerR), 2) - 4 * outerR * outerR * (x * x + y * y));
//            double eExp = (Math.pow((x0 * x0 + y0 * y0 + z0 * z0 + outerR * outerR - innerR * innerR), 2) - 4 * outerR * outerR * (x0 * x0 + y0 * y0));
//            System.out.println("eExpDif = " + (eExp - e));
//            double dExp = 4 * (x0 * x0 + y0 * y0 + z0 * z0 + outerR * outerR - innerR * innerR) * (x0 * m + y0 * n + z0 * p) - 8 *
//                    outerR * outerR * (x0 * m + y0 * n);
//
//            System.out.println("dExpDif = " + (dExp - d));
//            double cExp = 0.5 * (8 * Math.pow((x0 * m + y0 * n + z0 * p), 2) + 4 * (x0 * x0 + y0 * y0 + z0 * z0 + outerR * outerR - innerR * innerR) * (m * m + n * n + p * p) - 8 * outerR * outerR * (m * m + n * n));
//            System.out.println("cExpDif = " + (cExp - c));
//            double bExp = 4 * ((m * m + n * n + p * p) * (x0 * m + y0 * n + z0 * p));
//            double aExp = (m * m + n * n + p * p) * (m * m + n * n + p * p);
//            double expRes = aExp * Math.pow(tt, 4) + bExp * Math.pow(tt, 3) + cExp * Math.pow(tt, 2) + dExp * tt + eExp;
//            System.out.println("diff = " + (original - expRes));
//            //double result =  (a * Math.pow(tt, 4) + b * Math.pow(tt, 3) + c * Math.pow(tt, 2) + d * tt + e);
//            //System.out.println("diff = " +(original- result));
//        }
//        System.out.println("e = " + e);

        double a1 = (m * m + n * n + p * p);
        double b1 = 2 * (x0 * m + y0 * n + z0 * p);
        double c1 = x0 * x0 + y0 * y0 + z0 * z0 + outerR * outerR - innerR * innerR;

        double a2 = a1 * a1;//x^4 - left
        double b2 = 2 * a1 * b1;//x^3-left
        double c2 = (2 * a1 * c1 + b1 * b1);//x^2-left
        double d2 = 2 * b1 * c1;//x^1- left
        double e2 = c1 * c1;//x^0-left

        double c3 = 4 * outerR * outerR * (m * m + n * n);//x^2 -right
        double d3 = 8 * outerR * outerR * (m * x0 + n * y0);//x^1-right
        double e3 = 4 * outerR * outerR * (x0 * x0 + y0 * y0);//x^0-right

        double a = a2;
        double b = b2;
        double c = c2 - c3;
        double d = d2 - d3;
        double e = e2 - e3;
        double t[] = Solvers.solveQuartic(a, b, c, d, e);
//        for(int tt =0; tt < 100;tt++){
//            Point3D point = new Point3D(x0 + m * tt, y0 + n * tt, z0 +p*tt);
//            double x = point.getX();// - center.getX();
//            double y = point.getY();// - center.getY();
//            double z = point.getZ();// - center.getZ();
//            System.out.println("diff = " +( (a * Math.pow(tt, 4) + b * Math.pow(tt, 3) + c * Math.pow(tt, 2) + d * tt + e)-(Math.pow((x * x + y * y + z * z + outerR * outerR - innerR * innerR), 2) - 4 * outerR * outerR * (x * x + y * y))));
//        }

        if (t == null) {
            return null;
        }
        List<Point3D> crossPoints = new ArrayList<Point3D>();
        for (int i = 0; i < t.length; i++) {
            Point3D point = new Point3D(x0 + m * t[i], y0 + n * t[i], z0 + p * t[i]);
            crossPoints.add(point);
            double x = point.getX() - center.getX();
            double y = point.getY() - center.getY();
            double z = point.getZ() - center.getZ();
            // System.out.println("diff[T] = " + (a * Math.pow(t[i], 4) + b * Math.pow(t[i], 3) + c * Math.pow(t[i], 2) + d * t[i] + e));
            // System.out.println("diff = " + (Math.pow((x * x + y * y + z * z + outerR * outerR - innerR * innerR), 2) - 4 * outerR * outerR * (x * x + y * y)));
        }
        Collections.sort(crossPoints, new Comparator<Point3D>() {
            
            public int compare(Point3D o1, Point3D o2) {
                return Double.compare(o1.getZ(), o2.getZ());
            }
        });
        for (Point3D crossPoint : crossPoints) {
            if (crossPoint.getZ() >= 0) {
                return crossPoint;
            }
        }
        return null;
    }

    
    public int getColor(Point3D point) {
        return color;
    }
}
