package com.spbstu.raytracing;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.spbstu.raytracing.sceneObject.attributes.Orientation;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vva
 */
public class Camera {
    final Point location;
    final Vector dir;
    final Vector up;
    final Vector right;
    final FovInfo fovInfo;
    final Screen screen;

    public Camera(final Point location, final Vector dir, final Vector up, final FovInfo fovInfo, Screen screen) {
        this.location = location;
        this.dir = dir.getNormalized();
        this.up = up.getNormalized();
        this.right = Vector.cross(dir, up).getNormalized();
        this.fovInfo = fovInfo;
        this.screen = screen;
    }

    public Camera(final com.spbstu.raytracing.math.Point location, final Vector dir, final Vector up, final FovInfo fovInfo, Screen screen, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        Matrix matrix = Attributes.getCommonMatrix(attributesMap);
        this.location = Matrix.multiply(matrix, location);
        this.dir = Matrix.multiply(matrix, dir).getNormalized();
        this.up = Matrix.multiply(matrix, up).getNormalized();
        this.right = Vector.cross(dir, up);
        this.fovInfo = fovInfo;
        this.screen = screen;
    }

    public static Camera fromMap(final HashMap cameraAttributes, final Screen screen) {
        HashMap positionAttributes = (HashMap) cameraAttributes.get("position");
        double x = positionAttributes.containsKey("x") ? Double.parseDouble(positionAttributes.get("x").toString()) : 0;
        double y = positionAttributes.containsKey("y") ? Double.parseDouble(positionAttributes.get("y").toString()) : 0;
        double z = positionAttributes.containsKey("z") ? Double.parseDouble(positionAttributes.get("z").toString()) : 0;
        Point pos = new Point(x, y, z);
        double fovX = Double.parseDouble(cameraAttributes.get("fov_x").toString());
        double fovY = Double.parseDouble(cameraAttributes.get("fov_y").toString());
        FovInfo info = new FovInfo(fovX, fovY);
        HashMap orientationAttributes = (HashMap) cameraAttributes.get("orientation");
        double h = orientationAttributes.containsKey("h") ? Double.parseDouble(orientationAttributes.get("h").toString()) : 0;
        double p = orientationAttributes.containsKey("p") ? Double.parseDouble(orientationAttributes.get("p").toString()) : 0;
        double r = orientationAttributes.containsKey("r") ? Double.parseDouble(orientationAttributes.get("r").toString()) : 0;
        Map<Attributes.AttributeName, Attribute> cameraOrientation = new HashMap<>();
        cameraOrientation.put(Attributes.AttributeName.ORIENTATION, new Orientation(h, p, r));
        //  return new Camera(pos, Matrix.multiply(Attributes.getCommonMatrix(cameraOrientation), Vector.onNumber(pos.toVector3D(),-1).getNormalized()), new Vector(0, 1, 0), info, screen);
        //Vector dir = Vector.onNumber(pos.toVector3D(), -1).getNormalized();
        //double cosA = Vector.scalar(dir, new Vector(0, 1, 0));
        //double sinA = Math.sqrt(1 - cosA * cosA);
        Vector dir = Matrix.multiply(Attributes.getCommonMatrix(cameraOrientation), new Vector(0, 1, 0));
        Vector up = Matrix.multiply(Attributes.getCommonMatrix(cameraOrientation), new Vector(0, 0, 1));
        return new Camera(pos, dir, up, info, screen);
    }

    public void setColor(final Screen.PixelInfo pixelInfo, final Color color) {
        screen.setColor(pixelInfo, color.getRGB());
    }

    public int getScreenWidth() {
        return screen.width;
    }

    public int getScreenHeight() {
        return screen.height;
    }

    public BufferedImage getImage() {
        return screen.image;
    }

    public Ray emitRay(final int x, final int y) {
        Vector xDir = Vector.onNumber(right, Math.tan(Math.PI / 180 * fovInfo.fovX / 2) * (x - screen.width / 2) / screen.width);
        Vector zDir = Vector.onNumber(up, Math.tan(Math.PI / 180 * fovInfo.fovY / 2) * (screen.height / 2 - y) / screen.height);
        Vector rayDir = Vector.add(dir, xDir, zDir).getNormalized();
        return new Ray(location, rayDir);
    }

    public static class FovInfo {
        final double fovX;
        final double fovY;

        public FovInfo(double fovX, double fovY) {
            this.fovX = fovX;
            this.fovY = fovY;
        }
    }


}
