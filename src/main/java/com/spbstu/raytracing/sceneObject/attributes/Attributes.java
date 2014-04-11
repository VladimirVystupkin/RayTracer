package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for getting common object 3D conversation matrix from attributes
 * @author vva
 * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
 */
public class Attributes {

    /*Conversation names enumeration*/
    public enum AttributeName {
        TRANSLATION,
        ORIENTATION,
        SCALE
    }

    /**
     * Makes map from attributes({@link com.spbstu.raytracing.sceneObject.attributes.Attributes.AttributeName}->{@link com.spbstu.raytracing.sceneObject.attributes.Attribute})
     * @param t translation attribute
     * @param o orientation attribute
     * @param s scale attribute
     * @return map from attribute
     */
    @NonNull
    public static Map<AttributeName, Attribute> getAttributes(@Nullable final Translation t,
                                                              @Nullable final Orientation o,
                                                              @Nullable final Scale s) {
        Map<AttributeName, Attribute> attributesMap = new HashMap<>();
        attributesMap.put(AttributeName.TRANSLATION, t);
        attributesMap.put(AttributeName.ORIENTATION, o);
        attributesMap.put(AttributeName.SCALE, s);
        return attributesMap;
    }

    /**
     * Returns common object 3D conversation matrix from attributes
     * @param attributesMap attributes map({@link com.spbstu.raytracing.sceneObject.attributes.Attributes.AttributeName}->{@link com.spbstu.raytracing.sceneObject.attributes.Attribute})
     * @return common object 3D conversation matrix
     */
    @NonNull
    public static Matrix getCommonMatrix(@NonNull final Map<AttributeName, Attribute> attributesMap) {
        Translation translation = (Translation) attributesMap.get(AttributeName.TRANSLATION);
        Matrix tMatrix = translation == null ? Matrix.getIdentity() : translation.getMatrix();
        Orientation orientation = (Orientation) attributesMap.get(AttributeName.ORIENTATION);
        Matrix oMatrix = orientation == null ? Matrix.getIdentity() : orientation.getMatrix();
        Scale scale = (Scale) attributesMap.get(AttributeName.SCALE);
        Matrix sMatrix = scale == null ? Matrix.getIdentity() : scale.getMatrix();
        return Matrix.multiply(sMatrix, oMatrix, tMatrix);
    }
}
