package com.spbstu.raytracing.old;

import org.la4j.LinearAlgebra;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;


/**
 * @author vva
 * @date 12.03.14
 * @description
 */
public class Matrix3D {

    Matrix matrix;

    public static enum Axis {
        X_AXIS,
        Y_AXIS,
        Z_AXIS
    }

    public Matrix3D(double[][] a) {
        this.matrix = new Basic2DMatrix(a);
    }

    public Matrix3D(Matrix matrix) {
        this.matrix = matrix;
    }

    public Matrix3D(double... values) {
        int j = 0;
        matrix = new Basic2DMatrix(new double[3][3]);
        for (int i = 0; i < values.length; i++) {
            matrix.set(i, j, values[i]);
            if (i == 2 || i == 4) {
                j++;
            }
        }
    }

    public static Matrix3D getRotationMatrix(double degrees, Axis axis) {
        switch (axis) {
            case X_AXIS:
                return new Matrix3D(
                        1, 0, 0, 0,
                        0, Math.cos(degrees), Math.sin(degrees), 0,
                        0, -Math.sin(degrees), Math.cos(degrees), 0,
                        0, 0, 0, 1
                );
            case Y_AXIS:
                return new Matrix3D(
                        Math.cos(degrees), 0, Math.sin(degrees), 0,
                        0, 1, 0, 0,
                        -Math.sin(degrees), 0, Math.cos(degrees), 0,
                        0, 0, 0, 1
                );
            case Z_AXIS:
                return new Matrix3D(
                        Math.cos(degrees), Math.sin(degrees), 0, 0,
                        -Math.sin(degrees), Math.cos(degrees), 0, 0,
                        0, 0, 1, 0,
                        0, 0, 0, 1
                );
            default:
                throw new IllegalArgumentException("no axis");
        }
    }


    public static Matrix3D getRotationMatrix(double xTranslation, double yTranslation, double zTranslation) {
        return new Matrix3D(
                1, 0, 0, xTranslation,
                0, 1, 0, yTranslation,
                0, 0, 1, zTranslation,
                0, 0, 0, 1
        );
    }

    public static Matrix3D getMatrixIdentity() {
        return new Matrix3D(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix3D multiply(Matrix3D a, Matrix3D b) {
        return new Matrix3D(a.matrix.multiply(b.matrix));
    }

    public static Matrix3D invert(Matrix3D a) {
        return new Matrix3D(a.matrix.withInverter(LinearAlgebra.InverterFactory.GAUSS_JORDAN).inverse());
    }


    @Override
    public String toString() {
        return matrix.toString();
    }

    public Vector3D multiply(Vector3D vector) {
        double vectorEntry[] = {vector.x, vector.y, vector.z, 0};
        Vector vectorResult = matrix.multiply(new BasicVector(vectorEntry));
        return new Vector3D(vectorResult.get(0), vectorResult.get(1), vectorResult.get(2));
    }


}
