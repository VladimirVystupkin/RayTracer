package com.spbstu.raytracing;

import com.spbstu.raytracing.jframe.UpdatableRayTracerFrame;
import com.spbstu.raytracing.light.DirectionalLightSource;
import com.spbstu.raytracing.light.LightSource;
import com.spbstu.raytracing.light.PointLightSource;
import com.spbstu.raytracing.math.Matrix3D;
import com.spbstu.raytracing.math.Point3D;
import com.spbstu.raytracing.math.Vector3D;
import com.spbstu.raytracing.sceneObject.*;

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

        Camera camera = new Camera(new Point3D(0, 0, -500), new Vector3D(0, 0, 1), new Vector3D(0, -1, 0), new Camera.FovInfo(45, 30),
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
        Map<Attributes.Attribute, Matrix3D> attributes = new HashMap<>();
        attributes.put(Attributes.Attribute.TRANSLATION, new Translation(0, 50, 0).getMatrix());
        attributes.put(Attributes.Attribute.ORIENTATION, orientation.getMatrix());
        attributes.put(Attributes.Attribute.SCALE, new Scale(0.33,0.33, 0.33).getMatrix());
        sceneObjects.add(new Torus(50, 100, new Material(ambient, diffuse, specular, 20, 0.9), attributes));
        attributes.clear();
        attributes.put(Attributes.Attribute.ORIENTATION, new Orientation(-30, 0, 0).getMatrix());
        sceneObjects.add(new Plane(0, 1, 0, 0, new Material(ambient1, diffuse1, specular1, 10, 0.9), attributes));
        attributes.put(Attributes.Attribute.TRANSLATION,new Translation(-20,0,-100).getMatrix());
        sceneObjects.add(new Sphere(30, new Material(ambient2, diffuse2, specular2, 10, 0.0,0.5,2), attributes));

        //sceneObjects.add(new ModelObject(ObjectParser.parse("sheep.obj"), new Material(ambient1, diffuse1, specular1, 20), attributes));
        List<LightSource> lightSources = new ArrayList<>();

       lightSources.add(new PointLightSource(new Point3D(100, 500, -200), Color.WHITE,2,800));
       lightSources.add(new DirectionalLightSource(new Vector3D(1,-1,1), new Color(130, 130, 130)));
//        lightSources.add(new PointLightSource(new Point3D(-100, -500, -300), Color.WHITE));
        //  lightSources.add(new PointLightSource(new Point3D(300, 100, 100), Color.YELLOW));
        new UpdatableRayTracerFrame(camera, sceneObjects, lightSources);
    }
}
