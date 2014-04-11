package com.spbstu.raytracing.math;

import com.sun.javafx.beans.annotations.NonNull;

import java.lang.Object;

/**
 * 3D vector class
 *
 * @author vva
 */
public class Vector {

    final double x, y, z;

    /**
     * Constuctor which makes vector from to coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public Vector(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Constructor  to make vector from 2 points
     *
     * @param startPoint vector start point
     * @param endPoint   vector end point
     */
    public Vector(@NonNull final Point startPoint, @NonNull final Point endPoint) {
        this.x = endPoint.x - startPoint.x;
        this.y = endPoint.y - startPoint.y;
        this.z = endPoint.z - startPoint.z;
    }

    /**
     * Returns current  vector length
     *
     * @return current  vector length
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes vector and recalculates vector length
     */
    public Vector getNormalized() {
        double length = length();
        return new Vector(x / length,
                y / length,
                z / length);
    }

    /**
     * Returns vector scalar multiplication (dot product)
     *
     * @param a first vector to scalar multiplication
     * @param b second vector to scalar multiplication
     * @return scalar multiplication result
     */
    public static double scalar(@NonNull final Vector a, @NonNull final Vector b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    /**
     * Returns vector cross multiplication (cross product)
     *
     * @param a first vector to cross multiplication
     * @param b second vector to cross multiplication
     * @return cross multiplication result
     */
    public static Vector cross(@NonNull final Vector a,@NonNull final Vector b) {
        return new Vector(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    /**
     * Returns vector x coordinate
     *
     * @return vector x coordinate
     */
    public double getX() {
        return x;
    }


    /**
     * Returns vector y coordinate
     *
     * @return vector y coordinate
     */
    public double getY() {
        return y;
    }


    /**
     * Returns vector z coordinate
     *
     * @return vector z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns vector addition
     *
     * @param a vector to add
     * @param b addition vector
     * @return new vector c = a+b
     */
    @NonNull
    public static Vector add(@NonNull final Vector a, @NonNull final Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Returns vector subtraction
     *
     * @param a vector to subtract
     * @param b subtraction vector
     * @return new vector c = a-b
     */
    @NonNull
    public static Vector sub(@NonNull final Vector a, @NonNull final Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * Returns multiple vector addition
     *
     * @param vectors collection of vectors to get addition
     * @return new addition vector
     */
    @NonNull
    public static Vector add(@NonNull final Vector... vectors) {
        double x = 0, y = 0, z = 0;
        for (Vector vector : vectors) {
            x += vector.x;
            y += vector.y;
            z += vector.z;
        }
        return new Vector(x, y, z);
    }

    /**
     * Multiplies vector on number
     *
     * @param vector vector to multiply
     * @param c      multiplication coefficient
     * @return new vector b = a*c
     */
    @NonNull
    public static Vector onNumber(@NonNull final Vector vector, final double c) {
        return new Vector(vector.x * c, vector.y * c, vector.z * c);
    }


    /**
     * Vector to point conversation
     *
     * @return vector to point conversation
     */
    @NonNull
    public Point toPoint3D() {
        return new Point(x, y, z);
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


    /**
     * Vector double linear combination
     *
     * @param ca coefficient for vector a
     * @param cb coefficient for vector b
     * @param a  first vector to double linear combination
     * @param b  second vector to double linear combination
     * @return new vector - double linear combination result
     */
    @NonNull
    public static Vector doubleLinearCombination(final double ca, final double cb,
                                                 @NonNull final Vector a, @NonNull final Vector b) {
        return new Vector(ca * a.x + cb * b.x, ca * a.y + cb * b.y, ca * a.z + cb * b.z);
    }


    /**
     * Vector triple linear combination
     *
     * @param ca coefficient for vector a
     * @param cb coefficient for vector b
     * @param cc coefficient for vector c
     * @param a  first vector to triple linear combination
     * @param b  second vector to triple linear combination
     * @param c  third vector to triple linear combination
     * @return new vector - triple linear combination result
     */
    public static Vector tripleLinearCombination(double ca, double cb, double cc, Vector a, Vector b, Vector c) {
        return new Vector(ca * a.x + cb * b.x + cc * c.x, ca * a.y + cb * b.y + cc * c.y, ca * a.z + cb * b.z + cc * c.z);
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", length=" + length() +
                '}';
    }

}
