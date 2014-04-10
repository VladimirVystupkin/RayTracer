package com.spbstu.raytracing.jframe;

import com.spbstu.raytracing.Camera;
import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.RayTracer;
import com.spbstu.raytracing.sceneObject.SceneObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author vva
 * @date 11.03.14
 * @description
 */
public class UpdatableRayTracerFrame extends JFrame {


    public UpdatableRayTracerFrame(final Camera camera, final Collection<SceneObject> sceneObjects, final Collection<LightSource> lightSources) {
        setSize(camera.getScreenWidth(), camera.getScreenHeight());
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final Boolean[] isNeedToCalc = {true};


        new Thread(new Runnable() {
            @Override
            public void run() {
                RayTracer.RayTracerInfo rayTracerInfo = new RayTracer.RayTracerInfo(Color.WHITE, RayTracer.RayTracerInfo.LightningStyle.PHONG_BLINN, 100);
                try {
                    new RayTracer(rayTracerInfo, camera, sceneObjects, lightSources).apply();
                    ImageIO.write(camera.getImage(), "bmp", new File("result.bmp"));
//                    isNeedToCalc[0] = false;
                } catch (InterruptedException | IOException e) {

                    e.printStackTrace();
                }
            }
        }).start();

        final JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(camera.getImage(), 0, 0, null);
            }
        };

        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isNeedToCalc[0]) {
                    pane.invalidate();
                    pane.repaint();
                }
            }
        }).start();


        add(pane);
    }
}
