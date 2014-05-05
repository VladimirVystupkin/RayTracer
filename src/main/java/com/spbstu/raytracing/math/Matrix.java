package com.spbstu.raytracing.math;


import org.la4j.LinearAlgebra;
import org.la4j.matrix.dense.Basic2DMatrix;


/**
 * @author vva
 */
public class Matrix {

    org.la4j.matrix.Matrix matrix;

    public static enum Axis {
        X_AXIS,
        Y_AXIS,
        Z_AXIS
    }

    public Matrix(final double[][] a) {
        this.matrix = new Basic2DMatrix(a);
    }

    public Matrix(final org.la4j.matrix.Matrix matrix) {
        this.matrix = matrix;
    }

    public Matrix(final double... values) {
        int j = 0;
        matrix = new Basic2DMatrix(new double[4][4]);
        for (int i = 0; i < values.length; i++) {
            matrix.set(j, i % 4, values[i]);
            if (i == 3 || i == 7 || i == 11) {
                j++;
            }
        }
    }


    public static Matrix getRotationMatrix(final double degrees, final Axis axis) {
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


    public static Matrix getTranslationMatrix(final double xTranslation, final double yTranslation, final double zTranslation) {
        return new Matrix(
                1, 0, 0, xTranslation,
                0, 1, 0, yTranslation,
                0, 0, 1, zTranslation,
                0, 0, 0, 1
        );
    }


    public static Matrix getIdentity() {
        return new Matrix(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }


    public static Matrix multiply(final Matrix a, final Matrix b) {
        return new Matrix(a.matrix.multiply(b.matrix));
    }


    public static Matrix multiply(final Matrix... matrixes) {
        Matrix matrix = Matrix.getIdentity();
        for (Matrix currentMatrix : matrixes) {
            matrix = Matrix.multiply(matrix, currentMatrix);
        }
        return matrix;
    }


    public static Matrix invert(final Matrix a) {
        org.la4j.matrix.Matrix inverted = a.matrix.copy();
        return new Matrix(inverted.withInverter(LinearAlgebra.InverterFactory.SMART).inverse());
    }


    public static Vector multiply(final Matrix matrix, final Vector vector) {
        double newX = matrix.matrix.get(0, 0) * vector.x + matrix.matrix.get(0, 1) * vector.y + matrix.matrix.get(0, 2) * vector.z + matrix.matrix.get(0, 3);
        double newY = matrix.matrix.get(1, 0) * vector.x + matrix.matrix.get(1, 1) * vector.y + matrix.matrix.get(1, 2) * vector.z + matrix.matrix.get(1, 3);
        double newZ = matrix.matrix.get(2, 0) * vector.x + matrix.matrix.get(2, 1) * vector.y + matrix.matrix.get(2, 2) * vector.z + matrix.matrix.get(2, 3);
        return new Vector(newX, newY, newZ);
    }


    public static Point multiply(final Matrix matrix, final Point point) {
        double newX = matrix.matrix.get(0, 0) * point.x + matrix.matrix.get(0, 1) * point.y + matrix.matrix.get(0, 2) * point.z + matrix.matrix.get(0, 3);
        double newY = matrix.matrix.get(1, 0) * point.x + matrix.matrix.get(1, 1) * point.y + matrix.matrix.get(1, 2) * point.z + matrix.matrix.get(1, 3);
        double newZ = matrix.matrix.get(2, 0) * point.x + matrix.matrix.get(2, 1) * point.y + matrix.matrix.get(2, 2) * point.z + matrix.matrix.get(2, 3);
        return new Point(newX, newY, newZ);
    }

}
