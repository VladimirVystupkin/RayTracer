package com.spbstu.raytracing.old;

import com.spbstu.raytracing.old.baseObjects.BaseObject;
import com.spbstu.raytracing.old.baseObjects.Plane;
import com.spbstu.raytracing.old.baseObjects.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vva
 * @date 10.03.14
 * @description
 */
public class MainClass {
    public static void main(String[] args) {

        final Screen screen = new Screen(320, 240, BufferedImage.TYPE_INT_RGB);
        List<BaseObject> sceneObjects = new ArrayList<BaseObject>();
        sceneObjects.add(new Triangle(new Point3D(0, 0, 10), new Point3D(160, 240, 10), new Point3D(320, 0, 10), Color.RED.getRGB()));
        //sceneObjects.add(new Plane(new Point3D(0, 0, 10), new Point3D(160, 240, 10), new Point3D(320, 0, 10), Color.GREEN.getRGB()));
        new RayTracingAlgorithm().apply(new Point3D(160, 120, -10), screen, sceneObjects);
        JFrame jFrame = new JFrame();
        jFrame.setSize(screen.getWidth(), screen.getHeight());
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(screen.image, 0, 0, null);
            }
        };
        jFrame.add(pane);

    }
}
