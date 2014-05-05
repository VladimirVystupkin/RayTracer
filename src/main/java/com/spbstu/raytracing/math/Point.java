package com.spbstu.raytracing.math;



/**
 * @author vva
 */
public class Point {
    final double x, y, z;


    public Point(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(final Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }


    public Vector toVector3D() {
        return new Vector(x, y, z);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point3D = (Point) o;

        if (Double.compare(point3D.x, x) != 0) return false;
        if (Double.compare(point3D.y, y) != 0) return false;
        if (Double.compare(point3D.z, z) != 0) return false;

        return true;
    }

    public static double distance(final Point point1, final Point point2) {
        return new Vector(point1, point2).length();
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


    public static Point translate(final Point point, final Vector vector) {
        return new Point(point.x + vector.x, point.y + vector.y, point.z + vector.z);
    }


    public static Point tripleLinearCombination(final double ca, final double cb, final double cc,
                                                final Point a, final Point b, final Point c) {
        return new Point(ca * a.x + cb * b.x + cc * c.x, ca * a.y + cb * b.y + cc * c.y, ca * a.z + cb * b.z + cc * c.z);
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

}
