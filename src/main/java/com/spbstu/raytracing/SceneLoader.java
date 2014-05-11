package com.spbstu.raytracing;

import com.spbstu.raytracing.lightning.DirectionalLightSource;
import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.lightning.PointLightSource;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.CSGNode;
import com.spbstu.raytracing.sceneObject.Material;
import com.spbstu.raytracing.sceneObject.SceneObject;

import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class SceneLoader {

    public Map<ResultInfo, Object> loadFromStream(InputStream inputStream, final Screen screen, final RayTracer.RayTracerInfo rayTracerInfo) throws IOException {
        Camera camera = null;
        List<LightSource> lightSources = new ArrayList<>();
        List<SceneObject> sceneObjects = new ArrayList<>();
        Map<Integer, Material> materialKeyMap = new HashMap<>();
        ArrayList os = (ArrayList) new Yaml().load(inputStream);
        for (Object o : os) {
            HashMap hashMap = (HashMap) o;
            String key = (String) hashMap.keySet().iterator().next();
            switch (key) {
                case "shading_type":
                    rayTracerInfo.lightningStyle = "blinn_phong﻿".equals(hashMap.get("shading_type")) ? RayTracer.RayTracerInfo.LightningStyle.PHONG_BLINN : RayTracer.RayTracerInfo.LightningStyle.PHONG;
                    break;
                case "camera":
                    camera = Camera.fromMap((HashMap) hashMap.get("camera"), screen);
                    break;
                case "material":
                    materialKeyMap.put(System.identityHashCode(key), Material.fromMap((HashMap) hashMap.get(key)));
                    break;
                case "directional_light":
                    lightSources.add(DirectionalLightSource.fromMap((HashMap) hashMap.get("directional_light")));
                    break;
                case "point_light":
                    lightSources.add(PointLightSource.fromMap((HashMap) hashMap.get("point_light")));
                    break;
                case "node":
                    for (Object nodeMap : ((ArrayList) hashMap.get("node")))
                        sceneObjects.add(CSGNode.fromMap((ArrayList) ((HashMap) nodeMap).get("node"), materialKeyMap));
                    break;
            }

        }
        Map<ResultInfo, Object> resultInfo = new HashMap<>();
        resultInfo.put(ResultInfo.CAMERA, camera);
        resultInfo.put(ResultInfo.SCENE_OBJECTS, sceneObjects);
        resultInfo.put(ResultInfo.LIGHT_SOURCES, lightSources);
        resultInfo.put(ResultInfo.RAY_TRACER_INFO, rayTracerInfo);
        return resultInfo;
    }


    public static enum ResultInfo {
        CAMERA,
        SCENE_OBJECTS,
        LIGHT_SOURCES,
        RAY_TRACER_INFO
    }
}
