package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Attribute defining object 3D orientation
 *
 * @autor vva
 * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
 */
public class Orientation implements Attribute {

    double h, p, r;

    /**
     * Constructor defining orientation by angle
     *
     * @param h angle in degrees to rotate from x axis
     * @param p angle in degrees to rotate from y axis
     * @param r angle in degrees to rotate from z axis
     */
    public Orientation(final double h, final double p, final double r) {
        this.h = h;
        this.p = p;
        this.r = r;
    }

    @Override
    @NonNull
    public Matrix getMatrix() {
        Matrix matrix = Matrix.getIdentity();
        Matrix rotationX = Matrix.getRotationMatrix(h, Matrix.Axis.X_AXIS);
        Matrix rotationY = Matrix.getRotationMatrix(p, Matrix.Axis.Y_AXIS);
        Matrix rotationZ = Matrix.getRotationMatrix(r, Matrix.Axis.Z_AXIS);
        return Matrix.multiply(Matrix.multiply(Matrix.multiply(matrix, rotationX), rotationY), rotationZ);
    }
}
