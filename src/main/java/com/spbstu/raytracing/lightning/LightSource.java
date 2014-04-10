package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.math.Point3D;

import java.awt.*;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public interface LightSource {
    public Vector getOnPointDirection(Point3D point);

    public Color getColor();

    public double getIntensity(Point3D point);


}
