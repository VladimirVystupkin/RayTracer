package com.spbstu.raytracing;

import com.spbstu.raytracing.math.*;
import com.spbstu.raytracing.math.Point;
import com.spbstu.raytracing.sceneObject.attributes.Attribute;
import com.spbstu.raytracing.sceneObject.attributes.Attributes;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Camera defining class
 *
 * @author vva
 */
public class Camera {
    final Point location;
    final Vector dir;
    final Vector up;
    final Vector right;
    final FovInfo fovInfo;
    Screen screen;


    /**
     * Constructor defining camera with location, direction and up vectors,fov info and screen
     *
     * @param location camera location
     * @param dir      camera direction to screen center vector
     * @param up       camera up direction vector
     * @param fovInfo  camera fov info
     * @param screen   camera screen
     */
    public Camera(@NonNull final Point location, @NonNull final Vector dir, @NonNull final Vector up, @NonNull final FovInfo fovInfo, @NonNull Screen screen) {
        this.location = location;
        this.dir = dir.getNormalized();
        this.up = up.getNormalized();
        this.right = Vector.cross(dir, up);
        this.fovInfo = fovInfo;
        this.screen = screen;
    }

    /**
     * Constructor defining camera with location, direction and up vectors,fov info and screen and object 3D conversation attributes
     *
     * @param location      camera location
     * @param dir           camera direction to screen center vector
     * @param up            camera up direction vector
     * @param fovInfo       camera fov info
     * @param screen        camera screen
     * @param attributesMap map from attributes
     * @see com.spbstu.raytracing.sceneObject.SceneObject
     * @see com.spbstu.raytracing.sceneObject.attributes.Attribute
     */
    public Camera(@NonNull final com.spbstu.raytracing.math.Point location, @NonNull final Vector dir, @NonNull final Vector up, @NonNull final FovInfo fovInfo, @NonNull Screen screen, @NonNull final Map<Attributes.AttributeName, Attribute> attributesMap) {
        Matrix matrix = Attributes.getCommonMatrix(attributesMap);
        this.location = Matrix.multiply(matrix, location);
        this.dir = Matrix.multiply(matrix, dir).getNormalized();
        this.up = Matrix.multiply(matrix, up).getNormalized();
        this.right = Vector.cross(dir, up);
        this.fovInfo = fovInfo;
        this.screen = screen;
    }


    /**
     * Sets pixel with defined color to camera screen
     * @param pixelInfo pixel coordinates
     * @param color pixel new color
     */
    public void setColor(@NonNull final Screen.PixelInfo pixelInfo, @NonNull final Color color) {
        screen.setColor(pixelInfo, color.getRGB());
    }

    /**
     * Returns camera screen width
     * @return camera screen width
     */
    public int getScreenWidth() {
        return screen.width;
    }

    /**
     * Returns camera screen height
     * @return camera screen height
     */
    public int getScreenHeight() {
        return screen.height;
    }

    /**
     * Returns camera screen image
     * @return camera screen image
     */
    @NonNull
    public BufferedImage getImage() {
        return screen.image;
    }


    /**
     * Returns emitted ray from camera to screen pixel by screen coordinates
     * @param x camera screen x coordinate
     * @param y camera screen x coordinate
     * @return emitted ray from camera to screen pixel by screen coordinates
     */
    @NonNull
    public Ray emitRay(final int x, final int y) {
        Vector xDir = Vector.add(dir, Vector.onNumber(right, Math.tan(Math.PI / 180 * fovInfo.fovX) * (x - screen.width / 2) / screen.width * 2));
        Vector yDir = Vector.add(dir, Vector.onNumber(up, Math.tan(Math.PI / 180 * fovInfo.fovY) * (y - screen.height / 2) / screen.height * 2));
        Vector rayDir = Vector.add(xDir, yDir).getNormalized();
        return new Ray(location, rayDir);
    }

    /**
     * Class defining horizontal and vertical  camera fov
     */
    public static class FovInfo {
        final double fovX;
        final double fovY;

        /**
         * Default Constructor defining horizontal and vertical  camera fov
         * @param fovX horizontal camera fov
         * @param fovY vertical camera fov
         */
        public FovInfo(double fovX, double fovY) {
            this.fovX = fovX;
            this.fovY = fovY;
        }
    }

}
