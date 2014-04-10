package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector;

import java.awt.*;

/**
 * @author vva
 * @date 05.04.14
 * @description
 */
public class DirectionalLightSource implements LightSource {

    final Vector direction;
    final Color color;

    public DirectionalLightSource(Vector direction, Color color) {
        this.direction = direction;
        this.direction.normalize();
        this.color = color;
    }

    @Override
    public Vector getOnPointDirection(Point3D point) {
        return direction;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public double getIntensity(Point3D point) {
        return 1;
    }
}
