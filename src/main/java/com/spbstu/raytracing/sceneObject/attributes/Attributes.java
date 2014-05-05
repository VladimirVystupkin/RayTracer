package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;



import java.util.HashMap;
import java.util.Map;

/**
 * @author vva
 */
public class Attributes {

    public enum AttributeName {
        TRANSLATION,
        ORIENTATION,
        SCALE
    }


    public static Map<AttributeName, Attribute> getAttributes(final Translation t,
                                                              final Orientation o,
                                                              final Scale s) {
        Map<AttributeName, Attribute> attributesMap = new HashMap<>();
        attributesMap.put(AttributeName.TRANSLATION, t);
        attributesMap.put(AttributeName.ORIENTATION, o);
        attributesMap.put(AttributeName.SCALE, s);
        return attributesMap;
    }


    public static Matrix getCommonMatrix(final Map<AttributeName, Attribute> attributesMap) {
        Translation translation = (Translation) attributesMap.get(AttributeName.TRANSLATION);
        Matrix tMatrix = translation == null ? Matrix.getIdentity() : translation.getMatrix();
        Orientation orientation = (Orientation) attributesMap.get(AttributeName.ORIENTATION);
        Matrix oMatrix = orientation == null ? Matrix.getIdentity() : orientation.getMatrix();
        Scale scale = (Scale) attributesMap.get(AttributeName.SCALE);
        Matrix sMatrix = scale == null ? Matrix.getIdentity() : scale.getMatrix();
        return Matrix.multiply(sMatrix, oMatrix, tMatrix);
    }
}
