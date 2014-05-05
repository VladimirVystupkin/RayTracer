package com.spbstu.raytracing.lightning;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;


import java.awt.*;
import java.util.HashMap;

/**
 * @author vva
 */
public class PointLightSource implements LightSource {

    final Point location;
    final Color color;
    final double fadeExponent;
    final double range;

    public PointLightSource(final Point location, final Color color, final double fadeExponent, final double range) {
        this.location = location;
        this.color = color;
        this.fadeExponent = fadeExponent;
        this.range = range;
    }

    @Override

    public Vector getOnPointDirection(final Point point) {
        return new Vector(location, point).getNormalized();
    }

    @Override

    public Color getColor() {
        return color;
    }


    @Override
    public double getIntensity(final Point point) {
        double distance = new Vector(location, point).length();
        return Math.max(1 - Math.pow(distance / range, 1.0 / fadeExponent), 0);
    }


    public static PointLightSource fromMap(final HashMap hashMap) {
        HashMap positionAttributes = (HashMap) hashMap.get("position");
        double x = positionAttributes.containsKey("x") ? Double.parseDouble(positionAttributes.get("x").toString()) : 0;
        double y = positionAttributes.containsKey("y") ? Double.parseDouble(positionAttributes.get("y").toString()) : 0;
        double z = positionAttributes.containsKey("z") ? Double.parseDouble(positionAttributes.get("z").toString()) : 0;
        Point pos = new Point(x, y, z);
        HashMap colorAttributes = (HashMap) hashMap.get("color");
        double r = colorAttributes.containsKey("r") ? Double.parseDouble(colorAttributes.get("r").toString()) : 0;
        double g = colorAttributes.containsKey("g") ? Double.parseDouble(colorAttributes.get("g").toString()) : 0;
        double b = colorAttributes.containsKey("b") ? Double.parseDouble(colorAttributes.get("b").toString()) : 0;
        Color color = new Color((int) (255 * r), (int) (255 * g), (int) (255 * b));
        double distance = Double.parseDouble(hashMap.get("distance").toString());
        double fadeExp = Double.parseDouble(hashMap.get("fade_exponent").toString());
        return new PointLightSource(pos, color, fadeExp, distance);
    }
}
