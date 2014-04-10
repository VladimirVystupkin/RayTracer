package com.spbstu.raytracing.old;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class Vector3D {

    double x, y, z;

    public Vector3D(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3D(Point3D startPoint, Point3D endPoint) {
        this.x = endPoint.x - startPoint.x;
        this.y = endPoint.x - startPoint.y;
        this.z = endPoint.z - startPoint.z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize() {
        double length = length();
        x /= length;
        y /= length;
        z /= length;
    }

    public static double scalar(Vector3D a, Vector3D b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3D cross(Vector3D a, Vector3D b) {
        return new Vector3D(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
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
    public static Vector3D add(Vector3D a, Vector3D b) {
        return new Vector3D(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * c = a-b
     *
     * @param a
     * @param b
     * @return c
     */
    public static Vector3D sub(Vector3D a, Vector3D b) {
        return new Vector3D(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3D onNumber(Vector3D vector, double c) {
        return new Vector3D(vector.x * c, vector.y * c, vector.z * c);
    }


    public Point3D toPoint3D() {
        return new Point3D(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3D vector3D = (Vector3D) o;

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
}
