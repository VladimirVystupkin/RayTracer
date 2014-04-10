package com.spbstu.raytracing.light;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector3D;

import java.awt.*;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class PointLightSource implements LightSource {

    final Point3D location;
    final Color color;
    final int fadeExponent;
    final double range;

    public PointLightSource(Point3D location, Color color, int fadeExponent, double range) {
        this.location = location;
        this.color = color;
        this.fadeExponent = fadeExponent;
        this.range = range;
    }

    @Override
    public Vector3D getOnPointDirection(Point3D point) {
        Vector3D onPointDir = new Vector3D(location, point);
        onPointDir.normalize();
        return onPointDir;
    }

    @Override
    public Color getColor() {
        return color;
    }


    @Override
    public double getIntensity(Point3D point) {
        double distance = new Vector3D(location, point).length();
        return Math.max(1 - Math.pow(distance / range, 1.0 / fadeExponent), 0);
    }
}
