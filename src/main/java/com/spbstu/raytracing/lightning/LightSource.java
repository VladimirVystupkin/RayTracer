package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;


import java.awt.*;


/**
 * @author vva
 */
public interface LightSource {


    public Vector getOnPointDirection(final Point point);


    public Color getColor();

    public double getIntensity(final Point point);
}
