package com.spbstu.raytracing.math;

import com.sun.javafx.beans.annotations.NonNull;

/**
 * 3D Point class
 * @author vva
 */
public class Point {
    final double x, y, z;

    /**
     * Constructor which makes point by 3 coordinates
     * @param x  x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public Point(final double x,final double y,final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor which makes point from vector
     * @param vector Vector to make point
     * @see  com.spbstu.raytracing.math.Vector
     */
    public Point(@NonNull final Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    /**
     * Converts point to vector
     * @return point to vector conversation result
     * @see com.spbstu.raytracing.math.Vector
     */
    @NonNull
    public Vector toVector3D() {
        return new Vector(x, y, z);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point3D = (Point) o;

        if (Double.compare(point3D.x, x) != 0) return false;
        if (Double.compare(point3D.y, y) != 0) return false;
        if (Double.compare(point3D.z, z) != 0) return false;

        return true;
    }

    /**
     * Returns distance between 2 points
     * @param point1 first point to identify distance
     * @param point2 second point to identify distance
     * @return distance between 2 points
     */
    public static double distance(Point point1, Point point2) {
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

    /**
     * Translates point by vector
     * @param point point to translate
     * @param vector translation vector
     * @return point translation result
     */
    @NonNull
    public static Point translate(@NonNull final Point point,@NonNull final Vector vector) {
        return new Point(point.x + vector.x, point.y + vector.y, point.z + vector.z);
    }

    /**
     * Point triple linear combination
     * @param ca coefficient for point a
     * @param cb coefficient for point b
     * @param cc coefficient for point c
     * @param a first point for triple linear combination
     * @param b second point for triple linear combination
     * @param c third point for triple linear combination
     * @return  point  triple linear combination result
     */
    @NonNull
    public static Point tripleLinearCombination(final double ca,final double cb,final double cc,
                                                @NonNull final Point a,@NonNull final Point b,@NonNull final Point c) {
        return new Point(ca * a.x + cb * b.x + cc * c.x, ca * a.y + cb * b.y + cc * c.y, ca * a.z + cb * b.z + cc * c.z);
    }

    /**
     * Returns point x coordinate
     * @return point x coordinate
     */
    public double getX() {
        return x;
    }


    /**
     * Returns point y coordinate
     * @return point y coordinate
     */
    public double getY() {
        return y;
    }


    /**
     * Returns point z coordinate
     * @return point z coordinate
     */
    public double getZ() {
        return z;
    }

}
