package com.spbstu.raytracing.light;

import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.math.Point3D;

import java.awt.*;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public interface LightSource {
    public Vector3D getOnPointDirection(Point3D point);

    public Color getColor();

    public double getIntensity(Point3D point);


}
