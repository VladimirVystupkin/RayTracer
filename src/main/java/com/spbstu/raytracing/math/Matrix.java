package com.spbstu.raytracing.math;

import com.sun.javafx.beans.annotations.NonNull;
import org.la4j.LinearAlgebra;
import org.la4j.matrix.dense.Basic2DMatrix;


/**
 * Matrix class which provides needed for ray tracing functionality
 * Uses {@link org.la4j.matrix.Matrix}
 * @see org.la4j.matrix.Matrix
 * @author vva
 */
public class Matrix {

    org.la4j.matrix.Matrix matrix;

    /**
     * 3D axis enumeration
     */
    public static enum Axis {
        X_AXIS,
        Y_AXIS,
        Z_AXIS
    }

    /**
     * Constructor which makes matrix from two-dimensional array
     * @param a two-dimensional array for making matrix
     */
    public Matrix(@NonNull final double[][] a) {
        this.matrix = new Basic2DMatrix(a);
    }

    /**
     * Constructor which makes matrix from  {@link  org.la4j.matrix.Matrix}
     * @param matrix org.la4j.matrix.Matrix matrix
     */
    public Matrix(@NonNull final org.la4j.matrix.Matrix matrix) {
        this.matrix = matrix;
    }

    /**
     * Constructor which makes matrix from value array filling by rows
     * @param values matrix value array by rows
     */
    public Matrix(@NonNull final double... values) {
        int j = 0;
        matrix = new Basic2DMatrix(new double[4][4]);
        for (int i = 0; i < values.length; i++) {
            matrix.set(j, i % 4, values[i]);
            if (i == 3 || i == 7 || i == 11) {
                j++;
            }
        }
    }

    /**
     * Returns rotation matrix by axis
     * @param degrees rotation angle in degrees
     * @param axis rotation axis {@link com.spbstu.raytracing.math.Matrix.Axis}
     * @return rotation matrix by axis
     * @throws  java.lang.IllegalArgumentException if no axis selected
     */
    @NonNull
    public static Matrix getRotationMatrix(final double degrees,@NonNull final Axis axis) {
        double angle = Math.PI / 180 * degrees;
        switch (axis) {
            case X_AXIS:
                return new Matrix(
                        1, 0, 0, 0,
                        0, Math.cos(angle), Math.sin(angle), 0,
                        0, -Math.sin(angle), Math.cos(angle), 0,
                        0, 0, 0, 1
                );
            case Y_AXIS:
                return new Matrix(
                        Math.cos(angle), 0, Math.sin(angle), 0,
                        0, 1, 0, 0,
                        -Math.sin(angle), 0, Math.cos(angle), 0,
                        0, 0, 0, 1
                );
            case Z_AXIS:
                return new Matrix(
                        Math.cos(angle), Math.sin(angle), 0, 0,
                        -Math.sin(angle), Math.cos(angle), 0, 0,
                        0, 0, 1, 0,
                        0, 0, 0, 1
                );
            default:
                throw new IllegalArgumentException("no axis");
        }
    }


    /**
     * Returns translation matrix
     * @param xTranslation translation by x axis
     * @param yTranslation translation by x axis
     * @param zTranslation translation by x axis
     * @return translation matrix
     * @see com.spbstu.raytracing.math.Matrix.Axis
     */
    @NonNull
    public static Matrix getTranslationMatrix(final double xTranslation,final double yTranslation,final double zTranslation) {
        return new Matrix(
                1, 0, 0, xTranslation,
                0, 1, 0, yTranslation,
                0, 0, 1, zTranslation,
                0, 0, 0, 1
        );
    }

