package com.spbstu.raytracing.old;

import java.awt.image.BufferedImage;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class Screen {

    int width, height;
    BufferedImage image;

    public Screen(int width, int height, int imageType) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, imageType);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }



    public int getColor(int x, int y) {
        return image.getRGB(x, y);
    }

    public void setColor(int x, int y, int color) {
        image.setRGB(x, y, color);
    }

    public BufferedImage getImage() {
        return image;
    }
}
