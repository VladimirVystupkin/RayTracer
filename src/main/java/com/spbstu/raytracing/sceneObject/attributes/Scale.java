package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;


/**
 * @author vva
 */
public class Scale implements Attribute {

    double sx, sy, sz;

    public Scale(final double sx, final double sy, final double sz) {
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    @Override

    public Matrix getMatrix() {
        return new Matrix(
                1 / sx, 0, 0, 0,
                0, 1 / sy, 0, 0,
                0, 0, 1 / sz, 0,
                0, 0, 0, 1
        );
    }
}
