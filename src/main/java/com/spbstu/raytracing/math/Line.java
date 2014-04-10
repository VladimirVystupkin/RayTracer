package com.spbstu.raytracing.math;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class Line {
    /**
     * (x-point.x)/a = (y-point.y)/b = (z-point.z)
     */
   Point3D point;
    Vector directionVector;

    public Line(Point3D point1, Point3D point2) {
        double a = point1.x - point2.x;
        double b = point1.y - point2.y;
        double c = point1.z - point2.z;
        point = new Point3D(point1.x, point1.y, point1.z);
        directionVector = new Vector(a, b, c);
        directionVector.normalize();
    }


    public Line(Point3D point, Vector directionVector) {
        this.point = point;
        this.directionVector = directionVector;
        this.directionVector.normalize();
    }

    public Point3D getPoint() {
        return point;
    }

    public Vector getDirectionVector() {
        return directionVector;
    }
}
