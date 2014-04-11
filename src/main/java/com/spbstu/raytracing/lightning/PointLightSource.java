package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;

/**
 * Point light source class
 * @see com.spbstu.raytracing.lightning.LightSource
 */
public class PointLightSource implements LightSource {

    final Point location;
    final Color color;
    final int fadeExponent;
    final double range;

    /**
     * Default point light source constructor
     * @param location light source location point
     * @param color light source color
     * @param fadeExponent pow of light source intensity fade by distance
     * @param range radius of  lightning area
     */
    public PointLightSource(@NonNull final Point location,@NonNull final Color color,final int fadeExponent, @NonNull final double range) {
        this.location = location;
        this.color = color;
        this.fadeExponent = fadeExponent;
        this.range = range;
    }

    @Override
    @NonNull
    public Vector getOnPointDirection(@NonNull final Point point) {
        return  new Vector(location, point).getNormalized();
    }

    @Override
    @NonNull
    public Color getColor() {
        return color;
    }


    @Override
    public double getIntensity(@NonNull final Point point) {
        double distance = new Vector(location, point).length();
        return Math.max(1 - Math.pow(distance / range, 1.0 / fadeExponent), 0);
    }
}
