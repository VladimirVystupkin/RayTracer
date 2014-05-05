package com.spbstu.raytracing;



import java.awt.image.BufferedImage;

/**
 * @author vva
 */
public class Screen {
    BufferedImage image;
    final int width, height;

    public static class PixelInfo {
        final int x, y;

        public PixelInfo(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }

    public Screen(final int width, final int height, final int type) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, type);
    }

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setColor(final PixelInfo pixelInfo, final int color) {
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
