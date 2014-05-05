package com.spbstu.raytracing;

import com.spbstu.raytracing.jframe.UpdatableRayTracerFrame;
import com.spbstu.raytracing.lightning.DirectionalLightSource;
import com.spbstu.raytracing.lightning.LightSource;
import com.spbstu.raytracing.lightning.PointLightSource;
import com.spbstu.raytracing.math.Point;
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
 */
public class SomeTest {
    public static void main(String[] args) throws IOException {

        //ObjectParser.parse("model.obj");

        Map<Attributes.AttributeName, Attribute> cameraAttributes = new HashMap<>();
        cameraAttributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(0, -45, 0));
        Camera camera = new Camera(new Point(0, 15, -15), new Vector(0, 0, 1), new Vector(0, 1, 0), new Camera.FovInfo(40, 40),
                new Screen(500, 500), cameraAttributes);

        Material mGrayMaterial = new Material(new Material.Component(0.05, 0.05, 0.05), new Material.Component(0.5, 0.5, 0.5), new Material.Component(1, 1, 1), 30, 0);
        Material mOrangeMaterial = new Material(new Material.Component(0.05, 0.05, 0.05), new Material.Component(1, 0.5, 0), new Material.Component(0, 0, 0), 30, 0);
        Material mGreenMaterial = new Material(new Material.Component(0.05, 0.05, 0.05), new Material.Component(0, 1, 0), new Material.Component(0, 0, 0), 30, 0);

        List<LightSource> lightSources = new ArrayList<>();
        lightSources.add(new PointLightSource(new Point(10, -2, 0), Color.YELLOW, 2, 20));
        lightSources.add(new DirectionalLightSource(new Vector(0, -1, 0), Color.WHITE));

        Map<Attributes.AttributeName, Attribute> treeAttributes = new HashMap<>();
        treeAttributes.put(Attributes.AttributeName.TRANSLATION, new Translation(0, 0, 0));
        treeAttributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(0, 0, 0));
        CSGOperationNode node = new CSGOperationNode(
                new Cylinder(4, 0.1, mGreenMaterial, new HashMap<Attributes.AttributeName, Attribute>()),
                new Cylinder(1, 0.2, mGreenMaterial, new HashMap<Attributes.AttributeName, Attribute>()),
                CSGOperation.CSG_DIFFERENCE,
                treeAttributes

        );
        List<SceneObject> sceneObjects = new ArrayList<>();
       // sceneObjects.add(node);
        Map<Attributes.AttributeName, Attribute> bunnyAttributes = new HashMap<>();
        bunnyAttributes.put(Attributes.AttributeName.TRANSLATION, new Translation(0, 0, 0));
        bunnyAttributes.put(Attributes.AttributeName.SCALE, new Scale(10, 10, 10));
        bunnyAttributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(0, 0, 0));
        sceneObjects.add(new ModelObject(ModelObjectParserDefault.parse("stanford_bunny.obj"), mOrangeMaterial, bunnyAttributes));
        Map<Attributes.AttributeName, Attribute> pAttributes = new HashMap<>();
        pAttributes.put(Attributes.AttributeName.ORIENTATION,new Orientation(90,0,0));
        pAttributes.put(Attributes.AttributeName.TRANSLATION, new Translation(0, 0, 0));
        sceneObjects.add(new Cylinder(6, 1, mGrayMaterial, pAttributes));
        //lightSources.add(new PointLightSource(new Point(100, 500, -200), Color.WHITE, 2, 800));
        // lightSources.add(new DirectionalLightSource(new Vector(1, -1, 1), new Color(130, 130, 130)));
        new UpdatableRayTracerFrame(camera, sceneObjects, lightSources);
    }
}
