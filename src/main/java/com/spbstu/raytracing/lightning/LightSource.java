package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;


/**
 * A light source interface
 * @author vva
 */
public interface LightSource {

    /**
     * Returns normalized  direction vector from light source to point
     * @param point point on the light incident
     * @return normalized  direction vector from light source to point
     */
    @NonNull
    public Vector getOnPointDirection(@NonNull final Point point);

    /**
     * Returns light source color
     * @return light source color
     */
    @NonNull
    public Color getColor();

    /**
     * Return light source intensity from 0 to 1
     * @param point point on the light incident
     * @return light source intensity
     */
    public double getIntensity(@NonNull final Point point);


}