    /**
     * Return identity matrix
     * @return identity matrix
     */
    @NonNull
    public static Matrix getIdentity() {
        return new Matrix(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    /**
     * Multiplies two matrix
     * @param a left matrix to multiply
     * @param b right  matrix to multiply
     * @return new matrix a*b
     */
    @NonNull
    public static Matrix multiply(@NonNull final Matrix a,@NonNull final  Matrix b) {
        return new Matrix(a.matrix.multiply(b.matrix));
    }

    /**
     * Multiplies array of matrix
     * @param matrixes matrixes to multiply
     * @return new matrix -multiplication result
     */
    @NonNull
    public static Matrix multiply(@NonNull final Matrix... matrixes) {
        Matrix matrix = Matrix.getIdentity();
        for (Matrix currentMatrix : matrixes) {
            matrix = Matrix.multiply(matrix, currentMatrix);
        }
        return matrix;
    }

    /**
     * Inverts matrix
     * @param a matrix to inver
     * @return new inverted matrix
     */
    @NonNull
    public static Matrix invert(@NonNull final Matrix a) {
        org.la4j.matrix.Matrix inverted = a.matrix.copy();
        return new Matrix(inverted.withInverter(LinearAlgebra.InverterFactory.SMART).inverse());
    }


    /**
     * Multiplies matrix on vector
     * @param matrix matrix to multiply
     * @param vector vector to multiply
     * @return new vector -result of matrix*vector
     */
    @NonNull
    public static Vector multiply(@NonNull final Matrix matrix,@NonNull final  Vector vector) {
        double newX = matrix.matrix.get(0, 0) * vector.x + matrix.matrix.get(0, 1) * vector.y + matrix.matrix.get(0, 2) * vector.z + matrix.matrix.get(0, 3);
        double newY = matrix.matrix.get(1, 0) * vector.x + matrix.matrix.get(1, 1) * vector.y + matrix.matrix.get(1, 2) * vector.z + matrix.matrix.get(1, 3);
        double newZ = matrix.matrix.get(2, 0) * vector.x + matrix.matrix.get(2, 1) * vector.y + matrix.matrix.get(2, 2) * vector.z + matrix.matrix.get(2, 3);
        return new Vector(newX, newY, newZ);
    }

    /**
     * Multiplies matrix on point interpreting point as vector
     * @param matrix matrix to multiply
     * @param point point to multiply interpreting point as vector
     * @return new vector -result of matrix*point
     */
    @NonNull
    public static Point multiply(@NonNull final Matrix matrix,@NonNull final Point point) {
        double newX = matrix.matrix.get(0, 0) * point.x + matrix.matrix.get(0, 1) * point.y + matrix.matrix.get(0, 2) * point.z + matrix.matrix.get(0, 3);
        double newY = matrix.matrix.get(1, 0) * point.x + matrix.matrix.get(1, 1) * point.y + matrix.matrix.get(1, 2) * point.z + matrix.matrix.get(1, 3);
        double newZ = matrix.matrix.get(2, 0) * point.x + matrix.matrix.get(2, 1) * point.y + matrix.matrix.get(2, 2) * point.z + matrix.matrix.get(2, 3);
        return new Point(newX, newY, newZ);
    }

    /**
     * Multiplies matrix on point extension {@link com.spbstu.raytracing.math.PointExt} interpreting point as vector and
     * saving inner point info
     * @param matrix matrix to multiply
     * @param point point to multiply interpreting point as vector
     * @return new vector -result of matrix*point
     */
    @NonNull
    public static PointExt multiply(@NonNull final Matrix matrix,@NonNull final  PointExt point) {
        double newX = matrix.matrix.get(0, 0) * point.x + matrix.matrix.get(0, 1) * point.y + matrix.matrix.get(0, 2) * point.z + matrix.matrix.get(0, 3);
        double newY = matrix.matrix.get(1, 0) * point.x + matrix.matrix.get(1, 1) * point.y + matrix.matrix.get(1, 2) * point.z + matrix.matrix.get(1, 3);
        double newZ = matrix.matrix.get(2, 0) * point.x + matrix.matrix.get(2, 1) * point.y + matrix.matrix.get(2, 2) * point.z + matrix.matrix.get(2, 3);
        return new PointExt(newX, newY, newZ, point.getInfo());
    }
}
