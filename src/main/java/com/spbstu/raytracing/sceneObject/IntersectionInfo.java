package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.sceneObject.Material;


import java.awt.*;

/**
 * @autor vva
 */
public class IntersectionInfo {
    final Point point;
    final Vector normal;
    final Material material;

    public IntersectionInfo(final Point point, final Vector normal, final Material material) {
        this.point = point;
        this.normal = normal;
        this.material = material;
    }


    public Point getPoint() {
        return point;
    }


    public Vector getNormal() {
        return normal;
    }


    public Color getColor(final Color ambientColor, final Color diffuseColor, final Color specularColor) {
        return new Color(material.getColor(ambientColor.getRGB(), diffuseColor.getRGB(), specularColor.getRGB()));
    }

    public int getSpecularPower() {
        return material.specularPower;
    }

    public double getReflectionFactor() {
        return material.reflectionFactor;
    }

    public double getRefractionFactor() {
        return material.refractionFactor;
    }

    public double getRefractionIndex() {
        return material.refractionIndex;
    }


    public Material getMaterial() {
        return material;
    }
}
