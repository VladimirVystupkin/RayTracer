package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Matrix3D;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class Translation {
    double x;
    double y;
    double z;

    public Translation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Matrix3D getMatrix() {
        return Matrix3D.getTranslationMatrix(-x, -y, -z);
    }
}
