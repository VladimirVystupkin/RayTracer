package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Matrix3D;

/**
 * @autor vystupkin
 * @date 01.04.14
 * @description
 */
public class Orientation {

    double h, p, r;

    public Orientation(double h, double p, double r) {
        this.h = h;
        this.p = p;
        this.r = r;
    }


    public Matrix3D getMatrix() {
        Matrix3D matrix = Matrix3D.getIdentity();
        Matrix3D rotationX = Matrix3D.getRotationMatrix(h, Matrix3D.Axis.X_AXIS);
        Matrix3D rotationY = Matrix3D.getRotationMatrix(p, Matrix3D.Axis.Y_AXIS);
        Matrix3D rotationZ = Matrix3D.getRotationMatrix(r, Matrix3D.Axis.Z_AXIS);
        return Matrix3D.multiply(Matrix3D.multiply(Matrix3D.multiply(matrix, rotationX), rotationY), rotationZ);
    }
}
