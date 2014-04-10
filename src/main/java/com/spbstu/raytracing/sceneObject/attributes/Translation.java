package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class Translation  implements Attribute{
    double x;
    double y;
    double z;

    public Translation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Matrix getMatrix() {
        return Matrix.getTranslationMatrix(-x, -y, -z);
    }
}
