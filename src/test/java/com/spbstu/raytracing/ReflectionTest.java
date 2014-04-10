package com.spbstu.raytracing;

import com.spbstu.raytracing.jframe.UpdatableRayTracerFrame;
import com.spbstu.raytracing.lightning.DirectionalLightSource;
import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.lightning.PointLightSource;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector;
import com.spbstu.raytracing.sceneObject.*;
import com.spbstu.raytracing.sceneObject.attributes.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 * @date 03.04.14
 * @description
 */
public class ReflectionTest {
    public static void main(String[] args) throws IOException {

        //ObjectParser.parse("model.obj");

        Camera camera = new Camera(new Point3D(0, 0, -500), new Vector(0, 0, 1), new Vector(0, -1, 0), new Camera.FovInfo(45, 30),
                new Screen(750, 500));


        final Point3D center = new Point3D(0, 0, 0);
        Material.Component ambient = new Material.Component(0.3, 0.3, 0.3);
        Material.Component diffuse = new Material.Component(0.3, 0.3, 0.3);
        Material.Component specular = new Material.Component(0.3, 0.3, 0.3);


        Material.Component ambient1 = new Material.Component(0.0, 0.0, 0.0);
        Material.Component diffuse1 = new Material.Component(1, 1, 1);
        Material.Component specular1 = new Material.Component(0.5, 0.5, 0.5);


        Material.Component ambient2 = new Material.Component(0.1, 0.3, 0.1);
        Material.Component diffuse2 = new Material.Component(0.811, 0.821, 0.831);
        Material.Component specular2 = new Material.Component(0.0, 0.0, 0.0);


        List<SceneObject> sceneObjects = new ArrayList<>();
        Orientation orientation = new Orientation(-45, -45, 0);
        Map<Attributes.AttributeName, Attribute> attributes = new HashMap<>();
        attributes.put(Attributes.AttributeName.TRANSLATION, new Translation(0, 50, 0));
        attributes.put(Attributes.AttributeName.ORIENTATION, orientation);
        attributes.put(Attributes.AttributeName.SCALE, new Scale(0.33, 0.33, 0.33));
        sceneObjects.add(new Torus(50, 100, new Material(ambient, diffuse, specular, 20, 0.9), attributes));
        attributes.clear();
        attributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(-30, 0, 0));
        sceneObjects.add(new Plane(0, 1, 0, 0, new Material(ambient1, diffuse1, specular1, 10, 0.9), attributes));
        attributes.put(Attributes.AttributeName.TRANSLATION, new Translation(-20, 0, -100));
        sceneObjects.add(new Sphere(30, new Material(ambient2, diffuse2, specular2, 10, 0.0, 0.5, 2), attributes));
        List<LightSource> lightSources = new ArrayList<>();
        lightSources.add(new PointLightSource(new Point3D(100, 500, -200), Color.WHITE, 2, 800));
        lightSources.add(new DirectionalLightSource(new Vector(1, -1, 1), new Color(130, 130, 130)));
        new UpdatableRayTracerFrame(camera, sceneObjects, lightSources);
    }
}
