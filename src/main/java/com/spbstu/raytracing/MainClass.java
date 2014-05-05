package com.spbstu.raytracing;

import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.sceneObject.SceneObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @autor vva
 */
public class MainClass {


    public static void main(String args[]) {
        String filePath = null;
        String outputFile = null;
        int resX = 0;
        int resY = 0;
        int traceDepth = 1;

        if (args.length != 5) {
            System.out.println("Wrong number of arguments: expected 5, found " + args.length);
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--scene")) {
                filePath = args[i].split("=")[1];
                break;
            }
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--resolution_x")) {
                resX = Integer.parseInt(args[i].split("=")[1]);
                break;
            }
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--resolution_y")) {
                resY = Integer.parseInt(args[i].split("=")[1]);
                break;
            }
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--output")) {
                outputFile = args[i].split("=")[1].trim();
                break;
            }
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--trace_depth")) {
                traceDepth = Integer.parseInt(args[i].split("=")[1]);
                break;
            }
        }
        RayTracer.RayTracerInfo rayTracerInfo = new RayTracer.RayTracerInfo(Color.BLACK, RayTracer.RayTracerInfo.LightningStyle.PHONG, traceDepth);
        try {
            System.out.println("Start parsing " + filePath);
            Map<SceneLoader.ResultInfo, Object> resMap = new SceneLoader().loadFromStream(Files.newInputStream(Paths.get(filePath)), new Screen(resX, resY), rayTracerInfo);
            Camera camera = (Camera) resMap.get(SceneLoader.ResultInfo.CAMERA);
            java.util.List<SceneObject> sceneObjects = (java.util.List<SceneObject>) resMap.get(SceneLoader.ResultInfo.SCENE_OBJECTS);
            java.util.List<LightSource> lightSources = (java.util.List<LightSource>) resMap.get(SceneLoader.ResultInfo.LIGHT_SOURCES);
            System.out.println("Parsed successfully");
            System.out.println("Start creating output image");
            new RayTracer(rayTracerInfo, camera, sceneObjects, lightSources).apply();
            System.out.println("Output image created successfully");
            System.out.println("Trying to save output image as " + outputFile);
            if (!outputFile.endsWith(".jpeg") && !outputFile.endsWith(".jpg") && !outputFile.endsWith(".bmp")) {
                System.out.println("Unknown format ." + outputFile.split("\\.")[outputFile.split("\\.").length - 1]);
                return;
            }
            ImageIO.write(camera.getImage(), outputFile.split("\\.")[outputFile.split("\\.").length - 1], new File(outputFile));
            System.out.println("Output image saved successfully");
        } catch (IOException e) {
            System.out.println("Unable to parse  file");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
