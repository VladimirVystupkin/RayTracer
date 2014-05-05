package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;


import java.awt.*;
import java.util.HashMap;

/**
 * @author vva
 */
public class DirectionalLightSource implements LightSource {

    final Vector direction;
    final Color color;

    public DirectionalLightSource(final Vector direction, final Color color) {
        this.direction = direction.getNormalized();
        this.color = color;
    }

    @Override

    public Vector getOnPointDirection(final Point point) {
        return direction;
    }

    @Override

    public Color getColor() {
        return color;
    }

    @Override

    public double getIntensity(final Point point) {
        return 1;
    }


    public static DirectionalLightSource fromMap(final HashMap hashMap) {
        HashMap directionAttributes = (HashMap) hashMap.get("direction");
        double x = directionAttributes.containsKey("x") ? Double.parseDouble(directionAttributes.get("x").toString()) : 0;
        double y = directionAttributes.containsKey("y") ? Double.parseDouble(directionAttributes.get("y").toString()) : 0;
        double z = directionAttributes.containsKey("z") ? Double.parseDouble(directionAttributes.get("z").toString()) : 0;
        Vector dir = new Vector(x, y, z);
        HashMap colorAttributes = (HashMap) hashMap.get("color");
        double r = colorAttributes.containsKey("r") ? Double.parseDouble(colorAttributes.get("r").toString()) : 0;
        double g = colorAttributes.containsKey("g") ? Double.parseDouble(colorAttributes.get("g").toString()) : 0;
        double b = colorAttributes.containsKey("b") ? Double.parseDouble(colorAttributes.get("b").toString()) : 0;
        Color color = new Color((int) (255 * r), (int) (255 * g), (int) (255 * b));
        return new DirectionalLightSource(dir, color);
    }
}
