package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Attribute defining object 3D translation
 * @author vva
 */
public class Translation implements Attribute {
    double x;
    double y;
    double z;

    /**
     * Constructor defining object 3D translation by 3 coordinates
     * @param x coordinate to translate for x axis
     * @param y coordinate to translate for y axis
     * @param z coordinate to translate for z axis
     */
    public Translation(final double x,final double y,final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    @NonNull
    public Matrix getMatrix() {
        return Matrix.getTranslationMatrix(-x, -y, -z);
    }
}
