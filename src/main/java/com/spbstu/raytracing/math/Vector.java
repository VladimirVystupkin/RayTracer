package com.spbstu.raytracing.math;

import java.lang.Object;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class Vector {

    double x, y, z;
    double length;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        length = calcLength();
    }

    private double calcLength() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void update(Point3D startPoint, Point3D endPoint) {
        this.x = endPoint.x - startPoint.x;
        this.y = endPoint.y - startPoint.y;
        this.z = endPoint.z - startPoint.z;
        length = calcLength();
    }

    public Vector(Point3D startPoint, Point3D endPoint) {
        this.x = endPoint.x - startPoint.x;
        this.y = endPoint.y - startPoint.y;
        this.z = endPoint.z - startPoint.z;
        length = calcLength();
    }

    public double length() {
        return length;
    }

    public void normalize() {
        x /= length;
        y /= length;
        z /= length;
        length = calcLength();
    }

    public static double scalar(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector cross(Vector a, Vector b) {
        return new Vector(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * c = a+b
     *
     * @param a
     * @param b
     * @return c
     */
    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * c = a-b
     *
     * @param a
     * @param b
     * @return c
     */
    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector add(Vector... vectors) {
        double x = 0, y = 0, z = 0;
        for (Vector vector : vectors) {
            x += vector.x;
            y += vector.y;
            z += vector.z;
        }
        return new Vector(x, y, z);
    }

    public static Vector onNumber(Vector vector, double c) {
        return new Vector(vector.x * c, vector.y * c, vector.z * c);
    }


    public Point3D toPoint3D() {
        return new Point3D(x, y, z);
    }

    public Point3D toPoint3D(Object info) {
        return new PointExt(x, y, z, info);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector3D = (Vector) o;

        if (Double.compare(vector3D.x, x) != 0) return false;
        if (Double.compare(vector3D.y, y) != 0) return false;
        if (Double.compare(vector3D.z, z) != 0) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    public static Vector doubleLinearCombination(double ca, double cb, Vector a, Vector b) {
        return new Vector(ca * a.x + cb * b.x, ca * a.y + cb * b.y, ca * a.z + cb * b.z);
    }

    public static Vector tripleLinearCombination(double ca, double cb, double cc, Vector a, Vector b, Vector c) {
        return new Vector(ca * a.x + cb * b.x + cc * c.x, ca * a.y + cb * b.y + cc * c.y, ca * a.z + cb * b.z + cc * c.z);
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", length=" + length +
                '}';
    }
}
