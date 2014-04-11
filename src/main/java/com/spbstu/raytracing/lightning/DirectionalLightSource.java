package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;

/**
 * Directional light source class
 * @author vva
 */
public class DirectionalLightSource implements LightSource {

    final Vector direction;
    final Color color;

    /**
     * Default constructor which makes directional light source by direction vector and color
     * @param direction light source direction vector
     * @param color light source color
     */
    public DirectionalLightSource(@NonNull final Vector direction,@NonNull final Color color) {
        this.direction = direction.getNormalized();
        this.color = color;
    }

    @Override
    @NonNull
    public Vector getOnPointDirection(@NonNull final Point point) {
        return direction;
    }

    @Override
    @NonNull
    public Color getColor() {
        return color;
    }

    @Override
    @NonNull
    public double getIntensity(@NonNull final Point point) {
        return 1;
    }
}
