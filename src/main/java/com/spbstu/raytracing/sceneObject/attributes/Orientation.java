package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;


/**
 * @autor vva
 */
public class Orientation implements Attribute {

    double h, p, r;

    public Orientation(final double h, final double p, final double r) {
        this.h = h;
        this.p = p;
        this.r = r;
    }

    @Override

    public Matrix getMatrix() {
        Matrix matrix = Matrix.getIdentity();
        Matrix rotationH = Matrix.getRotationMatrix(-h, Matrix.Axis.Z_AXIS);
        Matrix rotationP = Matrix.getRotationMatrix(-p, Matrix.Axis.X_AXIS);
        Matrix rotationR = Matrix.getRotationMatrix(r, Matrix.Axis.Y_AXIS);
        return Matrix.multiply(rotationH,rotationP,rotationR);
    }
}
