package com.spbstu.raytracing;

import com.spbstu.raytracing.jframe.UpdatableRayTracerFrame;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @autor vva
 */
public class ParserTest {

    public static void main(String[] args) throws IOException {
        RayTracer.RayTracerInfo rayTracerInfo = new RayTracer.RayTracerInfo(Color.WHITE, RayTracer.RayTracerInfo.LightningStyle.PHONG, 3);
//        new UpdatableRayTracerFrame(new SceneLoader().loadFromStream(Files.newInputStream(Paths.get("scene_test_hpr.yaml")), new Screen(500, 500), rayTracerInfo));
        new UpdatableRayTracerFrame(new SceneLoader().loadFromStream(Files.newInputStream(Paths.get("scene.yml")), new Screen(500, 500), rayTracerInfo));
//        System.out.println("rayTracerInfo = " + rayTracerInfo);
    }
}
