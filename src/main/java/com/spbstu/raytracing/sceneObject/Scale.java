package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Matrix3D;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class Scale {

    double sx, sy, sz;

    public Scale(double sx, double sy, double sz) {
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    public Matrix3D getMatrix() {
        return new Matrix3D(
                1 / sx, 0, 0, 0,
                0, 1 / sy, 0, 0,
                0, 0, 1 / sz, 0,
                0, 0, 0, 1
        );
    }
}
