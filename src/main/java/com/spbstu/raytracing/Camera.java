package com.spbstu.raytracing;

import com.spbstu.raytracing.math.Matrix3D;
import com.spbstu.raytracing.math.Ray3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.sceneObject.Attributes;
import com.spbstu.raytracing.sceneObject.Orientation;
import com.sun.javafx.beans.annotations.NonNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author vva
 * @date 01.04.14
 * @description
 */
public class Camera {
    Point3D location;
    Vector3D dir;
    Vector3D up;
    Vector3D right;
    FovInfo fovInfo;
    Screen screen;
    int currentX = 0, currentY = 0;


    public Camera(@NonNull final Point3D location, @NonNull final Vector3D dir, @NonNull final Vector3D up, @NonNull final FovInfo fovInfo, @NonNull Screen screen) {
        this.location = location;
        this.dir = dir;
        this.up = up;
        this.dir.normalize();
        this.up.normalize();
        this.right = Vector3D.cross(dir, up);
        this.fovInfo = fovInfo;
        this.screen = screen;
    }

    public Camera(@NonNull final Point3D location, @NonNull final Vector3D dir, @NonNull final Vector3D up, @NonNull final FovInfo fovInfo, @NonNull Screen screen, Map<Attributes.Attribute, Matrix3D> attr) {
        Matrix3D matrix = Attributes.getCommonMatrix(attr);
        this.location = Matrix3D.multiply(matrix, location);
        this.dir = Matrix3D.multiply(matrix, dir);
        this.up = Matrix3D.multiply(matrix, up);
        this.dir.normalize();
        this.up.normalize();
        this.right = Vector3D.cross(dir, up);
        this.fovInfo = fovInfo;
        this.screen = screen;
    }


//    public Relation<Screen.PixelInfo, Ray3D> getNext() {
//        Vector3D xDir = Vector3D.add(dir, Vector3D.onNumber(right, Math.tan(Math.PI / 180 * fovInfo.fovX) * (currentX - screen.width / 2) / screen.width * 2));
//        Vector3D yDir = Vector3D.add(dir, Vector3D.onNumber(up, Math.tan(Math.PI / 180 * fovInfo.fovY) * (currentY - screen.height / 2) / screen.height * 2));
//        Vector3D rayDir = Vector3D.add(xDir, yDir);
//        rayDir.normalize();
//        Screen.PixelInfo pixelInfo = new Screen.PixelInfo(currentX, currentY);
//        currentX++;
//        if (currentX == screen.width) {
//            currentX = 0;
//            currentY++;
//        }
//
//        return new Relation<Screen.PixelInfo, Ray3D>(pixelInfo, new Ray3D(location, rayDir));
//    }

//    public Collection<Relation<Screen.PixelInfo, Ray3D>> getRaysThroughPixels() {
//        Collection<Relation<Screen.PixelInfo, Ray3D>> rayThroughPixels = new ArrayList<>();
//        Vector3D right = Vector3D.cross(dir, up);
//        for (int x = 0; x < screen.width; x++) {
//            Vector3D xDir = Vector3D.add(dir, Vector3D.onNumber(right, Math.tan(Math.PI / 180 * fovInfo.fovX) * (x - screen.width / 2) / screen.width * 2));
//            for (int y = 0; y < screen.height; y++) {
//                Vector3D yDir = Vector3D.add(dir, Vector3D.onNumber(up, Math.tan(Math.PI / 180 * fovInfo.fovY) * (y - screen.height / 2) / screen.height * 2));
//                Vector3D rayDir = Vector3D.add(xDir, yDir);
//                rayDir.normalize();
//                rayThroughPixels.add(new Relation<Screen.PixelInfo, Ray3D>(new Screen.PixelInfo(x, y), new Ray3D(location, rayDir)));
//            }
//        }
//        return rayThroughPixels;
//    }

    public void setColor(Screen.PixelInfo pixelInfo, Color color) {
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

    public Iterator<Relation<Screen.PixelInfo, Ray3D>> getRaysThroughPixelsIterator() {
        return new CameraIterator();
    }

    private class CameraIterator implements Iterator<Relation<Screen.PixelInfo, Ray3D>> {
        int currentX = currentY = 0;

        @Override
        public boolean hasNext() {
            return currentX < screen.width && currentY < screen.height;
        }

        @Override
        public Relation<Screen.PixelInfo, Ray3D> next() {
            Vector3D xDir = Vector3D.add(dir, Vector3D.onNumber(right, Math.tan(Math.PI / 180 * fovInfo.fovX) * (currentX - screen.width / 2) / screen.width * 2));
            Vector3D yDir = Vector3D.add(dir, Vector3D.onNumber(up, Math.tan(Math.PI / 180 * fovInfo.fovY) * (currentY - screen.height / 2) / screen.height * 2));
            Vector3D rayDir = Vector3D.add(xDir, yDir);
            rayDir.normalize();
            Screen.PixelInfo pixelInfo = new Screen.PixelInfo(currentX, currentY);
            currentX++;
            if (currentX == screen.width) {
                currentX = 0;
                currentY++;
            }

            return new Relation<Screen.PixelInfo, Ray3D>(pixelInfo, new Ray3D(location, rayDir));
        }

        @Override
        public void remove() {
        }
    }

    public Ray3D emitRay(int x, int y) {
        Vector3D xDir = Vector3D.add(dir, Vector3D.onNumber(right, Math.tan(Math.PI / 180 * fovInfo.fovX) * (x - screen.width / 2) / screen.width * 2));
        Vector3D yDir = Vector3D.add(dir, Vector3D.onNumber(up, Math.tan(Math.PI / 180 * fovInfo.fovY) * (y - screen.height / 2) / screen.height * 2));
        Vector3D rayDir = Vector3D.add(xDir, yDir);
        rayDir.normalize();
        return new Ray3D(location, rayDir);
    }

    public static class FovInfo {
        double fovX;
        double fovY;

        public FovInfo(double fovX, double fovY) {
            this.fovX = fovX;
            this.fovY = fovY;
        }
    }

}
