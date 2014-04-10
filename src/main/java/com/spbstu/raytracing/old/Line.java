package com.spbstu.raytracing.old;

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
    Vector3D directionVector;

    public Line(Point3D point1, Point3D point2) {
        double a = point1.x - point2.x;
        double b = point1.y - point2.y;
        double c = point1.z - point2.z;
        point = new Point3D(point1.x, point1.y, point1.z);
        directionVector = new Vector3D(a, b, c);
        directionVector.normalize();
    }


    public Line(Point3D point, Vector3D directionVector) {
        this.point = point;
        this.directionVector = directionVector;
    }

    public Point3D getPoint() {
        return point;
    }

    public Vector3D getDirectionVector() {
        return directionVector;
    }
}
