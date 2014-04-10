package com.spbstu.raytracing.sceneObject.attributes;

import com.spbstu.raytracing.math.Matrix;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class Attributes {
    public enum AttributeName {
        TRANSLATION,
        ORIENTATION,
        SCALE
    }

    public static Map<AttributeName, ? extends Attribute> getAttributes(@Nullable Translation t, @Nullable Orientation o, @Nullable Scale s) {
        Map<AttributeName, Attribute> attributesMap = new HashMap<>();
        attributesMap.put(AttributeName.TRANSLATION, t);
        attributesMap.put(AttributeName.ORIENTATION, o);
        attributesMap.put(AttributeName.SCALE, s);
        return attributesMap;
    }

    public static Matrix getCommonMatrix(Map<AttributeName, Attribute> attributesMap) {
        Translation translation = (Translation) attributesMap.get(AttributeName.TRANSLATION);
        Matrix tMatrix = translation==null? Matrix.getIdentity():translation.getMatrix();
        Orientation orientation = (Orientation)attributesMap.get(AttributeName.ORIENTATION);
        Matrix oMatrix = orientation==null? Matrix.getIdentity():orientation.getMatrix();
        Scale scale = (Scale)attributesMap.get(AttributeName.SCALE);
        Matrix sMatrix = scale==null? Matrix.getIdentity():scale.getMatrix();
        return Matrix.multiply(sMatrix, oMatrix, tMatrix);
    }
}
