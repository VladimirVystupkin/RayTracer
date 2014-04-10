package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Matrix3D;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class Attributes {
    public enum Attribute {
        TRANSLATION,
        ORIENTATION,
        SCALE
    }

    public static Map<Attribute, Matrix3D> getAttributes(@Nullable Translation t, @Nullable Orientation o, @Nullable Scale s) {
        Map<Attribute, Matrix3D> attributesMap = new HashMap<>();
        attributesMap.put(Attribute.TRANSLATION, t == null ? null : t.getMatrix());
        attributesMap.put(Attribute.ORIENTATION, o == null ? null : o.getMatrix());
        attributesMap.put(Attribute.SCALE, s == null ? null : s.getMatrix());
        return attributesMap;
    }

    public static Matrix3D getCommonMatrix(Map<Attribute, Matrix3D> attributesMap) {
        Matrix3D matrix = Matrix3D.getIdentity();
        Matrix3D tMatrix = attributesMap.get(Attribute.TRANSLATION);
        Matrix3D oMatrix = attributesMap.get(Attribute.ORIENTATION);
        Matrix3D sMatrix = attributesMap.get(Attribute.SCALE);
        if (sMatrix != null) {
            matrix = Matrix3D.multiply(matrix, sMatrix);
        }
        if (oMatrix != null) {
            matrix = Matrix3D.multiply(matrix, oMatrix);
        }
        if (tMatrix != null) {
            matrix = Matrix3D.multiply(matrix, tMatrix);
        }
        return matrix;
    }
}
