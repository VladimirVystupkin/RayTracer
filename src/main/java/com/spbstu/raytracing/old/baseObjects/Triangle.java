package com.spbstu.raytracing.old.baseObjects;

import com.spbstu.raytracing.old.Line;
import com.spbstu.raytracing.old.Point3D;
import com.spbstu.raytracing.old.Vector3D;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class Triangle extends BaseObject {
    Point3D a, b, c;
    Plane trianglePlane;

    public Triangle(Point3D a, Point3D b, Point3D c, int color) {
        this.a = a;
        this.b = b;
        this.c = c;
        trianglePlane = new Plane(b, a, c, color);
    }


    
    public Vector3D getNormal(Point3D point) {
        return trianglePlane.getNormal(point);
    }

    
    public Point3D getCrossPoint(Line line) {
        Point3D p =null; //trianglePlane.getCrossPoint(line);
        if (p == null) {
            return null;
        }
        double pl1 = (a.getX() - p.getX()) * (b.getY() - a.getY()) - (b.getX() - a.getX()) * (a.getY() - p.getY());
        double pl2 = (b.getX() - p.getX()) * (c.getY() - b.getY()) - (c.getX() - b.getX()) * (b.getY() - p.getY());
        double pl3 = (c.getX() - p.getX()) * (a.getY() - c.getY()) - (a.getX() - c.getX()) * (c.getY() - p.getY());
        if ((pl1 >= 0 && pl2 >= 0 && pl3 >= 0) || (pl1 <= 0 && pl2 <= 0 && pl3 <= 0)) {
            return p;
        }
        return null;
    }

    
    public int getColor(Point3D point) {
        return trianglePlane.color;
    }

}
