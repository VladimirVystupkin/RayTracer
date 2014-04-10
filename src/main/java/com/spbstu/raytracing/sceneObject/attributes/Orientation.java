package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;

/**
 * @autor vystupkin
 * @date 01.04.14
 * @description
 */
public class Orientation  implements Attribute{

    double h, p, r;

    public Orientation(double h, double p, double r) {
        this.h = h;
        this.p = p;
        this.r = r;
    }

    @Override
    public Matrix getMatrix() {
        Matrix matrix = Matrix.getIdentity();
        Matrix rotationX = Matrix.getRotationMatrix(h, Matrix.Axis.X_AXIS);
        Matrix rotationY = Matrix.getRotationMatrix(p, Matrix.Axis.Y_AXIS);
        Matrix rotationZ = Matrix.getRotationMatrix(r, Matrix.Axis.Z_AXIS);
        return Matrix.multiply(Matrix.multiply(Matrix.multiply(matrix, rotationX), rotationY), rotationZ);
    }
}
