package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Attribute defining object 3D size
 * @author vva
 */
public class Scale implements Attribute {

    double sx, sy, sz;

    /**
     * Constructor  defining object 3D size by 3 scale factors
     * @param sx scale factor for x axis
     * @param sy scale factor for y axis
     * @param sz scale factor for z axis
     */
    public Scale(final double sx,final double sy,final double sz) {
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    @Override
    @NonNull
    public Matrix getMatrix() {
        return new Matrix(
                1 / sx, 0, 0, 0,
                0, 1 / sy, 0, 0,
                0, 0, 1 / sz, 0,
                0, 0, 0, 1
        );
    }
}
