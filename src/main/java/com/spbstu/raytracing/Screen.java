package com.spbstu.raytracing;

import com.spbstu.raytracing.math.Ray3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.old.Point3D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 16.03.14
 * @description
 */
public class Screen {
    BufferedImage image;
    int width, height;

    public static class PixelInfo {
        int x, y;

        public PixelInfo(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


    public Screen(int width, int height, int type) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, type);
    }

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public Map<PixelInfo, Ray3D> getRaysByPixel() {
        Map<PixelInfo, Ray3D> raysByPixel = new HashMap<PixelInfo, Ray3D>();
        return raysByPixel;
    }


    public void setColor(PixelInfo pixelInfo, int color) {
        image.setRGB(pixelInfo.x, pixelInfo.y, color);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
