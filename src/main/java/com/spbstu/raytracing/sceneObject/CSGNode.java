package com.spbstu.raytracing.sceneObject;

import com.spbstu.raytracing.math.Ray;
import com.spbstu.raytracing.sceneObject.attributes.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vva
 */
public class CSGNode extends SceneObject {

    final List<SceneObject> childs;


    protected CSGNode(final List<SceneObject> childs, final Map<Attributes.AttributeName, Attribute> attributesMap) {
        super(attributesMap);
        this.childs = childs;
    }

    public static CSGNode fromMap(final ArrayList list, final Map<Integer, Material> materialMap) throws IOException {
        double x = 0, y = 0, z = 0, h = 0, p = 0, r = 0, sx = 1, sy = 1, sz = 1;
        Material material = null;
        List<SceneObject> childs = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof String) {
                material = materialMap.get(System.identityHashCode(object));
                break;
            }
        }
        for (Object object : list) {
            if (object instanceof HashMap) {
                HashMap lcsMap = (HashMap) ((HashMap) object).get("lcs");
                if (lcsMap != null) {
                    x = lcsMap.containsKey("x") ? Double.parseDouble(lcsMap.get("x").toString()) : 0;
                    y = lcsMap.containsKey("y") ? Double.parseDouble(lcsMap.get("y").toString()) : 0;
                    z = lcsMap.containsKey("z") ? Double.parseDouble(lcsMap.get("z").toString()) : 0;

                    h = lcsMap.containsKey("h") ? Double.parseDouble(lcsMap.get("h").toString()) : 0;
                    p = lcsMap.containsKey("p") ? Double.parseDouble(lcsMap.get("p").toString()) : 0;
                    r = lcsMap.containsKey("r") ? Double.parseDouble(lcsMap.get("r").toString()) : 0;

                    sx = lcsMap.containsKey("sx") ? Double.parseDouble(lcsMap.get("sx").toString()) : 1;
                    sy = lcsMap.containsKey("sy") ? Double.parseDouble(lcsMap.get("sy").toString()) : 1;
                    sz = lcsMap.containsKey("sz") ? Double.parseDouble(lcsMap.get("sz").toString()) : 1;
                    break;
                }
            }
        }

        HashMap<Attributes.AttributeName, Attribute> attributes = new HashMap<>();
        attributes.put(Attributes.AttributeName.TRANSLATION, new Translation(x, y, z));
        attributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(h, p, r));
        attributes.put(Attributes.AttributeName.SCALE, new Scale(sx, sy, sz));

        for (Object object : list) {
            if (object instanceof HashMap && !((HashMap) object).containsKey("lcs")) {
                String key = (String) ((HashMap) object).keySet().iterator().next();
                switch (key) {
                    case "node":
                        if (childs instanceof ArrayList) {
                            ((ArrayList) childs).add(fromMap((ArrayList) ((HashMap) object).get("node"), materialMap));
                        }
                        break;
                    case "sphere":
                        childs.add(Sphere.fromMap((HashMap) ((HashMap) object).get("sphere"), material, new HashMap<Attributes.AttributeName, Attribute>()));
                        break;
                    case "cylinder":
                        Map<Attributes.AttributeName, Attribute> cylinderAttributes = new HashMap<>();
//                        cylinderAttributes.put(Attributes.AttributeName.TRANSLATION, new Translation(0, 0, -Double.parseDouble(((HashMap) ((HashMap) object).get("cylinder")).get("height").toString()) / 2));
                        childs.add(Cylinder.fromMap((HashMap) ((HashMap) object).get("cylinder"), material, cylinderAttributes));
                        break;
                    case "plane":
                        childs.add(Plane.fromMap((HashMap) ((HashMap) object).get("plane"), material, new HashMap<Attributes.AttributeName, Attribute>()));
                        break;
                    case "torus":
                        Map<Attributes.AttributeName, Attribute> torusAttributes = new HashMap<>();
                        //torusAttributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(0, 90, 0));
                        childs.add(Torus.fromMap((HashMap) ((HashMap) object).get("torus"), material, torusAttributes));
                        break;
                    case "triangle":
                        childs.add(Triangle.fromMap((HashMap) ((HashMap) object).get("triangle"), material, new HashMap<Attributes.AttributeName, Attribute>()));
                        break;
                    case "cone":
                        Map<Attributes.AttributeName, Attribute> coneAttributes = new HashMap<>();
                        // coneAttributes.put(Attributes.AttributeName.ORIENTATION, new Orientation(90, 0, 0));
                        childs.add(Cone.fromMap((HashMap) ((HashMap) object).get("cone"), material, coneAttributes));
                        break;
                    case "obj_model":
                        childs.add(ModelObject.fromMap((HashMap) ((HashMap) object).get("obj_model"), material, new HashMap<Attributes.AttributeName, Attribute>()));
                        break;
                    case "csg_difference":
                        childs.add(CSGOperationNode.fromMap(CSGOperation.CSG_DIFFERENCE, (HashMap) ((HashMap) object).get("csg_difference"), materialMap));
                        break;
                    case "csg_intersection":
                        childs.add(CSGOperationNode.fromMap(CSGOperation.CSG_INTERSECTION, (HashMap) ((HashMap) object).get("csg_intersection"), materialMap));
                        break;
                    case "csg_union":
                        childs.add(CSGOperationNode.fromMap(CSGOperation.CSG_UNION, (HashMap) ((HashMap) object).get("csg_union"), materialMap));
                        break;
                    default:
                        throw new RuntimeException("Unknown object: " + key);
                }
            }
        }
        return new CSGNode(childs, attributes);
    }

    @Override

    public List<IntersectionInfo> getStaticIntersectionInfo(final Ray ray) {
        List<IntersectionInfo> intersectionInfoList = new ArrayList<>();
        for (SceneObject child : childs) {
            intersectionInfoList.addAll(child.getIntersectionInfo(ray));
        }
        return intersectionInfoList;
    }
}
