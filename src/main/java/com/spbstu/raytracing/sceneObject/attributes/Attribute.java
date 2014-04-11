package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Interface for attributes defining object conversation in 3D
 * @author vva
 */
public interface Attribute {

    /**
     * Returns attribute conversation matrix
     * @return attribute conversation matrix
     */
    @NonNull
    public Matrix getMatrix();
}
