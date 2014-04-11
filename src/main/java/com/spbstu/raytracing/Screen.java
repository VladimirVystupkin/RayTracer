package com.spbstu.raytracing;

import com.sun.javafx.beans.annotations.NonNull;

import java.awt.image.BufferedImage;

/**
 * Class defining screen
 *
 * @author vva
 */
public class Screen {
    BufferedImage image;
    final int width, height;

    /**
     * Class defining pixel coordinates
     */
    public static class PixelInfo {
        final int x, y;

        /**
         * Constructor to make pixel info
         *
         * @param x x coordinate
         * @param y y coordinate
         */
        public PixelInfo(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }


    /**
     * Constructor to make screen by dimensions and image type
     *
     * @param width  screen width
     * @param height screen height
     * @param type   image type
     * @see java.awt.image.BufferedImage
     */
    public Screen(final int width, final int height, final int type) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, type);
    }


    /**
     * Constructor to make screen by dimensions
     *
     * @param width  screen width
     * @param height screen height
     */
    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }


    /**
     * Sets screen pixel  color by pixel info
     * @param pixelInfo pixel info(pixel coordinates)
     * @param color new pixel color
     */
    public void setColor(@NonNull final PixelInfo pixelInfo,final int color) {
        image.setRGB(pixelInfo.x, pixelInfo.y, color);
    }

    /**
     * Returns current image from screen
     * @return current image from screen
     */
    @NonNull
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Returns screen width
     * @return screen width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns screen height
     * @return
     */
    public int getHeight() {
        return height;
    }
}
