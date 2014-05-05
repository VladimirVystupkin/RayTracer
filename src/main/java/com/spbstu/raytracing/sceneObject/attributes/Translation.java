package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;


/**
 * @author vva
 */
public class Translation implements Attribute {
    double x;
    double y;
    double z;

    public Translation(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override

    public Matrix getMatrix() {
        return Matrix.getTranslationMatrix(-x, -y, -z);
    }
}
